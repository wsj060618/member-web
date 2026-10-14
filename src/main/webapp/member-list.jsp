<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
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
        ${empty username ? "" : username}${empty username ? "" : "，欢迎登录！"}<a href="main.html">首页</a>&nbsp;<a href="register.html">注册</a>&nbsp;<a href="login.jsp">登录</a>&nbsp;<a href="member-list.jsp">会员列表</a>
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
        <!-- 会员一多无法快速定位，补充独立一行的搜索入口（按用户名/手机号即时过滤） -->
        <div class="list-tools">
            <input type="text" id="searchInput" class="search-input" placeholder="按用户名/手机号搜索" aria-label="按用户名/手机号搜索">
            <a class="btn btn-search" id="btnSearch" href="#">搜索</a>
        </div>
        <!-- 删除是危险操作却无二次确认，点击即删——为每个"删除"增加确认提示 -->
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
            <tr data-id="1">
                <td><input type="checkbox" class="row-check" aria-label="选择"></td>
                <td>1</td>
                <td>tom</td>
                <td>tom@x.com</td>
                <td>13800000001</td>
                <td>男</td>
                <td>运动</td>
                <td><a class="btn-sm" href="#">查看</a> <a class="btn-sm danger" href="#">删除</a></td>
            </tr>
            <tr data-id="2">
                <td><input type="checkbox" class="row-check" aria-label="选择"></td>
                <td>2</td>
                <td>jerry</td>
                <td>jerry@x.com</td>
                <td>13800000002</td>
                <td>男</td>
                <td>编程</td>
                <td><a class="btn-sm" href="#">查看</a> <a class="btn-sm danger" href="#">删除</a></td>
            </tr>
            <tr data-id="3">
                <td><input type="checkbox" class="row-check" aria-label="选择"></td>
                <td>3</td>
                <td>lily</td>
                <td>lily@x.com</td>
                <td>13800000003</td>
                <td>女</td>
                <td>阅读</td>
                <td><a class="btn-sm" href="#">查看</a> <a class="btn-sm danger" href="#">删除</a></td>
            </tr>
            <tr data-id="4">
                <td><input type="checkbox" class="row-check" aria-label="选择"></td>
                <td>4</td>
                <td>luck</td>
                <td>luck@x.com</td>
                <td>13800000004</td>
                <td>男</td>
                <td>音乐</td>
                <td><a class="btn-sm" href="#">查看</a> <a class="btn-sm danger" href="#">删除</a></td>
            </tr>
            <tr data-id="5">
                <td><input type="checkbox" class="row-check" aria-label="选择"></td>
                <td>5</td>
                <td>amy</td>
                <td>amy@x.com</td>
                <td>13800000005</td>
                <td>女</td>
                <td>运动</td>
                <td><a class="btn-sm" href="#">查看</a> <a class="btn-sm danger" href="#">删除</a></td>
            </tr>
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