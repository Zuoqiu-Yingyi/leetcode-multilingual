/**
 * @type {import("lint-staged").Config}
 * @see {@link https://www.npmjs.com/package/lint-staged | lint-staged}
 */
const config = {
    // REF: https://github.com/sudo-suhas/lint-staged-django-react-demo
    "*": "cspell",
    "*.{js,ts,md,json,yaml}": "eslint --fix",
};

export default config;
