export def clang-format [paths: list<string>] {
    # echo $env.PWD $paths
    ^clang-format "-i" "--style=file:./.clang-format" ...$paths
}
