import test from "bun:test";

import { E } from "@repo/common";

import { t } from "@/utils/test";

t(
    test,
    import.meta.dir,
    E.SolutionFormat.class,
);
