<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>会员管理系统</title>
    <link rel="stylesheet" href="css/site.css">
    <style>
        .list-header{
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 16px;
        }
        .btn-add{
            background-color: #409eff;
            color: #fff;
            text-decoration: none;
        }
        /* 标题行右侧的按钮组，两个按钮并排、间距统一 */
        .header-actions{
            display: flex;
            align-items: center;
            gap: 8px;
        }
        /* 列表只有标题和新增按钮，会员一多就无法按用户名/手机号快速查找 */
        .list-tools{
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 16px;
        }
        .list-tools .search-input,
        .list-tools .btn-search{
            height: 30px;      /* 统一高度 */
            padding-top: 0;
            padding-bottom: 0;
        }
        .list-tools .search-input{
            width: 200px;
            padding: 0 10px;
            border: 1px solid #dcdfe6;
            border-radius: 4px;
        }
        .btn-search{
            display: inline-flex;
            align-items: center;
            background-color: #f5f7fa;
            color: #409eff;
            border: 1px solid #dcdfe6;
            text-decoration: none;
        }
        .btn-batchdel{
            background-color: #f56c6c;
            color: #fff;
            border: 1px solid #f56c6c;
            text-decoration: none;
        }
        .btn-sm{
            display: inline-block;
            padding: 3px 10px;
            font-size: 12px;
            border-radius: 4px;
            border: 1px solid #409eff;
            background-color: #409eff;
            color: #fff;
            text-decoration: none;
        }
        .btn-sm.danger{
            border-color: #f56c6c;
            background-color: #f56c6c;
        }
        .btn-sm + .btn-sm{
            margin-left: 6px;
        }
    </style>
</head>
<body>
<header class="site-header">
    <h1>会员管理系统</h1>
    <nav>
        ${username == null ? '' : username + "，欢迎登录！"}<a href="main.html">首页</a>&nbsp;<a href="register.html">注册</a>&nbsp;<a href="login.jsp">登录</a>&nbsp;<a href="member-list.jsp">会员列表</a>
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

<script>
    // 静态元素引用
    let table = document.querySelector('table.data');
    let tbody = table.querySelector('tbody');
    let checkAll = document.getElementById('checkAll');
    let btnBatchDelete = document.getElementById('btnBatchDelete');
    let searchInput = document.getElementById('searchInput');
    let btnSearch = document.getElementById('btnSearch');

    // XSS 转义：后端数据渲染前必须转义
    function escapeHtml(s) {
        return String(s == null ? '' : s)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;');
    }

    // 根据当前行复选框状态，同步表头全选框
    function syncCheckAll() {
        let total = tbody.querySelectorAll('.row-check').length;
        let checked = tbody.querySelectorAll('.row-check:checked').length;
        checkAll.checked = (total > 0 && total === checked);
    }

    // 事件委托：整个表格只绑一次，动态行也能自动生效
    table.addEventListener('click', function (e) {
        let target = e.target;

        // 表头全选 / 全不选
        if (target === checkAll) {
            tbody.querySelectorAll('.row-check').forEach(function (cb) {
                cb.checked = checkAll.checked;
            });
            return;
        }

        // 行复选框被点击 → 重新判断表头状态
        if (target.classList.contains('row-check')) {
            syncCheckAll();
            return;
        }

        // 单条删除
        let delBtn = target.closest('.btn-sm.danger');
        if (delBtn) {
            if (confirm('确定删除该会员？')) {
                let id = delBtn.closest('tr').dataset.id;
                // TODO: 接入后端 DELETE /member/{id}（用 id）
                delBtn.closest('tr').remove();
                syncCheckAll();
            }
        }
    });

    // 批量删除
    btnBatchDelete.addEventListener('click', function (e) {
        e.preventDefault();
        let checkedBoxes = tbody.querySelectorAll('.row-check:checked');
        if (checkedBoxes.length === 0) {
            alert('请先勾选要删除的会员');
            return;
        }
        if (!confirm('确定删除选中的 ' + checkedBoxes.length + ' 条会员？')) return;

        let ids = [];
        checkedBoxes.forEach(function (cb) {
            let tr = cb.closest('tr');
            if (tr.dataset.id) ids.push(tr.dataset.id);
            tr.remove();
        });
        // TODO: 把 ids 发给后端批量删除，例如 DELETE /member/batch，body: JSON.stringify(ids)
        syncCheckAll();
    });

    // 渲染列表（后端数据 → 表格；复选框列已内置）
    function renderList(list) {
        if (!list || list.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;color:#909399;">暂无数据</td></tr>';
            checkAll.checked = false;
            return;
        }
        tbody.innerHTML = list.map(function (m) {
            return '<tr data-id="' + escapeHtml(m.id) + '">' +
                '<td><input type="checkbox" class="row-check" aria-label="选择"></td>' +
                '<td>' + escapeHtml(m.id) + '</td>' +
                '<td>' + escapeHtml(m.username) + '</td>' +
                '<td>' + escapeHtml(m.email) + '</td>' +
                '<td>' + escapeHtml(m.phone) + '</td>' +
                '<td>' + escapeHtml(m.gender) + '</td>' +
                '<td>' + escapeHtml(m.hobbies) + '</td>' +
                '<td><a class="btn-sm" href="#">查看</a> <a class="btn-sm danger" href="#">删除</a></td>' +
                '</tr>';
        }).join('');
        checkAll.checked = false;   // 重新渲染后重置全选
    }

    //  Ajax 搜索：GET /member/search?kw=xxx
    function doSearch() {
        let kw = searchInput.value.trim();
        let xhr = new XMLHttpRequest();
        xhr.open('GET', 'http://localhost:8080/member/search?kw=' + encodeURIComponent(kw), true);
        xhr.setRequestHeader('Accept', 'application/json');

        xhr.onload = function () {
            if (xhr.status === 200) {
                try {
                    renderList(JSON.parse(xhr.responseText));
                } catch (err) {
                    alert('响应数据解析失败：' + err.message);
                }
            } else if (xhr.status === 404) {
                alert('搜索接口未就绪（404），请确认后端 /member/search 已部署');
            } else if (xhr.status >= 500) {
                alert('服务器异常（' + xhr.status + '），请稍后重试');
            } else {
                alert('请求失败，HTTP 状态码：' + xhr.status);
            }
        };
        xhr.onerror = function () {
            alert('网络错误：请检查后端服务是否启动');
        };
        xhr.send();
    }

    btnSearch.addEventListener('click', function (e) {
        e.preventDefault();
        doSearch();
    });
    searchInput.addEventListener('keyup', function (e) {
        if (e.key === 'Enter') doSearch();
    });
    searchInput.addEventListener('input', function () {
        if (searchInput.value.trim() === '') doSearch();
    });

    // 后端就绪后，放开下面这行即可用真实数据覆盖静态表格：
    // window.addEventListener('load', doSearch);
</script>

</body>
</html>