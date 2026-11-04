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
                // 提示显示片刻后跳转到会员列表页（先请求 MemberListServlet，再由它转发到 JSP）
                setTimeout(function () {
                    location.href = 'member-list';
                }, 300);
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
        username: username.value.trim(),
        password: password.value.trim()
    }));
});
