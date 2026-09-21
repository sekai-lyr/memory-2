(() => {
    const root = document.querySelector('[data-showcase]');
    if (!root) return;

    const stage = root.querySelector('[data-showcase-stage]');
    const layers = Array.from(root.querySelectorAll('[data-showcase-layer]'));
    const title = root.querySelector('[data-showcase-title]');
    const counter = root.querySelector('[data-showcase-counter]');
    const toggle = root.querySelector('[data-showcase-toggle]');
    const pageCount = layers.length;
    const REST = 900;
    const EXPAND = 950;
    const HOLD = 1100;
    const STEP = 680;
    const EXIT = 860;
    const EXIT_START = REST + EXPAND + HOLD;
    const DURATION = EXIT_START + (pageCount - 1) * STEP + EXIT + 450;

    let width = 560;
    let elapsed = 0;
    let frameId = 0;
    let lastFrame = 0;
    let isPlaying = true;
    let pageInView = true;
    let pageHidden = document.visibilityState !== 'visible';
    let disposed = false;
    let reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    const clamp = value => Math.max(0, Math.min(1, value));
    const easeOutCubic = value => 1 - Math.pow(1 - clamp(value), 3);

    function dissolveClip(progress) {
        const points = ['100% 0%', '100% 100%'];
        for (let row = 40; row > 0; row -= 1) {
            const jitter = ((row * 13) % 7 - 3) * 0.5 * Math.sin(progress * Math.PI);
            const edge = Math.max(0, Math.min(100, progress * 100 + jitter));
            points.push(`${edge}% ${row * 2.5}%`, `${edge}% ${(row - 1) * 2.5}%`);
        }
        return `polygon(${points.join(',')})`;
    }

    function setMetrics() {
        width = stage.clientWidth || width;
        root.style.setProperty('--showcase-width', `${width}px`);
        render(reducedMotion ? REST + EXPAND : elapsed);
    }

    function createFragments(layer, layerIndex) {
        const container = layer.querySelector('.showcase-fragments');
        if (!container || container.children.length) return;
        for (let index = 0; index < 30; index += 1) {
            const fragment = document.createElement('i');
            fragment.className = 'showcase-fragment';
            fragment.dataset.seed = String((layerIndex + 3) * 47 + index * 19);
            fragment.style.top = `${8 + ((index * 29 + layerIndex * 17) % 82)}%`;
            fragment.style.height = `${3 + ((index * 7 + layerIndex) % 5)}px`;
            fragment.style.width = `${4 + ((index * 11 + layerIndex * 3) % 10)}px`;
            container.appendChild(fragment);
        }
    }

    function hideFragments(fragments) {
        fragments.forEach(fragment => {
            fragment.style.visibility = 'hidden';
            fragment.style.opacity = '0';
        });
    }

    function renderFragments(fragments, progress) {
        const edge = progress * 100;
        fragments.forEach((fragment, index) => {
            const seed = Number(fragment.dataset.seed) || index;
            const offset = (seed % 17) - 8;
            const fragmentEdge = Math.max(0, Math.min(98, edge + offset));
            const trail = progress * width * (0.035 + (seed % 5) * 0.008);
            fragment.style.left = `${fragmentEdge}%`;
            fragment.style.transform = `translate3d(${trail}px, 0, 0) rotate(${(seed % 7) - 3}deg)`;
            fragment.style.backgroundColor = seed % 4 === 0 ? 'var(--accent)' : 'var(--ink)';
            fragment.style.visibility = progress > 0.04 && progress < 0.99 ? 'visible' : 'hidden';
            fragment.style.opacity = progress > 0.04 && progress < 0.99 ? String(Math.min(0.9, progress * 2.8)) : '0';
        });
    }

    function getSpread(time) {
        if (reducedMotion) return 0.68;
        if (time < REST) return 0;
        if (time < REST + EXPAND) return easeOutCubic((time - REST) / EXPAND);
        return 1;
    }

    function getActiveIndex(time) {
        if (reducedMotion) return 0;
        let activeIndex = 0;
        for (let index = 0; index < pageCount - 1; index += 1) {
            if (time >= EXIT_START + index * STEP + EXIT * 0.34) activeIndex = index + 1;
        }
        return activeIndex;
    }

    function updateStatus(time) {
        const index = getActiveIndex(time);
        const panel = layers[index];
        if (title && panel && title.textContent !== panel.dataset.title) title.textContent = panel.dataset.title;
        if (counter) counter.textContent = `${String(index + 1).padStart(2, '0')} / ${String(pageCount).padStart(2, '0')}`;
    }

    function render(time) {
        const spread = getSpread(time);
        layerData.forEach(({ layer, panel, fragments }, index) => {
            const depth = pageCount - index;
            const x = width * 0.032 * index * spread;
            const y = width * 0.014 * index * spread;
            const z = depth * 0.2 + width * 0.048 * depth * spread;
            layer.style.transform = `translate3d(${x.toFixed(2)}px, ${y.toFixed(2)}px, ${z.toFixed(2)}px)`;
            layer.style.zIndex = String(pageCount - index);

            const exitProgress = reducedMotion ? 0 : clamp((time - EXIT_START - index * STEP) / EXIT);
            if (exitProgress > 0) {
                panel.style.clipPath = dissolveClip(exitProgress);
                layer.style.visibility = exitProgress >= 1 ? 'hidden' : 'visible';
                renderFragments(fragments, exitProgress);
            } else {
                panel.style.clipPath = 'none';
                layer.style.visibility = 'visible';
                hideFragments(fragments);
            }
        });
        updateStatus(time);
    }

    function canRun() {
        return !disposed && isPlaying && !pageHidden && pageInView && !reducedMotion;
    }

    function stopClock() {
        if (frameId) window.cancelAnimationFrame(frameId);
        frameId = 0;
    }

    function tick(timestamp) {
        frameId = 0;
        if (!canRun()) return;
        if (!lastFrame) lastFrame = timestamp;
        elapsed = (elapsed + Math.min(64, Math.max(0, timestamp - lastFrame))) % DURATION;
        lastFrame = timestamp;
        render(elapsed);
        frameId = window.requestAnimationFrame(tick);
    }

    function syncClock() {
        if (canRun()) {
            if (!frameId) {
                lastFrame = performance.now();
                frameId = window.requestAnimationFrame(tick);
            }
        } else {
            stopClock();
            lastFrame = 0;
        }
    }

    function updateToggle() {
        if (!toggle) return;
        const paused = !isPlaying;
        toggle.textContent = paused ? '▶' : 'Ⅱ';
        toggle.setAttribute('aria-label', paused ? '播放展示动画' : '暂停展示动画');
        toggle.setAttribute('aria-pressed', String(paused));
    }

    function setReducedMotion(nextValue) {
        reducedMotion = nextValue;
        root.classList.toggle('is-static', reducedMotion);
        if (toggle) toggle.hidden = reducedMotion;
        render(reducedMotion ? REST + EXPAND : elapsed);
        syncClock();
    }

    const layerData = layers.map((layer, index) => {
        createFragments(layer, index);
        return {
            layer,
            panel: layer.querySelector('.showcase-panel'),
            fragments: Array.from(layer.querySelectorAll('.showcase-fragment'))
        };
    });
    setMetrics();
    render(0);
    updateToggle();

    if (toggle) {
        toggle.addEventListener('click', () => {
            isPlaying = !isPlaying;
            updateToggle();
            syncClock();
        });
    }

    if ('ResizeObserver' in window) {
        const resizeObserver = new ResizeObserver(() => setMetrics());
        resizeObserver.observe(stage);
    } else {
        window.addEventListener('resize', setMetrics, { passive: true });
    }

    if ('IntersectionObserver' in window) {
        const intersectionObserver = new IntersectionObserver(entries => {
            pageInView = Boolean(entries[0] && entries[0].isIntersecting);
            syncClock();
        }, { threshold: 0.01 });
        intersectionObserver.observe(root);
    }

    document.addEventListener('visibilitychange', () => {
        pageHidden = document.visibilityState !== 'visible';
        syncClock();
    });

    window.addEventListener('pagehide', () => {
        disposed = true;
        stopClock();
    }, { once: true });

    const motionQuery = window.matchMedia('(prefers-reduced-motion: reduce)');
    if (motionQuery.addEventListener) motionQuery.addEventListener('change', event => setReducedMotion(event.matches));
    else motionQuery.addListener(event => setReducedMotion(event.matches));

    syncClock();
})();
