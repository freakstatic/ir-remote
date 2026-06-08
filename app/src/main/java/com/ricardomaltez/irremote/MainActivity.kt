package com.ricardomaltez.irremote

import android.content.Context
import android.hardware.ConsumerIrManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.ricardomaltez.irremote.ir.model.CommandIds
import com.ricardomaltez.irremote.ir.model.IrCommand
import com.ricardomaltez.irremote.ir.model.IrControllerProfile
import com.ricardomaltez.irremote.ir.profiles.ControllerProfiles

class MainActivity : AppCompatActivity() {

    private var irManager: ConsumerIrManager? = null
    private lateinit var statusText: TextView

    private val activeProfile: IrControllerProfile = ControllerProfiles.default
    private val statusHandler = Handler(Looper.getMainLooper())
    private val resetStatusRunnable = Runnable { statusText.text = getString(R.string.status_ready) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)
        applySystemBarInsets()

        irManager = getSystemService(Context.CONSUMER_IR_SERVICE) as? ConsumerIrManager
        statusText = findViewById(R.id.statusText)

        if (irManager == null || irManager?.hasIrEmitter() != true) {
            statusText.text = getString(R.string.status_no_ir)
            statusText.setTextColor(getResources().getColor(R.color.status_error, null))
            statusText.setBackground(getResources().getDrawable(R.drawable.status_pill_error, null))
            disableControllerButtons()
            return
        }
        findViewById<TextView>(R.id.controllerNameText).text = activeProfile.displayName



        statusText.text = getString(R.string.status_ready)
        bindControllerButtons()
    }

    private fun applySystemBarInsets() {
        val rootView = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(view.paddingLeft, bars.top, view.paddingRight, bars.bottom)
            insets
        }
    }

    private fun bindControllerButtons() {
        mapOf(
            R.id.btnPower to CommandIds.POWER,
            R.id.btnMute to CommandIds.MUTE,
            R.id.btnVolDown to CommandIds.VOLUME_DOWN,
            R.id.btnVolUp to CommandIds.VOLUME_UP,
            R.id.btnBluetooth to CommandIds.INPUT_BLUETOOTH,
            R.id.btnFmRadio to CommandIds.INPUT_FM_RADIO,
            R.id.btnAux to CommandIds.INPUT_AUX,
            R.id.btnRca to CommandIds.INPUT_RCA,
            R.id.btnSurround to CommandIds.SURROUND_MODE,
            R.id.btnChLevel to CommandIds.CHANNEL_LEVEL,
            R.id.btnSdUsb to CommandIds.INPUT_SD_USB,
            R.id.btnSkipPrev to CommandIds.SKIP_PREVIOUS,
            R.id.btnPlayPause to CommandIds.PLAY_PAUSE,
            R.id.btnSkipNext to CommandIds.SKIP_NEXT,
        ).forEach { (buttonId, commandId) ->
            findViewById<MaterialButton>(buttonId).setOnClickListener {
                activeProfile.command(commandId)?.let(::transmitIr)
            }
        }
    }

    private fun disableControllerButtons() {
        CONTROLLER_BUTTON_IDS.forEach { buttonId ->
            findViewById<MaterialButton>(buttonId).isEnabled = false
        }
    }

    private fun transmitIr(command: IrCommand) {
        try {
            val pattern = activeProfile.protocol.toPattern(command.code)
            irManager?.transmit(activeProfile.protocol.carrierFrequencyHz, pattern)

            currentFocus?.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            showTemporaryStatus(command.label)
        } catch (error: IllegalArgumentException) {
            showSendFailure(error)
        } catch (error: RuntimeException) {
            showSendFailure(error)
        }
    }

    private fun showSendFailure(error: Exception) {
        Toast.makeText(
            this,
            getString(R.string.error_ir_send_failed, error.localizedMessage ?: error.javaClass.simpleName),
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun showTemporaryStatus(label: String) {
        statusHandler.removeCallbacks(resetStatusRunnable)
        statusText.text = label
        statusHandler.postDelayed(resetStatusRunnable, STATUS_RESET_DELAY_MS)
    }

    override fun onDestroy() {
        statusHandler.removeCallbacks(resetStatusRunnable)
        super.onDestroy()
    }

    companion object {
        private const val STATUS_RESET_DELAY_MS = 2_000L

        private val CONTROLLER_BUTTON_IDS = listOf(
            R.id.btnPower,
            R.id.btnMute,
            R.id.btnVolDown,
            R.id.btnVolUp,
            R.id.btnBluetooth,
            R.id.btnFmRadio,
            R.id.btnAux,
            R.id.btnRca,
            R.id.btnSurround,
            R.id.btnChLevel,
            R.id.btnSdUsb,
            R.id.btnSkipPrev,
            R.id.btnPlayPause,
            R.id.btnSkipNext,
        )
    }
}
