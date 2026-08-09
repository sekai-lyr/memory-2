const form = document.getElementById('loginForm');

function showToast(message, type) {
    const container = document.getElementById('toastContainer');
    if (!container) {
        const c = document.createElement('div');
        c.id = 'toastContainer';
        c.style.cssText = 'position:fixed;top:24px;right:24px;z-index:9999;display:flex;flex-direction:column;gap:12px;';
        document.body.appendChild(c);
    }
    const c = document.getElementById('toastContainer');
    const t = document.createElement('div');
    t.className = `toast toast-${type}`;
    const icons = { success: '✓', error: '✕', info: 'ℹ' };
    t.innerHTML = `<span style="font-size:20px;flex-shrink:0">${icons[type] || 'ℹ'}</span><span>${message}</span>`;
    Object.assign(t.style, {
        display: 'flex', alignItems: 'center', gap: '12px',
        padding: '16px 20px', borderRadius: '12px', fontSize: '14px',
        fontWeight: '500', backdropFilter: 'blur(16px)',
        WebkitBackdropFilter: 'blur(16px)',
        boxShadow: '0 8px 32px rgba(0,0,0,0.3)',
        animation: 'toastSlideIn 0.4s cubic-bezier(0.16,1,0.3,1)',
        minWidth: '300px', maxWidth: '440px'
    });
    if (type === 'success') t.style.background = 'rgba(46, 204, 113, 0.9)';
    else if (type === 'error') t.style.background = 'rgba(231, 76, 60, 0.9)';
    else t.style.background = 'rgba(108, 92, 231, 0.9)';
    t.style.color = '#fff';
    c.appendChild(t);
    setTimeout(() => {
        t.style.animation = 'toastSlideOut 0.3s ease forwards';
        setTimeout(() => t.remove(), 300);
    }, 3500);
}

const style = document.createElement('style');
style.textContent = `
    @keyframes toastSlideIn {
        from { opacity: 0; transform: translateX(40px); }
        to { opacity: 1; transform: translateX(0); }
    }
    @keyframes toastSlideOut {
        to { opacity: 0; transform: translateX(40px); }
    }
`;
document.head.appendChild(style);

form.addEventListener('submit', function(e) {
    e.preventDefault();

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();

    if (!username) {
        showToast('请输入账号', 'error');
        return;
    }
    if (!password) {
        showToast('请输入密码', 'error');
        return;
    }

    const data = {
        userName: username,
        password: password
    };

    fetch('/login/api', {
        body: JSON.stringify(data),
        cache: 'no-cache',
        headers: { 'content-type': 'application/json' },
        method: 'POST'
    })
    .then(response => response.json())
    .then(result => {
        if (result.isSuccess == true || result.success == true) {
            showToast('登录成功！欢迎回来 ✦', 'success');
            setTimeout(() => { location.href = '/product/list'; }, 1000);
        } else {
            showToast('登录失败：' + (result.message || '账号或密码错误'), 'error');
        }
    })
    .catch(error => {
        showToast('网络异常，登录失败', 'error');
    });
});
