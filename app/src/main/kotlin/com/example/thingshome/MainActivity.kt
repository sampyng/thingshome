package com.example.thingshome

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.contrib.driver.bmx280.Bmx280
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
    private lateinit var sensor : Bmx280

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
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

    private fun setup() {
        try {
            val pioManager = PeripheralManager.getInstance()
            Log.d(TAG, "Available GPIO: " + pioManager.gpioList)

            buttonA = RainbowHat.openButtonA().apply {
                setDebounceDelay(1000)
                setOnButtonEventListener { _, pressed ->
                    if (pressed) {
                        Log.d(TAG, "temperature:" + sensor.readTemperature())
                    }
                }
            }
            buttonB = RainbowHat.openButtonB()
            buttonC = RainbowHat.openButtonC()
            ledA = RainbowHat.openLedRed()
            ledB = RainbowHat.openLedGreen()
            ledC = RainbowHat.openLedBlue()

            sensor = RainbowHat.openSensor()
            sensor.temperatureOversampling = Bmx280.OVERSAMPLING_1X
        } catch (e: IOException) {
            Log.w(TAG, "Error opening GPIO", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            buttonA.close()
            buttonB.close()
            buttonC.close()
            ledA.close()
            ledB.close()
            ledC.close()

            sensor.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing GPIO", e)
        }
    }
}
