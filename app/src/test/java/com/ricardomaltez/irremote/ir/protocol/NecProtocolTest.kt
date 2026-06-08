package com.ricardomaltez.irremote.ir.protocol

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class NecProtocolTest {
    @Test
    fun `carrier frequency is 38 kHz`() {
        assertEquals(38_000, NecProtocol.carrierFrequencyHz)
    }

    @Test
    fun `encodes 32 bit NEC command into 67 duration pattern`() {
        val pattern = NecProtocol.toPattern("80000001")

        assertEquals(67, pattern.size)
        assertEquals(9_000, pattern[0])
        assertEquals(4_500, pattern[1])
        assertEquals(562, pattern.last())

        // First payload bit is 1 for 0x80000001.
        assertArrayEquals(intArrayOf(562, 1_687), pattern.sliceArray(2..3))

        // Second payload bit is 0.
        assertArrayEquals(intArrayOf(562, 562), pattern.sliceArray(4..5))

        // Last payload bit is 1.
        assertArrayEquals(intArrayOf(562, 1_687), pattern.sliceArray(64..65))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects invalid hex command`() {
        NecProtocol.toPattern("not-a-code")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects short hex command`() {
        NecProtocol.toPattern("FFFF")
    }
}
