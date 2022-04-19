package com.example.today_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mqtt_ex.Mqtt
//반려동물 도어락
class doorActivity : AppCompatActivity() {

    val TAG = "MqttActivity"

    lateinit var mqttClient: Mqtt
    lateinit var text : String
    lateinit var arr: List<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_door)

        //값 받아오기
        if (intent.hasExtra("time_data")) {
            text = intent.getStringExtra("time_data").toString()
            arr = text.split(",")
            Log.i("Mqtt_result", "수신] 데이터 값 : $text // $arr")
        } else {
            Log.i("Mqtt_result", "수신] 값 못받아왔음")
        }

    }


}