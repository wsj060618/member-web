/**
 * 会员表单的统一校验规则
 */

/** 用户名：3~16 位字母、数字、下划线 */
const RULE_USERNAME = {
    test: (v) => /^[a-zA-Z0-9_]{3,16}$/.test(v),
    message: '用户名必须是3~16位字母、数字、下划线组合'
};

/** 密码：6~16 位（字母、数字等任意可见字符） */
const RULE_PASSWORD = {
    test: (v) => v.length >= 6 && v.length <= 16,
    message: '密码长度必须是6~16位'
};

/** 邮箱：选填；填了就必须符合基本格式 */
const RULE_EMAIL = {
    test: (v) => /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(v),
    message: '邮箱格式不正确'
};

/** 手机号：选填；填了就必须是 11 位、1 开头、第二位 3~9 */
const RULE_PHONE = {
    test: (v) => /^1[3-9][0-9]{9}$/.test(v),
    message: '手机号格式不正确'
};

/**
 * 对指定表单绑定统一校验（自动阻止非法提交）。
 * 适用于"校验通过就让浏览器原生提交"的场景（add.jsp / edit.jsp）。
 * @param {string} formId 表单元素 id，如 'regForm' / 'addForm' / 'editForm'
 */
function bindMemberFormValidation(formId) {
    const form = document.getElementById(formId);
    if (!form) return;   // 页面上没有该表单时静默退出

    form.addEventListener('submit', function (e) {
        if (!validateMemberForm(form)) {
            e.preventDefault();   // 任一项不通过，阻止提交
        }
    });
}

/**
 * 执行全部校验，返回 true（全部通过）或 false（有不通过项，且已弹窗提示）。
 * 供需要自行控制提交时机的页面直接调用（如 register.js）。
 * @param {HTMLFormElement} form
 * @returns {boolean}
 */
function validateMemberForm(form) {
    // 用 && 短路：一旦某项不通过就停止后续校验，与逐条 return 的原始行为一致
    return validateUsername(form)
        && validatePassword(form)
        && validateConfirmPassword(form)
        && validateOptional(form, 'email', RULE_EMAIL)
        && validateOptional(form, 'phone', RULE_PHONE);
}

/** 用户名：必填 + 格式 */
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

/** 密码：必填 + 长度 */
function validatePassword(form) {
    const el = form.querySelector('#password');
    if (!el) return true;

    // 密码不做 trim：空格也是合法字符，且 trim 会改变用户真实输入
    if (el.value === '') {
        return fail(el, '密码不能为空');
    }
    if (!RULE_PASSWORD.test(el.value)) {
        return fail(el, RULE_PASSWORD.message);
    }
    return true;
}

/** 确认密码：仅在页面上存在该输入框时校验（注册页有，新增/修改页没有） */
function validateConfirmPassword(form) {
    const confirmEl = form.querySelector('#confirmPwd');
    if (!confirmEl) return true;   // 该页面没有确认密码框，跳过

    const passwordEl = form.querySelector('#password');
    if (confirmEl.value !== (passwordEl ? passwordEl.value : '')) {
        return fail(confirmEl, '确认密码必须和密码一致');
    }
    return true;
}

/** 选填字段：留空则跳过，填了才按规则校验 */
function validateOptional(form, id, rule) {
    const el = form.querySelector('#' + id);
    if (!el) return true;

    const value = el.value.trim();
    if (value === '') return true;   // 空值放行
    if (!rule.test(value)) {
        return fail(el, rule.message);
    }
    return true;
}

/** 统一的不通过处理：提示、聚焦、返回 false */
function fail(el, message) {
    alert(message);
    el.focus();
    return false;
}
