package com.example.today_room

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.mqtt_ex.Mqtt
import kotlinx.android.synthetic.main.activity_led.*
import kotlinx.android.synthetic.main.activity_led.btn_back
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_photo_detail.*
import org.eclipse.paho.client.mqttv3.MqttMessage

private const val SUB_TOPIC = "Android/image" //받아오기
private const val PUB_TOPIC = "data/time" //led 시간 보내기
private const val SERVER_URI = "tcp://175.211.162.37:1883"

class PhotoDetailActivity : AppCompatActivity() {

    lateinit var mqttClient: Mqtt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        if(intent.hasExtra("photo_data")){
            photo_info.text = intent.getStringExtra("photo_data")
        }
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
        Log.i("Mqtt_result","수신메세지: $msg")
        test_data = msg
        val sentence = byteArrayToBitmap(message.payload)
        photo_detail.setImageBitmap(sentence)
        Log.i("Mqtt_result","수신메세지: "+test_data)
    }
    fun publish() {
        mqttClient.publish(PUB_TOPIC, "1")
    }


    fun byteArrayToBitmap(test_data: ByteArray): Bitmap {

        return BitmapFactory.decodeByteArray(test_data, 0, test_data.size)
    }
}


