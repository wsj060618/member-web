package com.perswsj.memberweb.dao;

import com.perswsj.memberweb.pojo.Member;
import com.perswsj.memberweb.util.DataSourceUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link MemberDao} 的 JDBC 实现：连接来自 DataSourceUtil 管理的连接池。
 * <p>
 * 本类只持有无状态的 SQL 常量，Connection、PreparedStatement、ResultSet 一律是方法局部变量，
 * 由 try-with-resources 按声明的相反次序自动关闭（ResultSet → PreparedStatement → Connection），
 * 关闭即把连接归还连接池；也不向控制层返回仍依赖连接的 ResultSet，只返回封装好的 Member 对象。
 */
public class JdbcMemberDao implements MemberDao {

    /** 查询列清单：集中一处，保证三个查询方法取出的列与顺序一致 */
    private static final String COLUMNS = "id, username, password, email, phone, gender, hobby, remark";

    private static final String SQL_FIND_ALL =
            "SELECT " + COLUMNS + " FROM `member` ORDER BY id";

    private static final String SQL_FIND_BY_ID =
            "SELECT " + COLUMNS + " FROM `member` WHERE id = ?";

    /**
     * 按用户名模糊查询。关键字整体作为一个参数绑定到 ?，
     * SQL 结构保持为常量字符串，不把用户输入拼接进 SQL，从根本上避免 SQL 注入。
     */
    private static final String SQL_FIND_BY_NAME =
            "SELECT " + COLUMNS + " FROM `member` WHERE username LIKE CONCAT('%', ?, '%') ORDER BY id";

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
            // 占位符下标从 1 开始
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
            // 关键字作为一个参数绑定到占位符：即使输入 ' OR '1'='1，也只会被当成普通字符串比较
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
     * 结果集当前行 → Member 对象，供三个查询方法复用。
     */
    private Member mapRow(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getInt("id"));
        // member 表的 username 列对应 Member.uname
        member.setUname(rs.getString("username"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setSex(normalizeGender(rs.getString("gender")));
        member.setHobby(rs.getString("hobby"));
        member.setRemark(rs.getString("remark"));
        return member;
    }

    /**
     * 数据库里的 gender 是 M/F，而 Member#getSexText 按 male/female 显示中文，
     * 这里统一转成领域层使用的取值。
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
}
