package com.perswsj.memberweb.servlet;

import com.perswsj.memberweb.dao.JdbcMemberDao;
import com.perswsj.memberweb.dao.MemberDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 删除会员。支持单条（参数 id）与批量（多个同名参数 ids）两种提交方式。
 * <p>
 * 删除只接受 POST：GET 可能被浏览器预取、爬虫抓取或地址栏误触，从而在用户无感知时删掉数据。
 */
@WebServlet("/member-delete")
public class MemberDeleteServlet extends HttpServlet {

    private static final String LIST_URL = "member-list";

    private final MemberDao memberDao = new JdbcMemberDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        redirectWithMessage(resp, "删除操作必须用 POST 提交，已取消");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String[] ids = req.getParameterValues("ids");
        if (ids != null && ids.length > 0) {
            handleBatchDelete(resp, ids);
            return;
        }

        Integer id = parseId(req.getParameter("id"));
        if (id == null) {
            redirectWithMessage(resp, "删除失败：编号不存在");
            return;
        }

        try {
            int rows = memberDao.deleteById(id);
            if (rows > 0) {
                redirectWithMessage(resp, "删除成功");
            } else {
                // 未抛异常但影响 0 行，说明记录已不存在
                redirectWithMessage(resp, "删除失败：会员不存在");
            }
        } catch (RuntimeException e) {
            // 异常原文只进日志，不输出到页面，避免泄露 SQL、库名、账号
            log("删除会员失败，id = " + id, e);
            redirectWithMessage(resp, "删除失败，请稍后重试");
        }
    }

    /**
     * 逐条删除而非拼 {@code DELETE ... WHERE id IN (...)}：占位符数量会随勾选条数变化，
     * 需动态拼接；逐条执行时某条失败不影响其余记录，成功条数也更准确。
     */
    private void handleBatchDelete(HttpServletResponse resp, String[] ids) throws IOException {
        int success = 0;
        int invalid = 0;

        for (String raw : ids) {
            Integer id = parseId(raw);
            if (id == null) {
                invalid++;
                continue;
            }
            try {
                if (memberDao.deleteById(id) > 0) {
                    success++;
                }
            } catch (RuntimeException e) {
                log("批量删除会员失败，id = " + id, e);
            }
        }

        if (success == 0) {
            redirectWithMessage(resp, "删除失败：选中的会员都不存在");
        } else if (invalid > 0) {
            redirectWithMessage(resp, "已删除 " + success + " 条会员，另有 " + invalid + " 条编号非法已跳过");
        } else {
            redirectWithMessage(resp, "已删除 " + success + " 条会员");
        }
    }

    /** 参数缺失、非数字、非正数一律返回 null，避免 parseInt 抛异常导致 500。 */
    private Integer parseId(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            int id = Integer.parseInt(raw.trim());
            return id > 0 ? id : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** PRG 重定向。消息含中文，必须 URL 编码，否则浏览器与 Tomcat 解码不一致会乱码。 */
    private void redirectWithMessage(HttpServletResponse resp, String message) throws IOException {
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(LIST_URL + "?message=" + encoded);
    }
}
