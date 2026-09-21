const Sekai = (() => {
    let currentUser = null;

    const $ = (selector, root = document) => root.querySelector(selector);
    const $$ = (selector, root = document) => Array.from(root.querySelectorAll(selector));
    let revealObserver = null;
    let rippleListenerBound = false;

    function esc(value) {
        return String(value == null ? '' : value).replace(/[&<>"']/g, character => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        }[character]));
    }

    function parseImages(value) {
        if (!value) return [];
        try {
            const parsed = JSON.parse(value);
            return Array.isArray(parsed) ? parsed.filter(Boolean) : [String(parsed)];
        } catch (error) {
            return String(value).split(',').map(item => item.trim()).filter(Boolean);
        }
    }

    function firstImage(value) { return parseImages(value)[0] || ''; }
    function fmtMoney(value) { return `¥${(Number(value) || 0).toFixed(2)}`; }
    function isSuccess(result) { return Boolean(result && (result.isSuccess === true || result.success === true)); }
    function dateLabel(value) { return value ? String(value).replace('T', ' ').substring(0, 16) : '刚刚'; }

    function ensureToastBox() {
        let box = $('#toastBox');
        if (!box) {
            box = document.createElement('div');
            box.id = 'toastBox';
            box.className = 'toast-container';
            document.body.appendChild(box);
        }
        return box;
    }

    function toast(message, type = 'info') {
        const item = document.createElement('div');
        item.className = `toast ${type}`;
        item.textContent = message;
        ensureToastBox().appendChild(item);
        window.setTimeout(() => {
            item.classList.add('leaving');
            window.setTimeout(() => item.remove(), 240);
        }, 3000);
    }

    async function api(url, options = {}) {
        const { redirectOnUnauthorized = true, ...requestOptions } = options;
        const request = { ...requestOptions, headers: { ...(requestOptions.headers || {}) } };
        if (request.body && typeof request.body !== 'string') {
            request.body = JSON.stringify(request.body);
            request.headers['Content-Type'] = 'application/json';
        }
        try {
            const response = await fetch(url, request);
            const result = await response.json().catch(() => null);
            if (result && result.code === '401') {
                if (redirectOnUnauthorized) {
                    toast('请先登录', 'error');
                    window.setTimeout(() => { window.location.href = '/login'; }, 700);
                }
                return null;
            }
            return result;
        } catch (error) {
            toast('网络异常，请稍后重试', 'error');
            return null;
        }
    }

    async function loadUser() {
        const result = await api('/api/user/current', { redirectOnUnauthorized: false });
        currentUser = isSuccess(result) ? result.data : null;
        return currentUser;
    }

    async function logout() {
        await api('/api/user/logout', { method: 'POST' });
        window.location.href = '/product/list';
    }

    function requireLogin() {
        if (currentUser) return true;
        toast('请先登录', 'error');
        window.setTimeout(() => { window.location.href = '/login'; }, 700);
        return false;
    }

    const links = [
        { href: '/product/list', label: '发现', key: 'market' },
        { href: '/product/mine', label: '我的商品', key: 'mine' },
        { href: '/trade/cart', label: '购物车', key: 'cart', badge: true },
        { href: '/trade/orders', label: '订单', key: 'orders' },
        { href: '/trade/seller/orders', label: '经营', key: 'seller' }
    ];

    async function renderNav(activeKey) {
        const mount = $('#siteHeader');
        if (!mount) return;
        mount.innerHTML = `
            <header class="site-header" data-intro>
                <div class="nav-shell">
                    <a class="brand" href="/product/list" aria-label="Sekai 收藏市场">
                        <span class="brand-mark" aria-hidden="true">S</span>
                        <span class="brand-copy"><span class="brand-name">Sekai</span><span class="brand-note">收藏市场</span></span>
                    </a>
                    <nav class="desktop-nav" id="mainNav" aria-label="主导航">
                        ${links.map(link => `<a class="nav-link ${activeKey === link.key ? 'active' : ''}" href="${link.href}">${link.label}${link.badge ? '<span class="nav-count" data-cart-badge aria-label="购物车数量"></span>' : ''}</a>`).join('')}
                    </nav>
                    <div class="nav-end">
                        ${currentUser ? `<div class="nav-user"><span class="avatar" aria-hidden="true">${esc((currentUser.name || currentUser.userName || 'U').charAt(0).toUpperCase())}</span><span class="user-name">${esc(currentUser.name || currentUser.userName)}</span><button class="logout-button" type="button" title="退出登录" data-logout>退出</button></div>` : '<a class="nav-auth" href="/login">登录 / 注册</a>'}
                        <button class="menu-button" type="button" aria-expanded="false" aria-controls="mainNav" data-menu>菜单</button>
                    </div>
                </div>
            </header>`;
        const menuButton = $('[data-menu]', mount);
        const nav = $('#mainNav', mount);
        menuButton?.addEventListener('click', () => {
            const open = nav.classList.toggle('open');
            menuButton.setAttribute('aria-expanded', String(open));
        });
        $('[data-logout]', mount)?.addEventListener('click', logout);
        await refreshCartBadge();
    }

    async function refreshCartBadge() {
        const badge = $('[data-cart-badge]');
        if (!badge || !currentUser) return;
        const result = await api('/trade/cart/list/api');
        if (!isSuccess(result)) return;
        const count = (result.data || []).reduce((total, item) => total + (Number(item.quantity) || 0), 0);
        const nextLabel = count > 0 ? ` ${count > 99 ? '99+' : count}` : '';
        const changed = badge.textContent !== nextLabel;
        badge.textContent = nextLabel;
        badge.hidden = count === 0;
        if (changed && count > 0 && !prefersReducedMotion() && Element.prototype.animate) {
            badge.animate([
                { opacity: .5, transform: 'scale(.72)' },
                { opacity: 1, transform: 'scale(1)' }
            ], { duration: 360, easing: 'cubic-bezier(.16,1,.3,1)' });
        }
    }

    function prefersReducedMotion() {
        return window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    }

    function observeReveals(root = document) {
        const elements = [];
        if (root.nodeType === 1 && root.matches('.reveal')) elements.push(root);
        elements.push(...$$('.reveal:not([data-reveal-observed])', root));
        if (!elements.length) return;
        if (prefersReducedMotion() || !('IntersectionObserver' in window)) {
            elements.forEach(element => element.classList.add('is-visible'));
            return;
        }
        if (!revealObserver) revealObserver = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('is-visible');
                    entry.target.dataset.revealObserved = 'true';
                    revealObserver.unobserve(entry.target);
                }
            });
        }, { threshold: .12 });
        elements.forEach(element => {
            element.dataset.revealObserved = 'true';
            revealObserver.observe(element);
        });
    }

    function playIntro(root = document, options = {}) {
        const elements = [];
        if (root.nodeType === 1 && root.matches('[data-intro]')) elements.push(root);
        elements.push(...$$('[data-intro]:not([data-intro-played])', root));
        if (!elements.length) return;
        if (prefersReducedMotion() || !Element.prototype.animate) {
            elements.forEach(element => { element.dataset.introPlayed = 'true'; });
            return;
        }
        const duration = options.duration || 720;
        const baseDelay = options.baseDelay || 0;
        const stagger = options.stagger == null ? 70 : options.stagger;
        elements.forEach((element, index) => {
            element.dataset.introPlayed = 'true';
            element.animate([
                { opacity: 0, transform: 'translateY(18px)' },
                { opacity: 1, transform: 'translateY(0)' }
            ], { duration, delay: baseDelay + index * stagger, easing: 'cubic-bezier(.16,1,.3,1)', fill: 'both' });
        });
    }

    function replaceHTMLWithMotion(element, html, options = {}) {
        if (!element) return;
        const token = (element._motionSwapToken || 0) + 1;
        element._motionSwapToken = token;
        const commit = () => {
            if (element._motionSwapToken !== token) return;
            element.innerHTML = html;
            playIntro(element, { duration: options.duration || 560, stagger: options.stagger == null ? 45 : options.stagger });
        };
        const current = Array.from(element.children);
        if (!current.length || prefersReducedMotion() || !Element.prototype.animate) {
            commit();
            return;
        }
        const exits = current.map(child => child.animate([
            { opacity: 1, transform: 'translateY(0)' },
            { opacity: 0, transform: 'translateY(10px)' }
        ], { duration: 140, easing: 'ease-in', fill: 'forwards' }));
        Promise.all(exits.map(animation => animation.finished.catch(() => undefined))).then(commit);
    }

    function bindMotionControls() {
        if (rippleListenerBound) return;
        rippleListenerBound = true;
        document.addEventListener('pointerdown', event => {
            const target = event.target.closest('.button, .text-button, .category-button, .view-toggle button, .page-button, .status-tab');
            if (!target || target.disabled || prefersReducedMotion() || !Element.prototype.animate) return;
            const rect = target.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height) * 1.35;
            const ripple = document.createElement('span');
            ripple.className = 'button-ripple';
            ripple.style.width = `${size}px`;
            ripple.style.height = `${size}px`;
            ripple.style.left = `${event.clientX - rect.left - size / 2}px`;
            ripple.style.top = `${event.clientY - rect.top - size / 2}px`;
            target.appendChild(ripple);
            const animation = ripple.animate([
                { opacity: .34, transform: 'scale(0)' },
                { opacity: 0, transform: 'scale(1)' }
            ], { duration: 460, easing: 'ease-out' });
            animation.finished.then(() => ripple.remove()).catch(() => ripple.remove());
        });
    }

    async function init(activeKey) {
        await loadUser();
        await renderNav(activeKey);
        playIntro();
        observeReveals();
        bindMotionControls();
    }

    return { $, $$, esc, parseImages, firstImage, fmtMoney, dateLabel, isSuccess, toast, api, loadUser, logout, requireLogin, refreshCartBadge, prefersReducedMotion, observeReveals, playIntro, replaceHTMLWithMotion, bindMotionControls, init, get currentUser() { return currentUser; } };
})();
