const form = document.getElementById('regForm');

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

    const userName = document.getElementById('userName').value.trim();
    const pwd = document.getElementById('pwd').value.trim();
    const mobile = document.getElementById('mobile').value.trim();
    const email = document.getElementById('email').value.trim();
    const name = document.getElementById('name').value.trim();
    const gender = document.getElementById('gender').value.trim();

    if (!userName) { showToast('请输入用户名', 'error'); return; }
    if (!pwd) { showToast('请输入密码', 'error'); return; }
    if (!mobile) { showToast('请输入手机号', 'error'); return; }
    if (!email) { showToast('请输入邮箱', 'error'); return; }
    if (!name) { showToast('请输入姓名', 'error'); return; }
    if (!gender) { showToast('请选择性别', 'error'); return; }

    const data = {
        userName: userName,
        password: pwd,
        mobile: mobile,
        email: email,
        name: name,
        gender: gender
    };

    fetch('/reg/api', {
        body: JSON.stringify(data),
        cache: 'no-cache',
        headers: { 'content-type': 'application/json' },
        method: 'POST'
    })
    .then(function(response) {
        return response.json();
    })
    .then(function(result) {
        if (result.isSuccess == true || result.success == true) {
            showToast('注册成功！即将跳转登录', 'success');
            setTimeout(() => { location.href = '/login'; }, 1200);
        } else {
            const msg = result?.message || '注册失败，请检查信息';
            showToast('注册失败：' + msg, 'error');
        }
    })
    .catch(function() {
        showToast('网络异常，注册失败', 'error');
    });
});
