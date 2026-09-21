const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const test = require('node:test');
const vm = require('node:vm');

const commonJs = fs.readFileSync(
    path.resolve(__dirname, '../../main/resources/static/common.js'),
    'utf8'
);

function createSekai(fetchResult) {
    const scheduled = [];
    const location = { href: null };
    const document = {
        body: { appendChild() {} },
        createElement() {
            return {
                appendChild() {},
                classList: { add() {} },
                remove() {}
            };
        },
        querySelector() { return null; },
        querySelectorAll() { return []; },
        addEventListener() {}
    };
    const window = {
        location,
        matchMedia() { return { matches: true }; },
        setTimeout(callback) {
            scheduled.push(callback);
            return scheduled.length;
        }
    };
    const sandbox = {
        document,
        window,
        fetch: async () => ({ json: async () => fetchResult }),
        Element: function Element() {},
        IntersectionObserver: function IntersectionObserver() {},
        console
    };
    sandbox.Element.prototype.animate = undefined;
    vm.runInNewContext(`${commonJs}\nthis.Sekai = Sekai;`, sandbox);
    return { sekai: sandbox.Sekai, location, scheduled };
}

test('loadUser does not redirect an unauthenticated auth page', async () => {
    const { sekai, location, scheduled } = createSekai({ code: '401', success: false });

    await sekai.loadUser();

    scheduled.forEach(callback => callback());
    assert.equal(location.href, null);
});

test('protected API calls still redirect unauthenticated users', async () => {
    const { sekai, location, scheduled } = createSekai({ code: '401', success: false });

    await sekai.api('/protected/api');

    scheduled.forEach(callback => callback());
    assert.equal(location.href, '/login');
});
