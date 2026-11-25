<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>修改会员 - 会员管理系统</title>
    <link rel="stylesheet" href="css/site.css">
    <link rel="stylesheet" href="css/add.css">
</head>
<body>
<header class="site-header">
    <h1>会员管理系统</h1>
    <nav>
        <a href="main.html">首页</a>
        <a href="register.html">注册</a>
        <a href="login.jsp">登录</a>
        <a href="member-list">会员列表</a>
    </nav>
</header>

<div class="container">
    <section class="card">
        <h2>修改会员</h2>
        <%-- 必须用 post：用 get 会把密码暴露在地址栏和访问日志里 --%>
        <form id="editForm" action="member-update" method="post">
            <input type="hidden" name="id" value="<c:out value='${member.id}'/>">

            <%-- value 用单引号包 c:out，原值里含双引号时也不会撑破属性 --%>
            <div class="form-item">
                <label for="username">用户名（必填）</label>
                <input type="text" id="username" name="username"
                       value="<c:out value='${member.uname}'/>"
                       placeholder="会员登录名，不可与已有会员重复" required>
            </div>

            <div class="form-item">
                <label for="password">密码（必填）</label>
                <input type="password" id="password" name="password"
                       value="<c:out value='${member.password}'/>"
                       placeholder="6~16位字母数字" required>
            </div>

            <div class="form-item">
                <label for="email">邮箱</label>
                <input type="email" id="email" name="email"
                       value="<c:out value='${member.email}'/>"
                       placeholder="example@xx.com">
            </div>

            <div class="form-item">
                <label for="phone">手机号</label>
                <input type="tel" id="phone" name="phone"
                       value="<c:out value='${member.phone}'/>"
                       placeholder="11位手机号">
            </div>

            <div class="form-item">
                <label>性别</label>
                <label class="radio-label">
                    <input type="radio" id="gender_m" name="gender" value="M"
                           <c:if test="${member.sex eq 'male' or member.sex eq 'M'}">checked</c:if>>&nbsp;男
                </label>
                <label class="radio-label">
                    <input type="radio" id="gender_f" name="gender" value="F"
                           <c:if test="${member.sex eq 'female' or member.sex eq 'F'}">checked</c:if>>&nbsp;女
                </label>
            </div>

            <div class="form-item">
                <label for="hobby">爱好</label>
                <select id="hobby" name="hobby">
                    <option value="">请选择</option>
                    <option value="reading" ${member.hobby eq 'reading' ? 'selected' : ''}>阅读</option>
                    <option value="music" ${member.hobby eq 'music' ? 'selected' : ''}>音乐</option>
                    <option value="traveling" ${member.hobby eq 'traveling' ? 'selected' : ''}>旅行</option>
                    <option value="sports" ${member.hobby eq 'sports' ? 'selected' : ''}>运动</option>
                    <option value="coding" ${member.hobby eq 'coding' ? 'selected' : ''}>编程</option>
                </select>
            </div>

            <div class="form-item">
                <label for="remark">备注</label>
                <textarea id="remark" name="remark" placeholder="选填"><c:out value="${member.remark}"/></textarea>
            </div>

            <div class="form-item form-actions">
                <input type="submit" class="btn btn-submit" value="保存修改">
                <input type="reset" class="btn btn-reset" value="重置">
                <a class="btn btn-back" href="member-list">返回列表</a>
            </div>
        </form>
    </section>
</div>

<footer class="site-footer">
    <p>&copy; 2026 长沙工业学院 • 应用程序设计课程</p>
</footer>

<script src="js/validation.js"></script>
<script src="js/member-form.js"></script>
<script>initMemberFormCheck('editForm');</script>

</body>
</html>
