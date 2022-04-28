package com.example.sensorsapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.time.*


class  MainActivity : AppCompatActivity()  {


    private lateinit var square: TextView

    private lateinit var ratingBar: RatingBar

    private lateinit var receiver: LightLevelReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        square = findViewById(R.id.square)
        ratingBar = findViewById(R.id.ratingBar)

        val intent = Intent(this, SensorsIntentService::class.java)
        startService(intent)

        receiver = LightLevelReceiver()
        registerReceiver(receiver,  IntentFilter("GET_SENSORS_DATA"))
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {

//
                    val lightValue = receiver.getLight()
                    val coordinates = receiver.getCoordinates()
                    val color = if(coordinates[0].toInt() == 0 && coordinates[1].toInt() == 0) Color.GREEN else Color.GRAY
                    square.setBackgroundColor(color)
                    ratingBar.rating = lightValue/5000


            }
        }, 0, 5000)
    }

     class LightLevelReceiver : BroadcastReceiver() {
         private var coordinates = floatArrayOf(0.0F, 0.0F)

         private var light = 0.0F
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == "GET_SENSORS_DATA") {
                light = intent.getFloatExtra("LIGHT_DATA", 0.0F)
                coordinates = intent.getFloatArrayExtra("ACCEL_DATA")!!
            }
        }
         fun getLight(): Float {
             return light
         }
         fun getCoordinates(): FloatArray{
             return  coordinates
         }
    }
}