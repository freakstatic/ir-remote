package com.ricardomaltez.irremote.ir.profiles

import com.ricardomaltez.irremote.ir.model.CommandIds
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class Z607SpeakerProfileTest {
    private val requiredCommandIds = setOf(
        CommandIds.POWER,
        CommandIds.MUTE,
        CommandIds.VOLUME_UP,
        CommandIds.VOLUME_DOWN,
        CommandIds.INPUT_BLUETOOTH,
        CommandIds.INPUT_FM_RADIO,
        CommandIds.INPUT_AUX,
        CommandIds.INPUT_RCA,
        CommandIds.SURROUND_MODE,
        CommandIds.CHANNEL_LEVEL,
        CommandIds.INPUT_SD_USB,
        CommandIds.PLAY_PAUSE,
        CommandIds.SKIP_PREVIOUS,
        CommandIds.SKIP_NEXT,
    )

    @Test
    fun `profile exposes all current UI commands`() {
        assertEquals(requiredCommandIds, Z607SpeakerProfile.commands.keys)
    }

    @Test
    fun `all command codes are valid NEC payloads`() {
        Z607SpeakerProfile.commands.values.forEach { command ->
            assertTrue("${command.id} has invalid code", command.code.matches(Regex("^[0-9A-F]{8}$")))
            assertEquals(67, Z607SpeakerProfile.protocol.toPattern(command.code).size)
        }
    }

    @Test
    fun `controller registry has unique ids and a usable default`() {
        val ids = ControllerProfiles.all.map { it.id }

        assertEquals(ids.distinct(), ids)
        assertNotNull(ControllerProfiles.default.command(CommandIds.POWER))
    }
}
