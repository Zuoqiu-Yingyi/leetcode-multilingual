import test from "bun:test";

import { t } from "@/utils/test";

await t(
    test,
    import.meta.dir,
);
