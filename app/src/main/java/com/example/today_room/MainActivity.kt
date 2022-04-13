package com.example.today_room

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.mqtt_ex.Mqtt
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.client.mqttv3.MqttMessage

private const val SUB_TOPIC = "Android" //받아오기
private const val LED_TOPIC = "data" //led 시간 보내기
private const val PUB_TOPIC = "data/led"
private const val SERVER_URI = "tcp://175.211.162.37:1883"

class MainActivity : AppCompatActivity() {
    val TAG = "MqttActivity"

    lateinit var mqttClient: Mqtt
    lateinit var tPicker: TimePicker
    lateinit var tvHour : TextView
    lateinit var tvMinute : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mqttClient = Mqtt(this, SERVER_URI)
        try {
            // mqttClient.setCallback { topic, message ->}
            mqttClient.setCallback(::onReceived)
            mqttClient.connect(arrayOf<String>(SUB_TOPIC))


        } catch (e: Exception) {
            e.printStackTrace()
        }


        btn1.setOnClickListener {
            val intent = Intent(this, LedActivity::class.java)
            startActivity(intent)
            mqttClient.publish(PUB_TOPIC, "get LED time")
        }

        btn2.setOnClickListener {
            val intent = Intent(this, HumidityActivity::class.java)
            startActivity(intent)
        }

        btn3.setOnClickListener {
            val intent = Intent(this, myroom::class.java)
            startActivity(intent)
        }
    }

    fun onReceived(topic: String, message: MqttMessage) {
        // 토픽 수신 처리
        val msg = String(message.payload)
        Log.i("Mqtt","수신메세지: $msg")
    }

}