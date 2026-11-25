// 注册页提交：先无条件阻止默认提交，校验通过后再手动 submit。
// 校验规则来自 js/validation.js，本文件只管提交时机。

(function () {
    const form = document.getElementById('regForm');
    if (!form) return;

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        if (!validateMemberForm(form)) return;

        form.submit();
    });
})();
