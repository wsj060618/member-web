/**
 * 会员表单的统一校验规则：注册页、新增页、修改页共用这一份，避免三处标准不一致。
 */

const RULE_USERNAME = {
    test: (v) => /^[a-zA-Z0-9_]{3,16}$/.test(v),
    message: '用户名必须是3~16位字母、数字、下划线组合'
};

const RULE_PASSWORD = {
    test: (v) => v.length >= 6 && v.length <= 16,
    message: '密码长度必须是6~16位'
};

const RULE_EMAIL = {
    test: (v) => /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(v),
    message: '邮箱格式不正确'
};

const RULE_PHONE = {
    test: (v) => /^1[3-9][0-9]{9}$/.test(v),
    message: '手机号格式不正确'
};

function bindMemberFormValidation(formId) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener('submit', function (e) {
        if (!validateMemberForm(form)) {
            e.preventDefault();
        }
    });
}

function validateMemberForm(form) {
    // 用 && 短路，任一项不通过就停止
    return validateUsername(form)
        && validatePassword(form)
        && validateConfirmPassword(form)
        && validateOptional(form, 'email', RULE_EMAIL)
        && validateOptional(form, 'phone', RULE_PHONE);
}

function validateUsername(form) {
    const el = form.querySelector('#username');
    if (!el) return true;

    const value = el.value.trim();
    if (value === '') {
        return fail(el, '用户名不能为空');
    }
    if (!RULE_USERNAME.test(value)) {
        return fail(el, RULE_USERNAME.message);
    }
    return true;
}

function validatePassword(form) {
    const el = form.querySelector('#password');
    if (!el) return true;

    // 不做 trim：空格也是合法字符，trim 会改掉用户真实输入
    if (el.value === '') {
        return fail(el, '密码不能为空');
    }
    if (!RULE_PASSWORD.test(el.value)) {
        return fail(el, RULE_PASSWORD.message);
    }
    return true;
}

/** 只有注册页有确认密码框，其他页面自动跳过 */
function validateConfirmPassword(form) {
    const confirmEl = form.querySelector('#confirmPwd');
    if (!confirmEl) return true;

    const passwordEl = form.querySelector('#password');
    if (confirmEl.value !== (passwordEl ? passwordEl.value : '')) {
        return fail(confirmEl, '确认密码必须和密码一致');
    }
    return true;
}

function validateOptional(form, id, rule) {
    const el = form.querySelector('#' + id);
    if (!el) return true;

    const value = el.value.trim();
    if (value === '') return true;
    if (!rule.test(value)) {
        return fail(el, rule.message);
    }
    return true;
}

function fail(el, message) {
    alert(message);
    el.focus();
    return false;
}
