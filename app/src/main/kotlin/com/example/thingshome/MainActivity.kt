package com.example.thingshome

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager
import java.io.IOException

private val TAG = MainActivity::class.java.simpleName

class MainActivity : Activity(), GpioCallback {
    private lateinit var buttonA: Button
    private lateinit var buttonB: Button
    private lateinit var buttonC: Button
    private lateinit var ledA : Gpio
    private lateinit var ledB : Gpio
    private lateinit var ledC : Gpio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButton()
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

            buttonA = RainbowHat.openButtonA()
            buttonB = RainbowHat.openButtonB()
            buttonC = RainbowHat.openButtonC()
            ledA = RainbowHat.openLedRed()
            ledB = RainbowHat.openLedGreen()
            ledC = RainbowHat.openLedBlue()
        } catch (e: IOException) {
            Log.w(TAG, "Error opening GPIO", e)
        }
    }

}
