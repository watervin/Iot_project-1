package com.example.today_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.mqtt_ex.Mqtt
import kotlinx.android.synthetic.main.activity_humidity.*
import kotlinx.android.synthetic.main.activity_led.*
import kotlinx.android.synthetic.main.activity_led.btn_back
import org.eclipse.paho.client.mqttv3.MqttMessage

private const val SUB_TOPIC = "Android" //받아오기
private const val PUB_TOPIC = "data/time" //led 시간 보내기
//private const val PUB_TOPIC = "data/led"
private const val SERVER_URI = "tcp://175.211.162.37:1883"

class HumidityActivity : AppCompatActivity() {

    val TAG = "MqttActivity"

    lateinit var mqttClient: Mqtt

    lateinit var text : String
    lateinit var arr: List<String>
    lateinit var Humidity_position : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_humidity)


        if (intent.hasExtra("time_data")) {
            text = intent.getStringExtra("time_data").toString()
            arr = text.split(",")
            Log.i("Mqtt_result", "수신] 데이터 값 : $text // $arr")
        } else {
            Log.i("Mqtt_result", "수신] 값 못받아왔음")
        }

        soil_info.setText(arr[2])
        setting_soil.setText(arr[4])

        btn_back.setOnClickListener {
            finish()
        }

        //array속 내가 만든 array
        var Humidity = resources.getStringArray(R.array.my_array)
        spinner.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Humidity)


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {


            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                Humidity_position = Humidity[position].toString()
                Log.i("Mqtt", "수신] 데이터 값 : $Humidity_position")
//                setting_soil.setText(Humidity_position)

            }
        }

        btn_set.setOnClickListener {
        setting_soil.setText(Humidity_position)
            mqttClient.publish(PUB_TOPIC, "setting,water,1,$Humidity_position")
            Log.i("Mqtt_result", "전송] setting,water,1,$Humidity_position")



    }
        auto_water.setOnCheckedChangeListener{ compoundButton, b ->
            if(b){
                mqttClient.publish(PUB_TOPIC, "setting,water,1,10")
                Log.i("Mqtt_result", "전송] setting,water,1,10")

//                var text = String.format("setting,water,1,%s,%s",arr[3],arr[4],arr[5],arr[6])
//                mqttClient.publish(PUB_TOPIC, text)
//                Log.i("Mqtt_result", "전송] $text ")

            }else{
                mqttClient.publish(PUB_TOPIC,"setting,water,0,0")
                Log.i("Mqtt_result", "전송] setting,water,0,0")
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
