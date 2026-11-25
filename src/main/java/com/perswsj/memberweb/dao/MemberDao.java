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
}
