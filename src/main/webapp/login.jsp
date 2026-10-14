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

<script>
    let usernameInput = document.getElementById('username');
    usernameInput.addEventListener('blur', function () {
        console.log(this.value);
    });

    // 获取表单元素
    let form = document.querySelector('#loginForm');

    // 获取用户名和密码输入框元素
    let username = document.querySelector('#username');
    let password = document.querySelector('#password');

    // 获取提示信息显示区域
    let loginMsg = document.querySelector('#loginMsg');

    // 显示提示信息：type 为 success 或 error
    function showMsg(text, type) {
        loginMsg.textContent = text;
        loginMsg.className = 'login-msg ' + type;
    }

    form.addEventListener('submit', (e) => {
        // 阻止表单提交
        e.preventDefault();

        // 用户名或密码为空（含只输入空格）时，直接提示，不发送请求
        if (username.value.trim() === '' || password.value.trim() === '') {
            showMsg('请输入用户名或密码', 'error');
            (username.value.trim() === '' ? username : password).focus();
            return;
        }

        // 创建 XMLHttpRequest 对象
        let xhr = new XMLHttpRequest();

        // 设置请求方式和发送的服务器地址（相对路径）
        xhr.open('POST', 'login');

        // 设置请求头
        xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');

        // 设置回调函数，处理响应结果
        xhr.onload = function () {
            if (xhr.status === 200) {
                // 登录成功
                let res = JSON.parse(xhr.responseText);
                if (res.success) {
                    showMsg('登录成功，欢迎' + username.value, 'success');
                }
            } else {
                if (xhr.status === 401) {
                    showMsg('用户名或密码错误', 'error');
                } else {
                    showMsg('网络错误( ' + xhr.status + ' )：请检查接口地址和服务器是否已启动', 'error');
                }
            }
        };

        xhr.onerror = function () {
            showMsg('网络错误( ' + xhr.status + ' )：请检查接口地址和服务器是否已启动', 'error');
        };

        // 发送请求，将用户名和密码以 json 格式发送
        xhr.send(JSON.stringify({
            username: username.value,
            password: password.value
        }));
    });
</script>
</body>
</html>