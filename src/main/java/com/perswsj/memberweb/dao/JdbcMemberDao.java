package com.perswsj.memberweb.dao;

import com.perswsj.memberweb.exception.DuplicateUsernameException;
import com.perswsj.memberweb.pojo.Member;
import com.perswsj.memberweb.util.DataSourceUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link MemberDao} 的 JDBC 实现。
 * <p>
 * 本类只持有无状态的 SQL 常量；Connection、PreparedStatement、ResultSet 一律是方法局部变量，
 * 不能提升为成员字段，否则多请求并发会互相覆盖。
 */
public class JdbcMemberDao implements MemberDao {

    private static final String COLUMNS = "id, username, password, email, phone, gender, hobby, remark";

    private static final String SQL_FIND_ALL =
            "SELECT " + COLUMNS + " FROM `member` ORDER BY id";

    private static final String SQL_FIND_BY_ID =
            "SELECT " + COLUMNS + " FROM `member` WHERE id = ?";

    private static final String SQL_FIND_BY_NAME =
            "SELECT " + COLUMNS + " FROM `member` WHERE username LIKE CONCAT('%', ?, '%') ORDER BY id";

    private static final String SQL_ADD =
            "INSERT INTO `member`(username, password, email, phone, gender, hobby, remark) VALUES(?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE =
            "UPDATE `member` SET username = ?, password = ?, email = ?, phone = ?, gender = ?, hobby = ?, remark = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM `member` WHERE id = ?";

    @Override
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DataSourceUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                members.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询全部会员失败", e);
        }
        return members;
    }

    @Override
    public Member findById(Integer id) {
        if (id == null) {
            return null;
        }
        try (Connection conn = DataSourceUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("按 id 查询会员失败，id = " + id, e);
        }
    }

    @Override
    public List<Member> findByName(String name) {
        List<Member> members = new ArrayList<>();
        if (name == null || name.isBlank()) {
            return members;
        }
        try (Connection conn = DataSourceUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_NAME)) {
            // 关键字作为参数绑定，不拼接进 SQL，避免 SQL 注入
            ps.setString(1, name.trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    members.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("按用户名查询会员失败，keyword = " + name, e);
        }
        return members;
    }

    /**
     * 用户名重复时数据库抛出 1062，转成 {@link DuplicateUsernameException} 供控制层单独提示。
     */
    @Override
    public int add(Member m) {
        try (Connection conn = DataSourceUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ADD)) {
            ps.setString(1, m.getUname());
            ps.setString(2, m.getPassword());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getPhone());
            ps.setString(5, toDbGender(m.getSex()));
            ps.setString(6, m.getHobby());
            ps.setString(7, m.getRemark());
            return ps.executeUpdate();
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException
                    || e.getErrorCode() == DuplicateUsernameException.MYSQL_DUPLICATE_ENTRY) {
                throw new DuplicateUsernameException(m.getUname(), e);
            }
            throw new RuntimeException("新增会员失败，username = " + m.getUname(), e);
        }
    }

    /**
     * 返回 0 表示 id 不存在，或提交的数据与库中原值完全相同（MySQL 不计为"行被改变"）。
     */
    @Override
    public int update(Member m) {
        try (Connection conn = DataSourceUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, m.getUname());
            ps.setString(2, m.getPassword());
            ps.setString(3, m.getEmail());
            ps.setString(4, m.getPhone());
            ps.setString(5, toDbGender(m.getSex()));
            ps.setString(6, m.getHobby());
            ps.setString(7, m.getRemark());
            ps.setInt(8, m.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            if (e instanceof SQLIntegrityConstraintViolationException
                    || e.getErrorCode() == DuplicateUsernameException.MYSQL_DUPLICATE_ENTRY) {
                throw new DuplicateUsernameException(m.getUname(), e);
            }
            throw new RuntimeException("修改会员失败，id = " + m.getId(), e);
        }
    }

    @Override
    public int deleteById(int id) {
        try (Connection conn = DataSourceUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("删除会员失败，id = " + id, e);
        }
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getInt("id"));
        member.setUname(rs.getString("username"));
        member.setPassword(rs.getString("password"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setSex(normalizeGender(rs.getString("gender")));
        member.setHobby(rs.getString("hobby"));
        member.setRemark(rs.getString("remark"));
        return member;
    }

    /**
     * 库中 gender 为 M/F，领域层统一用 male/female，便于 getSexText 显示中文。
     */
    private String normalizeGender(String gender) {
        if (gender == null) {
            return null;
        }
        return switch (gender.trim().toLowerCase()) {
            case "m", "male", "男" -> "male";
            case "f", "female", "女" -> "female";
            default -> gender;
        };
    }

    /**
     * gender 列是 char(1)，直接写入 "male" 会被截断，写库前必须转成 M/F。
     */
    private String toDbGender(String sex) {
        if (sex == null || sex.isBlank()) {
            return "M";
        }
        return switch (sex.trim().toLowerCase()) {
            case "female", "f", "女" -> "F";
            default -> "M";
        };
    }
}
