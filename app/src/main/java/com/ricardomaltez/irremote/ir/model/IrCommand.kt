package com.ricardomaltez.irremote.ir.model

/**
 * A single command exposed by an IR controller profile.
 *
 * @param id Stable internal command id used by UI bindings and tests.
 * @param label Human readable label shown in temporary status text.
 * @param code Protocol-specific encoded command payload.
 */
data class IrCommand(
    val id: String,
    val label: String,
    val code: String,
)
