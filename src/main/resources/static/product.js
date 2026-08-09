const form = document.getElementById('pubForm');
const submitBtn = document.getElementById('submitBtn');
const categorySelect = document.getElementById('categorySelect');
const categoryTags = document.getElementById('categoryTags');
const imageInput = document.getElementById('images');
const imagePreview = document.getElementById('imagePreview');
const detailInput = document.getElementById('detail');
const detailPreview = document.getElementById('detailPreview');

let selectedCategories = [];
let imageUrls = [];
let detailUrls = [];

function showToast(message, type) {
    const container = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    const icons = { success: '✓', error: '✕', info: 'ℹ' };
    toast.innerHTML = `<span class="toast-icon">${icons[type] || 'ℹ'}</span><span>${message}</span>`;
    container.appendChild(toast);
    setTimeout(() => {
        toast.classList.add('toast-leaving');
        setTimeout(() => toast.remove(), 300);
    }, 3500);
}

function setLoading(loading) {
    if (loading) {
        submitBtn.classList.add('loading');
        submitBtn.disabled = true;
    } else {
        submitBtn.classList.remove('loading');
        submitBtn.disabled = false;
    }
}

function showError(inputId, message) {
    const item = document.getElementById(inputId).closest('.item');
    const errorMsg = item.querySelector('.error-msg');
    document.getElementById(inputId).classList.add('error');
    if (errorMsg) {
        errorMsg.textContent = message;
        errorMsg.classList.add('visible');
    }
}

function clearError(inputId) {
    const item = document.getElementById(inputId).closest('.item');
    const errorMsg = item.querySelector('.error-msg');
    document.getElementById(inputId).classList.remove('error');
    if (errorMsg) {
        errorMsg.textContent = '';
        errorMsg.classList.remove('visible');
    }
}

function clearAllErrors() {
    document.querySelectorAll('.item input.error, .item textarea.error, .item select.error')
        .forEach(el => {
            el.classList.remove('error');
            const msg = el.closest('.item').querySelector('.error-msg');
            if (msg) {
                msg.textContent = '';
                msg.classList.remove('visible');
            }
        });
}

function addCategoryTag(category) {
    if (selectedCategories.some(c => c.id === category.id)) return;
    selectedCategories.push(category);
    renderCategoryTags();
}

function removeCategory(categoryId) {
    selectedCategories = selectedCategories.filter(c => c.id !== categoryId);
    renderCategoryTags();
}

function renderCategoryTags() {
    categoryTags.innerHTML = selectedCategories.map(c =>
        `<span class="category-tag">${c.name}<span class="remove" data-id="${c.id}">✕</span></span>`
    ).join('');
    categoryTags.querySelectorAll('.remove').forEach(el => {
        el.addEventListener('click', () => removeCategory(Number(el.dataset.id)));
    });
}

function addImagePreview(url, target) {
    if (!url || !url.trim()) return;
    url = url.trim();
    const container = target === 'images' ? imagePreview : detailPreview;
    const urls = target === 'images' ? imageUrls : detailUrls;
    if (urls.includes(url)) return;
    urls.push(url);
    const div = document.createElement('div');
    div.className = 'image-preview-item';
    div.innerHTML = `<img src="${url}" onerror="this.parentElement.remove()" alt="preview"><button class="remove-img" data-url="${url}" data-target="${target}">✕</button>`;
    container.appendChild(div);
    div.querySelector('.remove-img').addEventListener('click', function () {
        const idx = urls.indexOf(this.dataset.url);
        if (idx > -1) urls.splice(idx, 1);
        div.remove();
    });
}

