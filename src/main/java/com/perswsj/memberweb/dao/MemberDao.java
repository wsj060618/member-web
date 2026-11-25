package com.perswsj.memberweb.dao;

import com.perswsj.memberweb.exception.DuplicateUsernameException;
import com.perswsj.memberweb.pojo.Member;

import java.util.List;

/**
 * 会员持久层接口，控制层只依赖本接口。
 */
public interface MemberDao {

    List<Member> findAll();

    /** 查不到返回 null */
    Member findById(Integer id);

    List<Member> findByName(String name);

    /** 返回受影响行数；用户名重复时抛 {@link DuplicateUsernameException}。 */
    int add(Member m);

    /** 返回受影响行数；返回 0 表示 id 不存在，或数据与库中原值完全相同。 */
    int update(Member m);

    /** 返回受影响行数；返回 0 表示该 id 对应记录不存在。 */
    int deleteById(int id);
}
