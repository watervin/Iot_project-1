package com.example.today_room

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mqtt_ex.Mqtt
import kotlinx.android.synthetic.main.activity_led.*
import org.eclipse.paho.client.mqttv3.MqttMessage

private const val SUB_TOPIC = "iot/#"
private const val PUB_TOPIC = "data/led"
private const val SERVER_URI = "tcp://175.211.162.37:1883"

class LedActivity : AppCompatActivity() {
    val TAG = "MqttActivity"
    lateinit var mqttClient: Mqtt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_led)


        btn_back.setOnClickListener {
            finish()
        }

        switchLed.setOnCheckedChangeListener{ compoundButton, b ->
            if(b){
                mqttClient.publish(PUB_TOPIC, "ON")

            }else{
                mqttClient.publish(PUB_TOPIC,"OFF")
            }
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





