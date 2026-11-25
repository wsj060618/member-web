// 静态元素引用
let table = document.querySelector('table.data');
let tbody = table.querySelector('tbody');
let checkAll = document.getElementById('checkAll');
let btnBatchDelete = document.getElementById('btnBatchDelete');

// 新增会员结果
// 脚本在 </body> 前引入，此时 #flashMessage 已在 DOM 中，可直接取用
let flashBox = document.getElementById('flashMessage');
if (flashBox) {
    alert(flashBox.dataset.message);
    // 清掉 URL 上的 message 参数，避免刷新页面重复弹窗
    let url = new URL(location.href);
    url.searchParams.delete('message');
    history.replaceState(null, '', url);
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
