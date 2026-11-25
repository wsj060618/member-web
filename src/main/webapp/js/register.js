// 注册页提交逻辑。
// 校验规则统一来自 js/validation.js（唯一规则源），本文件只负责"校验通过后提交"这一步。
// 引入顺序：先 validation.js，后本文件。

(function () {
    const form = document.getElementById('regForm');
    if (!form) return;

    form.addEventListener('submit', function (e) {
        // 本页采用"全手动"提交模式：先无条件阻止默认提交，校验通过后再手动 submit
        e.preventDefault();

        // 调用统一校验；不通过时内部已弹窗提示并聚焦，直接返回即可
        if (!validateMemberForm(form)) return;

        // 全部通过：放行原生提交（action="register" method="post"）
        form.submit();
    });
})();
