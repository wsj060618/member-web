// 获取表单元素对象
let form = document.querySelector('#regForm');

// 为表单绑定提交事件
form.addEventListener('submit', (e) => {
    // 阻止表单提交
    e.preventDefault();
    // 获取要校验的表单元素
    let username = document.querySelector('#username');
    let password = document.querySelector('#password');
    let confirmPwd = document.querySelector('#confirmPwd');
    let email = document.querySelector('#email');
    let phone = document.querySelector('#phone');

    // 校验表单数据
    // 用户名必须是3~16位字母、数字、下划线
    if (!/^[a-zA-Z0-9_]{3,16}$/.test(username.value)) {
        alert('用户名必须是3~16位字母、数字、下划线组合');
        username.focus();
        return;
    }
    // 密码必须是6~16位
    if (password.value.length < 6 || password.value.length > 16) {
        alert('密码长度必须是6~16位');
        password.focus();
        return;
    }
    // 确认密码必须和密码一致
    if (confirmPwd.value !== password.value) {
        alert('确认密码必须和密码一致');
        confirmPwd.focus();
        return;
    }
    // 邮箱格式必须正确
    if (!/[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(email.value)) {
        alert('邮箱格式不正确');
        email.focus();
        return;
    }
    // 手机号长度必须是11位，必须以1开头，第二位必须是3~9的数字，第三位至11位必须都是数字。
    if (!/^1[3-9][0-9]{9}$/.test(phone.value)) {
        alert('手机号格式不正确');
        phone.focus();
        return;
    }

    form.submit();
});
