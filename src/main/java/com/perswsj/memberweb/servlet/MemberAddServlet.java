package com.perswsj.memberweb.servlet;

import com.perswsj.memberweb.exception.DuplicateUsernameException;
import com.perswsj.memberweb.dao.JdbcMemberDao;
import com.perswsj.memberweb.dao.MemberDao;
import com.perswsj.memberweb.pojo.Member;
import com.perswsj.memberweb.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 新增会员：GET 显示表单，POST 校验并写库，完成后重定向回列表页（PRG，避免刷新重复提交）。
 */
@WebServlet("/member-add")
public class MemberAddServlet extends HttpServlet {

    private final MemberDao memberDao = new JdbcMemberDao();

    /** GET：显示新增表单 */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/add.jsp").forward(req, resp);
    }

    /** POST：接收并保存表单数据 */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 必须在读取任何参数之前设置编码，否则中文乱码
        req.setCharacterEncoding("UTF-8");

        Member member = MemberForm.fromRequest(req, null);

        String error = MemberForm.validate(member);
        if (error != null) {
            WebUtil.redirectToList(resp, "新增失败：" + error);
            return;
        }

        try {
            int rows = memberDao.add(member);
            if (rows > 0) {
                WebUtil.redirectToList(resp, "新增成功");
            } else {
                // 未抛异常但影响 0 行，同样算失败
                WebUtil.redirectToList(resp, "新增失败，请重试");
            }
        } catch (DuplicateUsernameException e) {
            log("新增会员用户名重复：" + member.getUname());
            WebUtil.redirectToList(resp, "用户名已存在");
        } catch (RuntimeException e) {
            log("新增会员失败，username = " + member.getUname(), e);
            WebUtil.redirectToList(resp, "新增失败，请稍后重试");
        }
    }
}
