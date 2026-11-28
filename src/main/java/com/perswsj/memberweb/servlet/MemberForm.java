package com.perswsj.memberweb.servlet;

import com.perswsj.memberweb.pojo.Member;
import com.perswsj.memberweb.util.WebUtil;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 新增 / 修改会员表单的读取与校验，两个 Servlet 共用同一套规则。
 */
public final class MemberForm {

    private MemberForm() {
    }

    /** 把请求参数装配成 Member；新增时 id 传 null，修改时传解析出的 id。 */
    public static Member fromRequest(HttpServletRequest req, Integer id) {
        String username = WebUtil.trim(req.getParameter("username"));
        // 密码不做 trim：空格也是合法字符
        String password = req.getParameter("password");
        return new Member(
                id,
                username,
                password,
                WebUtil.trim(req.getParameter("email")),
                WebUtil.trim(req.getParameter("phone")),
                req.getParameter("gender"),
                WebUtil.trim(req.getParameter("hobby")),
                WebUtil.trim(req.getParameter("remark")));
    }

    /** 返回 null 表示通过，否则返回失败原因（不含"新增/修改失败："前缀）。 */
    public static String validate(Member member) {
        if (member.getUname().isEmpty()) {
            return "用户名不能为空";
        }
        if (member.getPassword() == null || member.getPassword().isEmpty()) {
            return "密码不能为空";
        }
        return null;
    }
}
