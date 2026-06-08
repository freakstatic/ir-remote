package com.ricardomaltez.irremote.ir.protocol

/**
 * NEC IR protocol encoder.
 *
 * Android transmit() expects an IntArray alternating mark/space durations in
 * microseconds, starting and ending with a mark.
 */
object NecProtocol : IrProtocol {
    private const val HDR_MARK = 9000
    private const val HDR_SPACE = 4500
    private const val BIT_MARK = 562
    private const val ONE_SPACE = 1687
    private const val ZERO_SPACE = 562
    private const val TRAILING_MARK = 562

    override val carrierFrequencyHz: Int = 38_000

    override fun toPattern(code: String): IntArray {
        require(code.matches(Regex("^[0-9A-Fa-f]{8}$"))) {
            "NEC code must be exactly 8 hexadecimal characters"
        }

        val value = code.toLong(16)
        val pattern = IntArray(67)
        var index = 0

        pattern[index++] = HDR_MARK
        pattern[index++] = HDR_SPACE

        for (bit in 31 downTo 0) {
            pattern[index++] = BIT_MARK
            pattern[index++] = if ((value shr bit) and 1L == 1L) ONE_SPACE else ZERO_SPACE
        }

        pattern[index] = TRAILING_MARK
        return pattern
    }
}
