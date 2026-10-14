<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>会员管理系统</title>
    <link rel="stylesheet" href="css/site.css">
    <link rel="stylesheet" href="css/login.css">
</head>
<body>
<header class="site-header">
    <h1>会员管理系统</h1>
    <nav>
        <a href="main.html">首页</a>&nbsp;<a href="register.html">注册</a>&nbsp;<a href="login.jsp">登录</a>
    </nav>
</header>

<div class="container">
    <section class="card card-narrow">
        <h2>会员登录</h2>
        <form id="loginForm" method="post">
            <div class="form-item">
                <label for="username">用户名</label>
                <input type="text" id="username" name="username" placeholder="3~16位字母数字下划线">
            </div>
            <div class="form-item">
                <label for="password">密码</label>
                <input type="password" id="password" name="password" placeholder="6~16位字母数字">
            </div>
            <div class="form-item">
                <label><input type="checkbox" id="remember" name="remember">&nbsp;记住我</label>
            </div>
            <!-- ajax 提示信息显示区域 -->
            <div class="login-msg" id="loginMsg">${loginErrorMsg}</div>
            <div class="form-item">
                <input type="submit" class="btn btn-submit" value="登录">
                <input type="reset" class="btn btn-reset" value="重置">
                <a class="form-link" href="register.html">还没账号？去注册</a>
            </div>
        </form>
    </section>
</div>

<footer class="site-footer">
    <p>&copy; 2026 长沙工业学院 • 应用程序设计课程</p>
</footer>

<script src="js/login.js"></script>
</body>
</html>