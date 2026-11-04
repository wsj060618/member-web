<%@ page import="com.perswsj.memberweb.pojo.Member" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>会员管理系统</title>
    <link rel="stylesheet" href="css/site.css">
    <link rel="stylesheet" href="css/member-list.css">
</head>
<body>
<header class="site-header">
    <h1>会员管理系统</h1>
    <nav>
        ${empty username ? "" : username}${empty username ? "" : "，欢迎登录！"}<a href="main.html">首页</a>&nbsp;<a href="register.html">注册</a>&nbsp;<a href="login.jsp">登录</a>&nbsp;<a href="member-list">会员列表</a>
    </nav>
</header>

<div class="container">
    <section class="card">
        <div class="list-header">
            <h2>会员列表</h2>
            <div class="header-actions">
                <a class="btn btn-add" href="register.html">+ 新增会员</a>
                <a class="btn btn-batchdel" id="btnBatchDelete" href="#">批量删除</a>
            </div>
        </div>
        <!-- 搜索 -->
        <form class="list-tools" action="member-list" method="get">
            <input type="text" name="keyword" class="search-input" value="${keyword}" placeholder="按用户名搜索" aria-label="按用户名搜索">
            <input type="submit" class="btn btn-search" value="搜索">
            <a class="btn btn-search" href="member-list">显示全部</a>
        </form>
        <table class="data">
            <thead>
            <tr>
                <th><input type="checkbox" id="checkAll" aria-label="全选"></th>
                <th>编号</th>
                <th>用户名</th>
                <th>邮箱</th>
                <th>手机</th>
                <th>性别</th>
                <th>爱好</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
<%--                <%--%>
<%--                    List<Member> memberList = (List<Member>) request.getAttribute("memberList");--%>
<%--                    if (memberList != null && !memberList.isEmpty()) {--%>
<%--                        for (Member member : memberList) {--%>
<%--                    %>--%>
<%--                    <tr>--%>
<%--                        <td><input type="checkbox" class="row-check" aria-label="选择"></td>--%>
<%--                        <td><%=member.getId()%>--%>
<%--                        </td>--%>
<%--                        <td><%=member.getUname()%>--%>
<%--                        </td>--%>
<%--                        <td><%=member.getEmail()%>--%>
<%--                        </td>--%>
<%--                        <td><%=member.getPhone()%>--%>
<%--                        </td>--%>
<%--                        <td><%=member.getSexText()%>--%>
<%--                        </td>--%>
<%--                        <td><%=member.getHobbyText()%>--%>
<%--                        </td>--%>
<%--                        <td><a class="btn-sm" href="detail?id=<%= member.getId() %>">查看</a> <a class="btn-sm danger" href="#">删除</a></td>--%>
<%--                    </tr>--%>
<%--                    <%--%>
<%--                        }--%>
<%--                    }--%>
<%--                %>--%>
                <c:forEach items="${memberList}" var="member">
                    <tr>
                        <td><input type="checkbox" class="row-check" aria-label="选择"></td>
                        <td>${member.id}</td>
                        <td>${member.uname}</td>
                        <td>${member.email}</td>
                        <td>${member.phone}</td>
                        <td>${member.sexText}</td>
                        <td>${member.hobbyText}</td>
                        <td><a class="btn-sm" href="detail?id=${member.id}">查看</a> <a class="btn-sm danger" href="#">删除</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </section>
</div>

<footer class="site-footer">
    <p>&copy; 2026 长沙工业学院 • 应用程序设计课程</p>
</footer>

<script src="js/member-list.js"></script>

</body>
</html>