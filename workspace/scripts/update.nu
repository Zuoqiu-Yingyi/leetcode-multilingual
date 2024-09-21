def update [path: string] {
    cd $path
    print $"\n(ansi green)pnpm-update (pwd)(ansi reset)"
    ^pnpm update
}

update .

update ./tools/eslint

update ./packages/templates

update ./solutions/ecmascript