async function loadCategories() {
    try {
        const res = await fetch('/category/list/api');
        const result = await res.json();
        if (result.isSuccess || result.success) {
            const categories = result.data || [];
            categorySelect.innerHTML = '<option value="">选择分类...</option>';
            const parents = categories.filter(c => c.parentCategoryId === 0 || c.parentCategoryId === null);
            const children = categories.filter(c => c.parentCategoryId > 0);
            parents.forEach(p => {
                const opt = document.createElement('option');
                opt.value = p.id;
                opt.textContent = p.name;
                opt.style.fontWeight = '600';
                categorySelect.appendChild(opt);
                children.filter(c => c.parentCategoryId === p.id).forEach(ch => {
                    const sub = document.createElement('option');
                    sub.value = ch.id;
                    sub.textContent = '  └ ' + ch.name;
                    sub.style.color = 'rgba(255,255,255,0.7)';
                    categorySelect.appendChild(sub);
                });
            });
        }
    } catch (err) {
        console.error('Failed to load categories:', err);
    }
}

function validateForm() {
    let valid = true;
    clearAllErrors();

    const name = document.getElementById('name').value.trim();
    if (!name) {
        showError('name', '请输入商品名称');
        valid = false;
    } else if (name.length > 100) {
        showError('name', '商品名称不能超过100个字符');
        valid = false;
    }

    const price = document.getElementById('price').value.trim();
    if (!price) {
        showError('price', '请输入商品价格');
        valid = false;
    } else if (isNaN(Number(price)) || Number(price) < 0) {
        showError('price', '请输入有效的价格');
        valid = false;
    }

    const stock = document.getElementById('stock').value.trim();
    if (!stock) {
        showError('stock', '请输入库存数量');
        valid = false;
    } else if (isNaN(Number(stock)) || Number(stock) < 0 || !Number.isInteger(Number(stock))) {
        showError('stock', '请输入有效的整数库存');
        valid = false;
    }

    const description = document.getElementById('description').value.trim();
    if (!description) {
        showError('description', '请输入商品描述');
        valid = false;
    }

    return valid;
}

form.addEventListener('submit', async function (e) {
    e.preventDefault();
    if (!validateForm()) return;

    const name = document.getElementById('name').value.trim();
    const description = document.getElementById('description').value.trim();
    const price = document.getElementById('price').value.trim();
    const stock = document.getElementById('stock').value.trim();

    const data = {
        name: name,
        description: description,
        price: Number(price),
        stock: Number(stock),
        categoryIds: '[' + selectedCategories.map(c => c.id).join(',') + ']',
        images: imageUrls.length ? '["' + imageUrls.join('","') + '"]' : '[]',
        detail: detailUrls.length ? '["' + detailUrls.join('","') + '"]' : '[]'
    };

    setLoading(true);
    try {
        const response = await fetch('/product/pub/api', {
            body: JSON.stringify(data),
            cache: 'no-cache',
            headers: { 'content-type': 'application/json' },
            method: 'POST'
        });
        const result = await response.json();
        if (result.isSuccess === true || result.success === true) {
            showToast('商品发布成功！正在跳转...', 'success');
            setTimeout(() => { location.href = '/product/list'; }, 1200);
        } else {
            showToast('发布失败：' + (result.message || '未知错误'), 'error');
        }
    } catch (err) {
        showToast('网络异常，发布失败', 'error');
    } finally {
        setLoading(false);
    }
});

categorySelect.addEventListener('change', function () {
    const id = Number(this.value);
    if (!id) return;
    const opt = this.options[this.selectedIndex];
    addCategoryTag({ id, name: opt.textContent.replace('  └ ', '') });
    this.value = '';
});

document.querySelectorAll('.add-image-btn').forEach(btn => {
    btn.addEventListener('click', function () {
        const target = this.dataset.target;
        const input = document.getElementById(target);
        const url = input.value.trim();
        if (!url) return;
        addImagePreview(url, target);
        input.value = '';
    });
});

document.querySelectorAll('.image-url-input').forEach(input => {
    input.addEventListener('keydown', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            const target = this.dataset.target;
            const url = this.value.trim();
            if (!url) return;
            addImagePreview(url, target);
            this.value = '';
        }
    });
});

document.querySelectorAll('.item input, .item textarea').forEach(el => {
    el.addEventListener('input', function () {
        if (this.classList.contains('error')) {
            clearError(this.id);
        }
    });
});

loadCategories();
