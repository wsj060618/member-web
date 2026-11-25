let form = document.querySelector('#loginForm');
let username = document.querySelector('#username');
let password = document.querySelector('#password');
let loginMsg = document.querySelector('#loginMsg');

// type 为 success 或 error，对应样式类
function showMsg(text, type) {
    loginMsg.textContent = text;
    loginMsg.className = 'login-msg ' + type;
}

form.addEventListener('submit', (e) => {
    e.preventDefault();

    if (username.value.trim() === '' || password.value.trim() === '') {
        showMsg('请输入用户名或密码', 'error');
        (username.value.trim() === '' ? username : password).focus();
        return;
    }

    let xhr = new XMLHttpRequest();
    xhr.open('POST', 'login');
    xhr.setRequestHeader('Content-Type', 'application/json;charset=utf-8');

    xhr.onload = function () {
        if (xhr.status === 200) {
            let res = JSON.parse(xhr.responseText);
            if (res.success) {
                showMsg('登录成功，欢迎' + username.value, 'success');
                // 先请求 MemberListServlet 再转发到 JSP，不能直连 member-list.jsp
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

    xhr.send(JSON.stringify({
        username: username.value.trim(),
        password: password.value.trim()
    }));
});
