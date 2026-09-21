const loginForm = document.getElementById('loginForm');

function loginError(id, message) {
    const field = document.getElementById(id);
    document.getElementById(`${id}Error`).textContent = message;
    field.classList.add('invalid');
}

loginForm.addEventListener('submit', async event => {
    event.preventDefault();
    document.getElementById('usernameError').textContent = '';
    document.getElementById('passwordError').textContent = '';
    const userName = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    if (!userName) loginError('username', '请输入账号');
    if (!password) loginError('password', '请输入密码');
    if (!userName || !password) return;
    const button = document.getElementById('loginButton');
    button.disabled = true;
    button.textContent = '登录中';
    const result = await Sekai.api('/login/api', { method: 'POST', body: { userName, password } });
    button.disabled = false;
    button.textContent = '登录';
    if (Sekai.isSuccess(result)) { Sekai.toast('登录成功，欢迎回来', 'success'); window.setTimeout(() => { location.href = '/product/list'; }, 650); }
    else if (result) Sekai.toast(result.message || '账号或密码不正确', 'error');
});

Sekai.init(null);
