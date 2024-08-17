use clang.nu [clang-format]

export def format-ecmascript [] {
    cd "./solutions/ecmascript"
    ^pnpm run format:eslint
}

export def format-go [] {
    cd "./solutions/go"
    ^go fmt -n "./..."
}

export def format-java [] {
    let paths = ls ./solutions/java/lib/src/**/*.java | get name
    clang-format $paths
    echo $paths
}

export def format-kotlin [] {
    cd "./solutions/kotlin"
    ^gradlew spotlessApply
}

export def format-python [] {
    cd "./solutions/python"
    ^rye run ruff format
}
