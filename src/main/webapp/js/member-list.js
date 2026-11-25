// 会员列表页交互：全选联动、批量删除的确认与 id 收集。
// 单条删除不走这里——它由行内 POST 表单 + onsubmit 的 confirm 完成。

let table = document.querySelector('table.data');
let tbody = table.querySelector('tbody');
let checkAll = document.getElementById('checkAll');

let flashBox = document.getElementById('flashMessage');
if (flashBox) {
    alert(flashBox.dataset.message);
    // 抹掉 URL 上的 message 参数，否则刷新页面会重复弹窗
    let url = new URL(location.href);
    url.searchParams.delete('message');
    history.replaceState(null, '', url);
}

function syncCheckAll() {
    let total = tbody.querySelectorAll('.row-check').length;
    let checked = tbody.querySelectorAll('.row-check:checked').length;
    checkAll.checked = (total > 0 && total === checked);
}

function getCheckedIds() {
    let ids = [];
    tbody.querySelectorAll('.row-check:checked').forEach(function (cb) {
        let tr = cb.closest('tr');
        if (tr && tr.dataset.id) ids.push(tr.dataset.id);
    });
    return ids;
}

/** 由批量删除表单的 onsubmit 调用，返回 false 即取消提交 */
function confirmBatchDelete() {
    let ids = getCheckedIds();

    if (ids.length === 0) {
        alert('请先勾选要删除的会员');
        return false;
    }

    if (!confirm('确定删除选中的 ' + ids.length + ' 条会员？删除后不可恢复。')) {
        return false;
    }

    // 写成同名隐藏域，Servlet 用 getParameterValues("ids") 一次取到
    let holder = document.getElementById('batchIds');
    holder.innerHTML = '';
    ids.forEach(function (id) {
        let input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'ids';
        input.value = id;
        holder.appendChild(input);
    });

    return true;
}

// 事件委托：整个表格只绑一次
table.addEventListener('click', function (e) {
    let target = e.target;

    if (target === checkAll) {
        tbody.querySelectorAll('.row-check').forEach(function (cb) {
            cb.checked = checkAll.checked;
        });
        return;
    }

    if (target.classList.contains('row-check')) {
        syncCheckAll();
        return;
    }
});
