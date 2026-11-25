<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>新增会员 - 会员管理系统</title>
    <%-- 沿用实验五的同一套 CSS 与顶部导航，保证各页面风格统一 --%>
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
        <h2>新增会员</h2>
        <%-- method 必须为 post：数据写库属于修改操作，用 get 会把密码暴露在地址栏和访问日志里 --%>
        <%-- action 指向新增会员 Servlet，由它在 doPost 中接收参数并写入 member 表 --%>
        <form id="addForm" action="member-add" method="post">
            <div class="form-item">
                <label for="username">用户名（必填）</label>
                <input type="text" id="username" name="username" placeholder="3~16位字母数字下划线" required>
            </div>

            <div class="form-item">
                <label for="password">密码（必填）</label>
                <input type="password" id="password" name="password" placeholder="6~16位字母数字" required>
            </div>

            <div class="form-item">
                <label for="email">邮箱</label>
                <input type="email" id="email" name="email" placeholder="example@xx.com">
            </div>

            <div class="form-item">
                <label for="phone">手机号</label>
                <input type="tel" id="phone" name="phone" placeholder="11位手机号">
            </div>

            <%-- 性别：指导书要求取值为 M/F，默认选中"男" --%>
            <div class="form-item">
                <label>性别</label>
                <label class="radio-label">
                    <input type="radio" id="gender_m" name="gender" value="M" checked>&nbsp;男
                </label>
                <label class="radio-label">
                    <input type="radio" id="gender_f" name="gender" value="F">&nbsp;女
                </label>
            </div>

            <div class="form-item">
                <label for="hobby">爱好</label>
                <select id="hobby" name="hobby">
                    <option value="">请选择</option>
                    <option value="reading">阅读</option>
                    <option value="music">音乐</option>
                    <option value="traveling">旅行</option>
                    <option value="sports">运动</option>
                    <option value="coding">编程</option>
                </select>
            </div>

            <div class="form-item">
                <label for="remark">备注</label>
                <textarea id="remark" name="remark" placeholder="选填"></textarea>
            </div>

            <div class="form-item form-actions">
                <input type="submit" class="btn btn-submit" value="保存">
                <input type="reset" class="btn btn-reset" value="重置">
                <%-- 返回列表：直接指向列表 Servlet，不依赖浏览器后退，避免回到已失效的表单状态 --%>
                <a class="btn btn-back" href="member-list">返回列表</a>
            </div>
        </form>
    </section>
</div>

<footer class="site-footer">
    <p>&copy; 2026 长沙工业学院 • 应用程序设计课程</p>
</footer>

<%-- 校验规则统一由 js/validation.js 提供（唯一规则源），member-form.js 只做绑定 --%>
<script src="js/validation.js"></script>
<script src="js/member-form.js"></script>
<script>initMemberFormCheck('addForm');</script>

</body>
</html>
