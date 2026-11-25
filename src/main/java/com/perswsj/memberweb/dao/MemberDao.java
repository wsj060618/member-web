package com.perswsj.memberweb.dao;

import com.perswsj.memberweb.exception.DuplicateUsernameException;
import com.perswsj.memberweb.pojo.Member;

import java.util.List;

/**
 * 会员持久层接口。
 * <p>
 * {@code findAll}、{@code findById} 的方法名、参数、返回值与实验五的内存版保持一致，
 * 控制层只依赖接口，把实现类换成 {@link JdbcMemberDao} 即可，调用代码不用改。
 */
public interface MemberDao {

    /** 查询全部会员 */
    List<Member> findAll();

    /** 按主键查询单个会员，查不到返回 null */
    Member findById(Integer id);

    /** 按用户名模糊查询会员 */
    List<Member> findByName(String name);

    /**
     * 新增会员，返回受影响行数。
     * <p>
     * username 列有唯一索引，用户名重复时数据库抛出 1062 错误，
     * 由实现类转成 {@link DuplicateUsernameException}，供控制层区分处理。
     */
    int add(Member m);

    /**
     * 修改会员，返回受影响行数。
     * <p>
     * UPDATE 的 SET 部分更新除 id 外的七个字段，WHERE 用 id 限定记录，共八个占位符。
     * 把用户名改成其他会员已占用的名字时同样会触发唯一索引冲突，抛出
     * {@link DuplicateUsernameException}，供控制层与新增任务统一处理。
     * <p>
     * 返回 0 有两种可能：id 不存在，或提交的数据与库中原值完全相同（MySQL 认为没有行被改变）。
     * 控制层据此给出"编号不存在或数据未变化"的提示。
     */
    int update(Member m);
}
