// 静态元素引用
let table = document.querySelector('table.data');
let tbody = table.querySelector('tbody');
let checkAll = document.getElementById('checkAll');
let btnBatchDelete = document.getElementById('btnBatchDelete');
let searchInput = document.getElementById('searchInput');
let btnSearch = document.getElementById('btnSearch');

// XSS 转义：后端数据渲染前必须转义
function escapeHtml(s) {
    return String(s == null ? '' : s)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
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

// 渲染列表（后端数据 → 表格；复选框列已内置）
function renderList(list) {
    if (!list || list.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;color:#909399;">暂无数据</td></tr>';
        checkAll.checked = false;
        return;
    }
    tbody.innerHTML = list.map(function (m) {
        return '<tr data-id="' + escapeHtml(m.id) + '">' +
            '<td><input type="checkbox" class="row-check" aria-label="选择"></td>' +
            '<td>' + escapeHtml(m.id) + '</td>' +
            '<td>' + escapeHtml(m.username) + '</td>' +
            '<td>' + escapeHtml(m.email) + '</td>' +
            '<td>' + escapeHtml(m.phone) + '</td>' +
            '<td>' + escapeHtml(m.gender) + '</td>' +
            '<td>' + escapeHtml(m.hobbies) + '</td>' +
            '<td><a class="btn-sm" href="#">查看</a> <a class="btn-sm danger" href="#">删除</a></td>' +
            '</tr>';
    }).join('');
    checkAll.checked = false;   // 重新渲染后重置全选
}

//  Ajax 搜索：GET member/search?kw=xxx
function doSearch() {
    let kw = searchInput.value.trim();
    let xhr = new XMLHttpRequest();
    xhr.open('GET', 'member/search?kw=' + encodeURIComponent(kw), true);
    xhr.setRequestHeader('Accept', 'application/json');

    xhr.onload = function () {
        if (xhr.status === 200) {
            try {
                renderList(JSON.parse(xhr.responseText));
            } catch (err) {
                alert('响应数据解析失败：' + err.message);
            }
        } else if (xhr.status === 404) {
            alert('搜索接口未就绪（404），请确认后端 /member/search 已部署');
        } else if (xhr.status >= 500) {
            alert('服务器异常（' + xhr.status + '），请稍后重试');
        } else {
            alert('请求失败，HTTP 状态码：' + xhr.status);
        }
    };
    xhr.onerror = function () {
        alert('网络错误：请检查后端服务是否启动');
    };
    xhr.send();
}

btnSearch.addEventListener('click', function (e) {
    e.preventDefault();
    doSearch();
});
searchInput.addEventListener('keyup', function (e) {
    if (e.key === 'Enter') doSearch();
});
searchInput.addEventListener('input', function () {
    if (searchInput.value.trim() === '') doSearch();
});

// 后端就绪后，放开下面这行即可用真实数据覆盖静态表格：
// window.addEventListener('load', doSearch);
