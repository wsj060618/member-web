package com.perswsj.memberweb.dao;

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
}
