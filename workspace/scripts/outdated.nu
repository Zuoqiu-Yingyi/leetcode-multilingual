def pnpm-outdated [path: string] {
    cd $path
    print $"\n(ansi green)pnpm-outdated(ansi reset) (pwd)"
    ^pnpm outdated
    }

def gradlew-dependency-updates [path: string] {
    cd $path
    print $"\n(ansi green)gradlew-dependency-updates(ansi reset) (pwd)"
    ^gradlew dependencyUpdates
}

pnpm-outdated .

pnpm-outdated ./tools/eslint

pnpm-outdated ./packages/templates

pnpm-outdated ./solutions/ecmascript

gradlew-dependency-updates ./solutions/java

gradlew-dependency-updates ./solutions/kotlin
