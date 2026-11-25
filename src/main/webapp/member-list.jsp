<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
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
                <a class="btn btn-add" href="member-add">+ 新增会员</a>
                <%-- ids 隐藏域由 member-list.js 按勾选行动态写入 --%>
                <form id="batchDeleteForm" action="member-delete" method="post"
                      onsubmit="return confirmBatchDelete();">
                    <div id="batchIds"></div>
                    <button type="submit" class="btn btn-batchdel" id="btnBatchDelete">批量删除</button>
                </form>
            </div>
        </div>
        <!-- GET 提交，关键字为空则查全部 -->
        <form class="list-tools" action="member-list" method="get">
            <input type="text" name="keyword" class="search-input" value="<c:out value='${keyword}'/>"
                   placeholder="按用户名搜索" aria-label="按用户名搜索">
            <input type="submit" class="btn btn-search" value="搜索">
            <a class="btn btn-search" href="member-list">显示全部</a>
        </form>
        <%-- MemberAddServlet 以 ?message= 重定向带回（PRG），这里只把它藏进 data-message，
             由 member-list.js 弹窗并清掉 URL 参数，避免刷新时重复提示 --%>
        <c:if test="${not empty message}">
            <div id="flashMessage" data-message="<c:out value='${message}'/>" ${fn:contains(message, '成功') ? 'data-type="success"' : 'data-type="fail"'} hidden></div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <p class="list-error"><c:out value="${errorMessage}"/></p>
        </c:if>
        <c:if test="${not empty keyword}">
            <p class="list-tip">当前搜索关键字：<strong><c:out value="${keyword}"/></strong>，共 ${empty memberList ? 0 : memberList.size()} 条结果</p>
        </c:if>
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
                <c:choose>
                    <c:when test="${empty memberList}">
                        <tr>
                            <td colspan="8" class="empty-tip">暂无会员数据</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${memberList}" var="member">
                            <tr data-id="${member.id}">
                                <td><input type="checkbox" class="row-check" aria-label="选择"></td>
                                <td>${member.id}</td>
                                <td>${member.uname}</td>
                                <td>${member.email}</td>
                                <td>${member.phone}</td>
                                <td>${member.sexText}</td>
                                <td>${member.hobbyText}</td>
                                <%-- 删除必须用 POST：GET 链接会被浏览器预取或误点后直接删库 --%>
                                <td>
                                    <a class="btn-sm" href="detail?id=${member.id}">查看</a>
                                    <a class="btn-sm" href="member-update?id=${member.id}">编辑</a>
                                    <form class="inline-form" action="member-delete" method="post"
                                          onsubmit="return confirm('确定删除会员【${member.uname}】？删除后不可恢复。');">
                                        <input type="hidden" name="id" value="${member.id}">
                                        <button type="submit" class="btn-sm danger">删除</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
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