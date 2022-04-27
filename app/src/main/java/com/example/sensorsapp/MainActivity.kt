package com.example.sensorsapp

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var light_sensor: SensorManager

    private lateinit var acc_sensor: SensorManager

    private lateinit var square: TextView

    private lateinit var ratingBar: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        square = findViewById(R.id.square)
        ratingBar = findViewById(R.id.ratingBar)

        setUpSensor()
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

            val color = if(updown.toInt() == 0 && sides.toInt() == 0) Color.GREEN else Color.GRAY
            square.setBackgroundColor(color)
        }
        if(event?.sensor?.type == Sensor.TYPE_LIGHT){
            val light_value = event.values[0]
            val maxvalue = light_sensor.getDefaultSensor(Sensor.TYPE_LIGHT).maximumRange
            if(light_value < 5)
                ratingBar.numStars = 1
            else if(light_value < 100)
                ratingBar.numStars = 5
            else
                ratingBar.numStars = 8
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onDestroy() {
        acc_sensor.unregisterListener(this)
        super.onDestroy()
    }
}