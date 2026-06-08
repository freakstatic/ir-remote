package com.ricardomaltez.irremote.ir.model

import com.ricardomaltez.irremote.ir.protocol.IrProtocol

/**
 * Describes one supported physical remote/controller.
 *
 * Adding another controller should normally mean adding a new profile object,
 * not changing MainActivity.
 */
interface IrControllerProfile {
    val id: String
    val displayName: String
    val protocol: IrProtocol
    val commands: Map<String, IrCommand>

    fun command(id: String): IrCommand? = commands[id]
}
