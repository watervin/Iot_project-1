package com.example.today_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mqtt_ex.Mqtt
import kotlinx.android.synthetic.main.activity_humidity.*
import kotlinx.android.synthetic.main.activity_led.*
import kotlinx.android.synthetic.main.activity_led.btn_back
import kotlinx.android.synthetic.main.activity_myroom.*
import org.eclipse.paho.client.mqttv3.MqttMessage

private const val SUB_TOPIC = "Android" //받아오기
private const val PUB_TOPIC = "data/time" //led 시간 보내기
//private const val PUB_TOPIC = "data/led"
private const val SERVER_URI = "tcp://175.211.162.37:1883"

class myroom : AppCompatActivity() {

    val TAG = "MqttActivity"

    lateinit var mqttClient: Mqtt

    lateinit var text : String
    lateinit var arr: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myroom)

        if (intent.hasExtra("time_data")){
            text = intent.getStringExtra("time_data").toString()
            arr = text.split(",")

            Log.i("Mqtt_result", "데이터 값 : $text // $arr")

        } else{
            Log.i("Mqtt_result", "값 못받아왔음")
        }
        txt_tempval.setText(arr[2])
        txt_humidval.setText(arr[3])
        txt_dustval.setText(arr[4])
        btn_back.setOnClickListener {
            finish()
        }
        mqttClient = Mqtt(this, SERVER_URI)
        try {
            // mqttClient.setCallback { topic, message ->}
            mqttClient.setCallback(::onReceived)
            mqttClient.connect(arrayOf<String>(SUB_TOPIC))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onReceived(topic: String, message: MqttMessage) {
        // 토픽 수신 처리
        val msg = String(message.payload)
    }
    fun publish() {
        mqttClient.publish(PUB_TOPIC, "1")
    }


}