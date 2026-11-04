package com.perswsj.memberweb.dao;

import com.perswsj.memberweb.pojo.Member;

import java.util.ArrayList;
import java.util.List;

public class MemberDao {
    private static List<Member> list;

    static {
        list = new ArrayList<>();
        list.add(new Member(1, "张三", "zhangsan@example.com", "12345678901", "male", "sports"));
        list.add(new Member(2, "李四", "lisi@example.com", "12345678902", "female", "reading"));
        list.add(new Member(3, "王五", "wangwu@example.com", "12345678903", "male", "coding"));
        list.add(new Member(4, "赵六", "zhaoliu@example.com", "12345678904", "female", "music"));
        list.add(new Member(5, "孙七", "sunqi@example.com", "12345678905", "male", "traveling"));
    }

    public List<Member> findAll() {
        return list;
    }

    public List<Member> findByName(String name) {
        List<Member> result = new ArrayList<>();
        for (Member member : list) {
            if (member.getUname().contains(name)) {
                result.add(member);
            }
        }
        return result;
    }

    public Member findById(Integer id) {
        for (Member member : list) {
            if (member.getId() == id) {
                return member;
            }
        }
        return null;
    }
}
