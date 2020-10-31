package edu.purdue.sensordemo

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.math.sqrt

class SensorActivity : Activity() {

    private var mySensorManager: SensorManager? = null
    private var gravity = FloatArray(3) // Accelerometer sensor readouts

    // Use this variable to count the number of times the device is shaken.
    private var counter = 0
    private var lastAcc : Float = -1.0f

    private var messageTextView: TextView? = null
    private var resetButton: Button? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor)
        mySensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        messageTextView = findViewById<View>(R.id.Message) as TextView
        resetButton = findViewById<View>(R.id.Reset_button) as Button

        resetButton?.setOnClickListener {
            counter = 0
            updateGUI()
        }
    }

    // A single listener for all sensors that are being monitored
    // The same code can also be broken down into separate methods for each sensor
    private val sensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            // If the sensor data is unreliable, return
            if(event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) return

            when(event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    gravity = event.values.clone() // get the accelerometer sensor readout

                    // calculate the acceleration vector
                    // if the device is resting on a flat surface, this value should be equal to
                    // normal gravity, i.e. 9.81 [m/s/s]
                    val acceleration = sqrt((gravity[0] * gravity[0]) + (gravity[1] * gravity[1]) + (gravity[2] * gravity[2]))

                    if (lastAcc > 30 && acceleration < 30) {
                        counter++
                        updateGUI()
                        lastAcc = 0f
                    }

                    lastAcc = acceleration
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    // Register all sensor listeners when the activity is running
    override fun onResume() {
        super.onResume()
        val accelerometer = mySensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mySensorManager?.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    // Unregister all sensors when the activity is paused or terminated
    override fun onPause() {
        mySensorManager?.unregisterListener(sensorEventListener)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sensor, menu)
        return true
    }

    // Update the GUI with changes triggered off the UI thread.
    private fun updateGUI() {
        runOnUiThread {
            messageTextView?.text = counter.toString()
            messageTextView?.invalidate()
        }
    }
}