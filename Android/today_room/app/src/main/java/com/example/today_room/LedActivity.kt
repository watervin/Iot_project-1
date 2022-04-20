package com.example.today_room

import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.mqtt_ex.Mqtt
import kotlinx.android.synthetic.main.activity_humidity.*
import kotlinx.android.synthetic.main.activity_led.*
import kotlinx.android.synthetic.main.activity_led.btn_back
import org.eclipse.paho.client.mqttv3.*
import java.text.SimpleDateFormat
import java.util.*

private const val SUB_TOPIC = "Android" //받아오기
private const val PUB_TOPIC = "data/time" //led 시간 보내기
private const val LED_TOPIC = "Iot/LED"
private const val LIGTH_TOPIC = "Iot/light"
private const val SERVER_URI = "tcp://175.211.162.37:1883"
//무드등 값
class LedActivity : AppCompatActivity() {

    // 무드등 : 받아 오는값
    // init,android mood,활성화여부,시작시간,시작 분,종료시간,종료 분

    // 전등 : 받아오는값
    // "on" , "off"
    //경로 : Iot/light


    //선언
    val TAG = "MqttActivity"
    lateinit var mqttClient: Mqtt
    lateinit var text : String
    lateinit var arr: List<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_led)

        //값 받아오기
        if (intent.hasExtra("time_data")){
            text = intent.getStringExtra("time_data").toString()
            arr = text.split(",")
            Log.i("Mqtt_result", "수신] 데이터 값 : $text // $arr")
        } else{
            Log.i("Mqtt_result", "수신] 값 못받아왔음")
        }

        //받아온 값을 셋팅
        start_hour.setText(arr[3])
        start_min.setText(arr[4])
        end_hour.setText(arr[5])
        end_min.setText(arr[6])

        //스위치 초기값 받아오기
        if (arr[2] == "1"){
            auto_led.isChecked = true
            btn_setting.setEnabled(true);
        }
        else{
            auto_led.isChecked = false
            btn_setting.setEnabled(false);
        }

        //시간보내기
        btn_setting.setOnClickListener { view ->

            var start_hour = start_hour.text.toString()
            var start_min = start_min.text.toString()
            var end_hour = end_hour.text.toString()
            var end_min = end_min.text.toString()

            // 스위치가 꺼져있다면
            if(arr[2] == "0"){
                var time = String.format("setting,mood,0,%s,%s,%s,%s",start_hour,start_min,end_hour,end_min)
                mqttClient.publish(PUB_TOPIC, time)
                Log.i("Mqtt_result", "전송] $time")
                Toast.makeText(this,"$start_hour : $start_min ~ $end_hour : $end_min 설정완료",Toast.LENGTH_SHORT).show()

            }

            // 스위치가 켜져있다면
            else if(arr[2] == "1"){

                var time = String.format("setting,mood,1,%s,%s,%s,%s",start_hour,start_min,end_hour,end_min)
                mqttClient.publish(PUB_TOPIC, time)
                Log.i("Mqtt_result", "전송] $time")
                Toast.makeText(this,"$start_hour : $start_min ~ $end_hour : $end_min 설정완료",Toast.LENGTH_SHORT).show()

            }

        }

        //뒤로가기
        btn_back.setOnClickListener {
            finish()
        }

        //스위치버튼 on,off
        auto_led.setOnCheckedChangeListener{ compoundButton, b ->
            if(b){
                // 켜질 때
                btn_setting.setEnabled(true);
                var time = String.format("setting,mood,1,%s,%s,%s,%s",arr[3],arr[4],arr[5],arr[6])
                mqttClient.publish(PUB_TOPIC, time)
                Log.i("Mqtt_result", "전송] $time ")
                Toast.makeText(this,"무드등 자동설정 ON",Toast.LENGTH_SHORT).show()


            }else{
                //꺼질 때
                btn_setting.setEnabled(false);
                var time = String.format("setting,mood,0,%s,%s,%s,%s",arr[3],arr[4],arr[5],arr[6])
                mqttClient.publish(PUB_TOPIC,time)
                Log.i("Mqtt_result", "전송] $time ")
                Toast.makeText(this,"무드등 자동설정 OFF",Toast.LENGTH_SHORT).show()
            }
        }

        //led on
        led_on.setOnClickListener{
            mqttClient.publish(LED_TOPIC, "on")
            Log.i("Mqtt_result", "전송] LED_ON ")
            Toast.makeText(this,"무드등 ON",Toast.LENGTH_SHORT).show()

        }

        //led off
        led_off.setOnClickListener{
            mqttClient.publish(LED_TOPIC, "off")
            Log.i("Mqtt_result", "전송] LED_OFF")
            Toast.makeText(this,"무드등 OFF",Toast.LENGTH_SHORT).show()
        }

        //LIGTH ON
        Main_led_on.setOnClickListener{
            mqttClient.publish(LIGTH_TOPIC, "on")
            Log.i("Mqtt_result", "전송] LED_ON ")
            Toast.makeText(this,"LED ON",Toast.LENGTH_SHORT).show()
        }

        //LIGTH OFF
        Main_led_off.setOnClickListener{
            mqttClient.publish(LIGTH_TOPIC, "off")
            Log.i("Mqtt_result", "전송] LED_OFF")
            Toast.makeText(this,"LED OFF",Toast.LENGTH_SHORT).show()
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