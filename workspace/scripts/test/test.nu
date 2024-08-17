export def test-ecmascript [] {
    cd "./solutions/ecmascript"
    ^pnpm run test
}

export def test-go [] {
    cd "./solutions/go"
    ^go test "./..."
}

export def test-java [] {
    cd "./solutions/java"
    ^gradlew test
}

export def test-kotlin [] {
    cd "./solutions/kotlin"
    ^gradlew test
}

export def test-python [] {
    cd "./solutions/python"
    ^rye test
}
