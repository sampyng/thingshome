package com.example.thingshome

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager
import java.io.IOException


private val TAG = MainActivity::class.java.simpleName
private const val DEVICE_RPI = "rpi3"
private val BUTTON_PIN_NAME = if (Build.DEVICE == DEVICE_RPI) "BCM21" else "GPIO6_IO14"

class MainActivity : AppCompatActivity(), GpioCallback {
    // GPIO connection to button input
    private lateinit var buttonGpio: Gpio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyButton()
    }

    override fun onGpioEdge(gpio: Gpio?): Boolean {
        try {
            Log.i(TAG, "GPIO changed, button " + gpio?.value)
        } catch (e: IOException) {
            Log.w(TAG, "Error reading GPIO")
        }

        // Return true to keep callback active.
        return true
    }

    private fun setupButton() {
        try {
            val pioManager = PeripheralManager.getInstance()
            Log.d(TAG, "Available GPIO: " + pioManager.gpioList)

            buttonGpio = pioManager.openGpio(BUTTON_PIN_NAME)
            // Configure as an input, trigger events on every change.
            buttonGpio.setDirection(Gpio.DIRECTION_IN)
            buttonGpio.setEdgeTriggerType(Gpio.EDGE_BOTH)
            // Value is true when the pin is LOW
            buttonGpio.setActiveType(Gpio.ACTIVE_LOW)
            // Register the event callback.
            buttonGpio.registerGpioCallback(this)
        } catch (e: IOException) {
            Log.w(TAG, "Error opening GPIO", e)
        }
    }

    private fun destroyButton() {
        // Close the button
        buttonGpio.unregisterGpioCallback(this)
        try {
            buttonGpio.close()
        } catch (e: IOException) {
            Log.w(TAG, "Error closing GPIO", e)
        }
    }

}
