/**
 * @type {import("lint-staged").Config}
 * @see {@link https://www.npmjs.com/package/lint-staged | lint-staged}
 */
const config = {
    // REF: https://github.com/sudo-suhas/lint-staged-django-react-demo
    "*": "cspell lint --no-must-find-files",
    "*.{js,ts,md,json,yaml}": "eslint --fix",
    "*.go": "gofmt -w",
    "*.java": "clang-format -i --style=file:./.clang-format",
    // "*.java": (_) => "./solutions/java/gradlew -p ./solutions/java spotlessApply",
    // "*.{kt,kts}": "ktlint -F",
    "*.{kt,kts}": (_) => "./solutions/kotlin/gradlew -p ./solutions/kotlin spotlessApply",
};

export default config;
