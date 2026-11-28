package com.perswsj.memberweb.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Servlet 层公共小工具：参数解析与 PRG 重定向。
 */
public final class WebUtil {

    private static final String LIST_URL = "member-list";

    private WebUtil() {
    }

    /** null 视为空串，避免把 null 写进数据库。 */
    public static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    /** 参数缺失、非数字、非正数一律返回 null，避免 parseInt 抛异常导致 500。 */
    public static Integer parseId(String raw) {
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
    public static void redirectToList(HttpServletResponse resp, String message) throws IOException {
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(LIST_URL + "?message=" + encoded);
    }
}
