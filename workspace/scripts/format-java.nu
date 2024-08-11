
use format.nu [clang-format]

let paths = ls ./solutions/java/lib/src/**/*.java | get name
# echo $paths
clang-format $paths
