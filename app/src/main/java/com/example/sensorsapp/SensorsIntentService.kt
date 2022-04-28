package com.example.sensorsapp

import android.app.IntentService
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

// TODO: Rename actions, choose action names that describe tasks that this
// IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
private const val ACTION_FOO = "com.example.sensorsapp.action.FOO"
private const val ACTION_BAZ = "com.example.sensorsapp.action.BAZ"

// TODO: Rename parameters
private const val EXTRA_PARAM1 = "com.example.sensorsapp.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.example.sensorsapp.extra.PARAM2"

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.

 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.

 */
class SensorsIntentService : IntentService("SensorsIntentService") , SensorEventListener {

    private lateinit var light_sensor: SensorManager

    private lateinit var acc_sensor: SensorManager

    private var coordinates = floatArrayOf(0.0F, 0.0F)

    private var light = 0.0F

    override fun onCreate() {
        setUpSensor()
        super.onCreate()
    }

    override fun onHandleIntent(p0: Intent?) {

    }

    private fun sendDataToActivity() {
        val sendLevel = Intent()
        sendLevel.action = "GET_SENSORS_DATA"
        sendLevel.putExtra("LIGHT_DATA", light)
        sendLevel.putExtra("ACCEL_DATA", coordinates)
        sendBroadcast(sendLevel)
    }

    private fun setUpSensor() {
        acc_sensor = getSystemService(SENSOR_SERVICE) as SensorManager
        acc_sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            acc_sensor.registerListener(this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }

        light_sensor = getSystemService(SENSOR_SERVICE) as SensorManager
        light_sensor.getDefaultSensor(Sensor.TYPE_LIGHT)?.also {
            acc_sensor.registerListener(this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            val sides = event.values[0]
            val updown = event.values[1]
            coordinates = floatArrayOf(sides,updown)
            sendDataToActivity()
        }
        if(event?.sensor?.type == Sensor.TYPE_LIGHT){
            light = event.values[0]
            sendDataToActivity()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }
}