use clang.nu [clang-format]

export def format-ecmascript [] {
    cd "./solutions/ecmascript"
    print $"\n(ansi green)format-ecmascript(ansi reset) (pwd)"
    ^pnpm run format:eslint
}

export def format-go [] {
    cd "./solutions/go"
    print $"\n(ansi green)format-go(ansi reset) (pwd)"
    ^go fmt -n "./..."
}

export def format-java [] {
    let paths: list<string> = (ls ./solutions/java/lib/src/**/*.java | get name)
    print $"\n(ansi green)format-java(ansi reset)"
    print $paths
    clang-format $paths
    echo $paths
}

export def format-kotlin [] {
    cd "./solutions/kotlin"
    print $"\n(ansi green)format-kotlin(ansi reset) (pwd)"
    ^gradlew spotlessApply
}

export def format-python [] {
    cd "./solutions/python"
    print $"\n(ansi green)format-python(ansi reset) (pwd)"
    ^rye run ruff format
}

export def format-rust [] {
    cd "./solutions/rust"
    print $"\n(ansi green)format-rust(ansi reset) (pwd)"
    ^cargo fmt
}
