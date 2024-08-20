/**
 * @type {import("lint-staged").Config}
 * @see {@link https://www.npmjs.com/package/lint-staged | lint-staged}
 */
const config = {
    /* incremental formatting */
    // REF: https://github.com/sudo-suhas/lint-staged-django-react-demo
    "*": "cspell lint --no-must-find-files",
    "*.{js,ts,md,json,toml,yaml}": "eslint --fix",
    "*.go": "gofmt -w",
    "*.java": "clang-format -i --style=file:./.clang-format",
    // "*.java": (_) => "./solutions/java/gradlew -p ./solutions/java spotlessJavaApply",
    "*.gradle": (_) => "./solutions/java/gradlew -p ./solutions/java spotlessGroovyGradleApply",
    // "*.{kt,kts}": "ktlint -F",
    "*.{kt,kts}": (_) => "./solutions/kotlin/gradlew -p ./solutions/kotlin spotlessKotlinApply",
    "*.py": "rye run --pyproject ./solutions/python/pyproject.toml ruff format",
    "*.rs": "rustfmt --config-path ./solutions/rust/rustfmt.toml",

    /* full formatting */
    "eslint.config.js": (_) => "pnpm run format:eslint",
    ".clang-format": (_) => "pnpm run format:java",
    "build.gradle.kts": (_) => "pnpm run format:kt",
    "ruff.toml": (_) => "pnpm run format:py",
    "rustfmt.toml": (_) => "pnpm run format:rs",
};

export default config;
