/**
 * 新增 / 修改会员表单的提交绑定。
 *
 * 校验规则不在这里定义——统一由 js/validation.js 的 validateMemberForm 提供，
 * 保证注册页、新增页、修改页三处标准完全一致。
 *
 * 用法（在各页面 </body> 前）：
 *   <script src="js/validation.js"></script>
 *   <script>initMemberFormCheck('addForm');</script>   // 或 'editForm'
 */

/**
 * 为指定表单挂载统一校验。
 * @param {string} formId 表单元素 id，如 'addForm' / 'editForm'
 */
function initMemberFormCheck(formId) {
    const form = document.getElementById(formId);
    if (!form) return;   // 页面上没有该表单（例如被复用到其它页面），静默退出

    form.addEventListener('submit', function (e) {
        // 校验通过则由浏览器原生提交（action 指向对应 Servlet）；
        // 不通过时阻止提交，validateMemberForm 内部已弹出提示并聚焦到问题字段。
        if (!validateMemberForm(form)) {
            e.preventDefault();
        }
    });
}
