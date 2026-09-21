const registerForm = document.getElementById('regForm');
const registerFields = ['userName', 'pwd', 'name', 'gender', 'mobile', 'email'];

function registerError(id, message) {
    const field = document.getElementById(id);
    document.getElementById(`${id}Error`).textContent = message;
    field.classList.add('invalid');
}

registerForm.addEventListener('submit', async event => {
    event.preventDefault();
    registerFields.forEach(id => { document.getElementById(`${id}Error`).textContent = ''; document.getElementById(id).classList.remove('invalid'); });
    const data = {
        userName: document.getElementById('userName').value.trim(),
        password: document.getElementById('pwd').value,
        name: document.getElementById('name').value.trim(),
        gender: document.getElementById('gender').value,
        mobile: document.getElementById('mobile').value.trim(),
        email: document.getElementById('email').value.trim()
    };
    if (!data.userName) registerError('userName', '请输入账号');
    if (!data.password || data.password.length < 6) registerError('pwd', '密码至少 6 位');
    if (!data.name) registerError('name', '请输入称呼');
    if (!data.gender) registerError('gender', '请选择性别');
    if (!data.mobile) registerError('mobile', '请输入手机号');
    if (!data.email) registerError('email', '请输入邮箱');
    if (registerFields.some(id => document.getElementById(`${id}Error`).textContent)) return;
    const button = document.getElementById('registerButton');
    button.disabled = true;
    button.textContent = '创建中';
    const result = await Sekai.api('/reg/api', { method: 'POST', body: data });
    button.disabled = false;
    button.textContent = '创建账号';
    if (Sekai.isSuccess(result)) { Sekai.toast('账号已创建，欢迎加入 Sekai', 'success'); window.setTimeout(() => { location.href = '/product/list'; }, 650); }
    else if (result) Sekai.toast(result.message || '注册失败，请检查信息', 'error');
});

Sekai.init(null);
