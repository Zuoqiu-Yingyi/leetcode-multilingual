package s2.s1.s0.s4

import utils.T
import kotlin.test.Test
import s2.s1.s0.s4.s_2104_00.Solution as s_2104_00
import s2.s1.s0.s4.s_2104_01.Solution as s_2104_01

class s_2104_test {
    @Test
    fun test() {
        val t = T(
            this::class,
            s_2104_00::class,
            s_2104_01::class,
        )
        t.run()
    }
}
