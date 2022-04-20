package com.example.today_room

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mqtt_ex.Mqtt
import kotlinx.android.synthetic.main.activity_blind.*
import org.eclipse.paho.client.mqttv3.MqttMessage

private const val SUB_TOPIC = "Android" //받아오기
private const val PUB_TOPIC = "data/blind" //led 시간 보내기
private const val BLIND_TOPIC = "Iot/blind"
private const val SERVER_URI = "tcp://175.211.162.37:1883"

class BlindActivity : AppCompatActivity() {

    val TAG = "MqttActivity"
    lateinit var mqttClient: Mqtt
    lateinit var text : String
    lateinit var arr: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blind)

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
            switch_auto_blind.isChecked = true
            blind_btn_setting.setEnabled(true);
        }
        else{
            switch_auto_blind.isChecked = false
            blind_btn_setting.setEnabled(false);
        }

        switch_auto_blind.setOnCheckedChangeListener{ compoundButton, b ->
            if(b){
                // 켜질 때
                blind_btn_setting.setEnabled(true);
                var time = String.format("setting,blind,1,%s,%s,%s,%s",arr[3],arr[4],arr[5],arr[6])
                mqttClient.publish(PUB_TOPIC, time)
                Log.i("Mqtt_result", "전송] $time ")
                Toast.makeText(this,"블라인드 자동설정 ON",Toast.LENGTH_SHORT).show()


            }else{
                //꺼질 때
                blind_btn_setting.setEnabled(false);
                var time = String.format("setting,blind,0,%s,%s,%s,%s",arr[3],arr[4],arr[5],arr[6])
                mqttClient.publish(PUB_TOPIC,time)
                Log.i("Mqtt_result", "전송] $time ")
                Toast.makeText(this,"블라인드 자동설정 OFF",Toast.LENGTH_SHORT).show()
            }
        }



        //시간보내기
        blind_btn_setting.setOnClickListener { view ->

            var start_hour = start_hour.text.toString()
            var start_min = start_min.text.toString()
            var end_hour = end_hour.text.toString()
            var end_min = end_min.text.toString()

            // 스위치가 꺼져있다면
            if(arr[2] == "0"){
                var time = String.format("setting,blind,0,%s,%s,%s,%s",start_hour,start_min,end_hour,end_min)
                mqttClient.publish(PUB_TOPIC, time)
                Log.i("Mqtt_result", "전송] $time")
                Toast.makeText(this,"$start_hour : $start_min ~ $end_hour : $end_min 설정완료",Toast.LENGTH_SHORT).show()

            }

            // 스위치가 켜져있다면
            else if(arr[2] == "1"){

                var time = String.format("setting,blind,1,%s,%s,%s,%s",start_hour,start_min,end_hour,end_min)
                mqttClient.publish(PUB_TOPIC, time)
                Log.i("Mqtt_result", "전송] $time")
                Toast.makeText(this,"$start_hour : $start_min ~ $end_hour : $end_min 설정완료",Toast.LENGTH_SHORT).show()

            }

        }



        blind_up.setOnClickListener{
            mqttClient.publish(BLIND_TOPIC, "UP")
            Log.i("Mqtt_result", "전송] BLIND UP ")
            Toast.makeText(this,"BLIND UP",Toast.LENGTH_SHORT).show()
        }

        blind_down.setOnClickListener{
            mqttClient.publish(BLIND_TOPIC, "DOWN")
            Log.i("Mqtt_result", "전송] BLIND DOWN ")
            Toast.makeText(this,"BLIND DOWN",Toast.LENGTH_SHORT).show()
        }

        //뒤로가기
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