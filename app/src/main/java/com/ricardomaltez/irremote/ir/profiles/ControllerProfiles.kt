package com.ricardomaltez.irremote.ir.profiles

import com.ricardomaltez.irremote.ir.model.IrControllerProfile

/** Registry for all bundled controller profiles. */
object ControllerProfiles {
    val default: IrControllerProfile = Z607SpeakerProfile

    val all: List<IrControllerProfile> = listOf(
        Z607SpeakerProfile,
    )
}
