export def test-ecmascript [] {
    cd "./solutions/ecmascript"
    print $"\n(ansi blue)test-ecmascript(ansi reset) (pwd)"
    ^pnpm run test
}

export def test-go [] {
    cd "./solutions/go"
    print $"\n(ansi blue)test-go(ansi reset) (pwd)"
    ^go test "./..."
}

export def test-java [] {
    cd "./solutions/java"
    print $"\n(ansi blue)test-java(ansi reset) (pwd)"
    ^gradlew test
}

export def test-kotlin [] {
    cd "./solutions/kotlin"
    print $"\n(ansi blue)test-kotlin(ansi reset) (pwd)"
    ^gradlew test
}

export def test-python [] {
    cd "./solutions/python"
    print $"\n(ansi blue)test-python(ansi reset) (pwd)"
    ^rye test
}

export def test-rust [] {
    cd "./solutions/rust"
    print $"\n(ansi blue)test-rust(ansi reset) (pwd)"
    ^cargo test
}
