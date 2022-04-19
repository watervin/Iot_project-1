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


    //받아 오는값
    // init,android water,현재 토양수분,활성화,기준토양
    val TAG = "MqttActivity"
    lateinit var mqttClient: Mqtt
    lateinit var text : String
    lateinit var arr: List<String>
    lateinit var Humidity_position : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_humidity)

        //값 받아오기
        if (intent.hasExtra("time_data")) {
            text = intent.getStringExtra("time_data").toString()
            arr = text.split(",")
            Log.i("Mqtt_result", "수신] 데이터 값 : $text // $arr")
        } else {
            Log.i("Mqtt_result", "수신] 값 못받아왔음")
        }

        //
        soil_info.setText(arr[2])
        setting_soil.setText(arr[4])


        if (arr[3] == "1"){
            auto_water.isChecked = true
            btn_set.setEnabled(true);
        }
        else{
            auto_water.isChecked = false
            btn_set.setEnabled(false);
        }

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

            if(arr[3] == "0") {
                //switch가 꺼져있을때
                mqttClient.publish(PUB_TOPIC, "setting,water,0,$Humidity_position")
                Log.i("Mqtt_result", "전송] setting,water,0,$Humidity_position")
                Toast.makeText(this@HumidityActivity, "기준 토양습도가 설정되었습니다 ", Toast.LENGTH_SHORT).show()
            }


            else if(arr[3] == "1") {
                //switch가 켜져있을때
                mqttClient.publish(PUB_TOPIC, "setting,water,1,$Humidity_position")
                Log.i("Mqtt_result", "전송] setting,water,1,$Humidity_position")
                Toast.makeText(this@HumidityActivity, "기준 토양습도가 설정되었습니다 ", Toast.LENGTH_SHORT).show()

            }


    }
        auto_water.setOnCheckedChangeListener{ compoundButton, b ->
            if(b){
                //스위치가 켜져있을 때
                btn_set.setEnabled(true);
                var text = String.format("setting,water,1,%s",arr[4])
                mqttClient.publish(PUB_TOPIC, text)
                Log.i("Mqtt_result", "전송] $text ")
                Toast.makeText(this@HumidityActivity, "물주기 자동 설정 ON", Toast.LENGTH_SHORT).show()
                // init,android water,활성화여부,현재 토양수분,기준토양


            }else{
                //스위치가 꺼져있을 때
                btn_set.setEnabled(false);

                var text = String.format("setting,water,0,%s",arr[4])
                mqttClient.publish(PUB_TOPIC, text)
                Log.i("Mqtt_result", "전송] $text ")
                Toast.makeText(this@HumidityActivity, "물주기 자동 설정 OFF", Toast.LENGTH_SHORT).show()
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
