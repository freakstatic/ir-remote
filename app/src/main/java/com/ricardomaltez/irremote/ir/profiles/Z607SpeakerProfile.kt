package com.ricardomaltez.irremote.ir.profiles

import com.ricardomaltez.irremote.ir.model.CommandIds
import com.ricardomaltez.irremote.ir.model.IrCommand
import com.ricardomaltez.irremote.ir.model.IrControllerProfile
import com.ricardomaltez.irremote.ir.protocol.NecProtocol

/** IR profile for the Z607 speaker system remote. */
object Z607SpeakerProfile : IrControllerProfile {
    override val id: String = "z607_speaker"
    override val displayName: String = "Z607"
    override val protocol = NecProtocol

    override val commands: Map<String, IrCommand> = listOf(
        IrCommand(CommandIds.POWER, "Power", "212EFF00"),
        IrCommand(CommandIds.MUTE, "Mute", "010E9D62"),
        IrCommand(CommandIds.VOLUME_UP, "Volume +", "010EE31C"),
        IrCommand(CommandIds.VOLUME_DOWN, "Volume −", "010E13EC"),
        IrCommand(CommandIds.SURROUND_MODE, "5.1 / 2.1", "010E37C8"),
        IrCommand(CommandIds.INPUT_AUX, "AUX", "010E2DD2"),
        IrCommand(CommandIds.INPUT_BLUETOOTH, "Bluetooth", "010E5DA2"),
        IrCommand(CommandIds.CHANNEL_LEVEL, "CH Level", "010E837C"),
        IrCommand(CommandIds.INPUT_FM_RADIO, "FM Radio", "010EB748"),
        IrCommand(CommandIds.INPUT_RCA, "RCA", "010E9F60"),
        IrCommand(CommandIds.INPUT_SD_USB, "SD / USB", "010E57A8"),
        IrCommand(CommandIds.PLAY_PAUSE, "Play / Pause", "414E15EA"),
        IrCommand(CommandIds.SKIP_NEXT, "Next", "010E758A"),
        IrCommand(CommandIds.SKIP_PREVIOUS, "Previous", "010EF50A"),
    ).associateBy { it.id }
}
