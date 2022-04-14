package com.example.today_room

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mqtt_ex.Mqtt
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.concurrent.timer
import kotlin.coroutines.CoroutineContext

private const val SUB_TOPIC = "Android" //받아오기
private const val PUB_TOPIC = "data" //led 시간 보내기
//private const val PUB_TOPIC = "data/led"
private const val SERVER_URI = "tcp://175.211.162.37:1883"
var test_data = ""

class MainActivity : AppCompatActivity() {
    lateinit var mqttClient: Mqtt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btn1.setOnClickListener {
            val intent = Intent(this, LedActivity::class.java)

            mqttClient.publish(PUB_TOPIC, "init,android_mood")
            //0.05초 뒤 화면전환
            Handler().postDelayed({ intent_call(intent) }, 100L)
            Log.i("Mqtt_result", "전송] android_mood ")

        }

        btn2.setOnClickListener {
            val intent = Intent(this, HumidityActivity::class.java)
            mqttClient.publish(PUB_TOPIC, "init,android_water")
            //0.05초 뒤 화면전환
            Handler().postDelayed({ intent_call(intent) }, 100L)
            Log.i("Mqtt_result", "전송] android_water ")
        }

        btn3.setOnClickListener {
            val intent = Intent(this, myroom::class.java)
            mqttClient.publish(PUB_TOPIC, "init,android_room")
            //0.05초 뒤 화면전환
            Handler().postDelayed({ intent_call(intent) }, 100L)
            Log.i("Mqtt_result", "전송] android_room ")
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
        Log.i("Mqtt_result","수신메세지: $msg")
        test_data=msg
        Log.i("Mqtt_result","수신메세지: "+test_data)
    }

    fun intent_call(intent: Intent){
        Log.i("Mqtt_test","수신메세지: " +test_data)
        intent.putExtra("time_data",test_data)
        startActivity(intent)

    }



}