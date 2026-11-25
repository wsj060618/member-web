/**
 * 新增 / 修改会员表单的提交绑定，校验规则全部来自 js/validation.js。
 * 用法：<script>initMemberFormCheck('addForm');</script>（或 'editForm'）
 */

function initMemberFormCheck(formId) {
    const form = document.getElementById(formId);
    if (!form) return;

    form.addEventListener('submit', function (e) {
        if (!validateMemberForm(form)) {
            e.preventDefault();
        }
    });
}
