# Workspace

## Languages

| Language                                                                                                                                                                             | Lint / Format                                                                                                                                                         | Test                                                                                                                                                                      |
| ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [![JavaScript](https://img.shields.io/badge/%E2%80%8D-JavaScript-F7DF1E?style=flat-square&logo=javascript&logoColor=white)](https://developer.mozilla.org/en-US/docs/Web/javascript) | [![ESLint](https://img.shields.io/badge/%E2%80%8D-ESLint-4B32C3?style=flat-square&logo=eslint&logoColor=white)](https://eslint.org/)                                  | [![Bun test](https://img.shields.io/badge/%E2%80%8D-Bun_test-000000?style=flat-square&logo=bun&logoColor=white)](https://bun.sh/docs/cli/test)                            |
| [![TypeScript](https://img.shields.io/badge/%E2%80%8D-TypeScript-3178C6?style=flat-square&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)                         | [![ESLint](https://img.shields.io/badge/%E2%80%8D-ESLint-4B32C3?style=flat-square&logo=eslint&logoColor=white)](https://eslint.org/)                                  | [![Bun test](https://img.shields.io/badge/%E2%80%8D-Bun_test-000000?style=flat-square&logo=bun&logoColor=white)](https://bun.sh/docs/cli/test)                            |
| [![Go](https://img.shields.io/badge/%E2%80%8B-Go-00ADD8?style=flat-square&logo=go&logoColor=white)](https://go.dev/)                                                                 | [![Go gofmt](https://img.shields.io/badge/%E2%80%8B-Go_gofmt-00ADD8?style=flat-square&logo=go&logoColor=white)](https://pkg.go.dev/cmd/gofmt)                         | [![Go testing](https://img.shields.io/badge/%E2%80%8B-Go_testing-00ADD8?style=flat-square&logo=go&logoColor=white)](https://pkg.go.dev/testing)                           |
| [![Java](https://img.shields.io/badge/J_%E2%80%8B-Java-E11F21?style=flat-square&logoColor=white)](https://www.java.com/)                                                             | [![ClangFormat](https://img.shields.io/badge/%E2%80%8D-ClangFormat-262D3A?style=flat-square&logo=llvm&logoColor=white)](https://clang.llvm.org/docs/ClangFormat.html) | [![JUnit5](https://img.shields.io/badge/%E2%80%8B-JUnit5-25A162?style=flat-square&logo=junit5&logoColor=white)](https://junit.org/junit5/)                                |
| [![Kotlin](https://img.shields.io/badge/%E2%80%8B-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org/)                                             | [![Ktlint](https://img.shields.io/badge/K_%E2%80%8D-Ktlint-E92063?style=flat-square&logoColor=white)](https://pinterest.github.io/ktlint/)                            | [![kotlin.test](https://img.shields.io/badge/%E2%80%8B-kotlin.test-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org/api/latest/kotlin.test/) |
| [![Python](https://img.shields.io/badge/%E2%80%8D-Python-3776AB?style=flat-square&logo=python&logoColor=white)](https://www.python.org/)                                             | [![Ruff](https://img.shields.io/badge/%E2%80%8D-Ruff-D7FF64?style=flat-square&logo=ruff&logoColor=white)](https://docs.astral.sh/ruff/)                               | [![Pytest](https://img.shields.io/badge/%E2%80%8D-Pytest-0A9EDC?style=flat-square&logo=pytest&logoColor=white)](https://docs.pytest.org/en/stable/)                       |
| [![Rust](https://img.shields.io/badge/%E2%80%8D-Rust-000000?style=flat-square&logo=rust&logoColor=white)](https://www.rust-lang.org/)                                                | [![Rustfmt](https://img.shields.io/badge/%E2%80%8D-Rustfmt-000000?style=flat-square&logo=rust&logoColor=white)](https://rust-lang.github.io/rustfmt/)                 | [![Cargo test](https://img.shields.io/badge/%E2%80%8D-Cargo_test-000000?style=flat-square&logo=rust&logoColor=white)](https://doc.rust-lang.org/cargo/guide/tests.html)   |

## Solve

```shell
$ pnpm solve
```

## Test

```shell
# Run all testing tasks
$ pnpm test

# Run testing task for JavaScript & TypeScript
$ pnpm test:es

# Run testing task for Go
$ pnpm test:go

# Run testing task for Java
$ pnpm test:java

# Run testing task for Kotlin
$ pnpm test:kt

# Run testing task for Python
$ pnpm test:py

# Run testing task for Rust
$ pnpm test:rs
```

## Format

```shell
# Run all formatting tasks
$ pnpm format

# Run formatting task for JavaScript & TypeScript
$ pnpm format:es

# Run formatting task for Go
$ pnpm format:go

# Run formatting task for Java
$ pnpm format:java

# Run formatting task for Kotlin
$ pnpm format:kt

# Run formatting task for Python
$ pnpm format:py

# Run formatting task for Rust
$ pnpm format:rs
```
