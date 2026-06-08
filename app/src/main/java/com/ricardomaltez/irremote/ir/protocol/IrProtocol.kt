package com.ricardomaltez.irremote.ir.protocol

/** Converts encoded IR command data into Android ConsumerIrManager transmit data. */
interface IrProtocol {
    val carrierFrequencyHz: Int
    fun toPattern(code: String): IntArray
}
