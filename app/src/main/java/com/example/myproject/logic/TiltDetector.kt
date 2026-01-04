package com.example.myproject.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs

class TiltDetector(context: Context, private val tiltCallback: TiltCallback?) {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var sensorEventListener: SensorEventListener? = null

    private var timestamp: Long = 0L

    init {
        initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                calculateTilt(x, y)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // pass
            }
        }
    }

    private fun calculateTilt(x: Float, y: Float) {
        // Debounce mechanism: restricts updates to every 500ms to prevent erratic movement
        if (System.currentTimeMillis() - timestamp > 500) {
            timestamp = System.currentTimeMillis()
            if (abs(x) >= 3.0) {
                tiltCallback?.tiltX(x)
            }
            if (abs(y) >= 3.0) {
                tiltCallback?.tiltY(y)
            }
        }
    }

    fun start() {
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun stop() {
        sensorManager.unregisterListener(
            sensorEventListener,
            sensor
        )
    }

    interface TiltCallback {
        fun tiltX(x: Float)
        fun tiltY(y: Float)
    }
}