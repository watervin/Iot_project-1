package com.example.today_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.mqtt_ex.Mqtt
import kotlinx.android.synthetic.main.activity_led.*
import kotlinx.android.synthetic.main.activity_led.btn_back
import kotlinx.android.synthetic.main.activity_pet_feed.*
import org.eclipse.paho.client.mqttv3.*

private const val SUB_TOPIC = "Android" //받아오기
private const val PUB_TOPIC = "data/time" //재찬님
private const val FEED_TOPIC = "Iot/pet" //재찬님
private const val SERVER_URI = "tcp://175.211.162.37:1883"

class PetFeedActivity : AppCompatActivity() {

// 블라인드

    //선언
    val TAG = "MqttActivity"
    lateinit var mqttClient: Mqtt
    lateinit var text : String
    lateinit var arr: List<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_feed)

        //값 받아오기
        if (intent.hasExtra("time_data")){
            text = intent.getStringExtra("time_data").toString()
            arr = text.split(",")
            Log.i("Mqtt_result", "수신] 데이터 값 : $text // $arr")
        } else{
            Log.i("Mqtt_result", "수신] 값 못받아왔음")
        }

        //받아온 값을 셋팅
        feed_hour.setText(arr[3])
        feed_min.setText(arr[4])
        feed_hour2.setText(arr[5])
        feed_min2.setText(arr[6])

        //초기값 받아오기
        if (arr[2] == "1"){
            auto_feed.isChecked = true
            feed_btn_setting.setEnabled(true);
        }
        else{
            auto_feed.isChecked = false
            feed_btn_setting.setEnabled(false);
        }

        //시간보내기
        feed_btn_setting.setOnClickListener { view ->

            var feed_hour = feed_hour.text.toString()
            var feed_min = feed_min.text.toString()
            var feed_hour2 = feed_hour2.text.toString()
            var feed_min2 = feed_min2.text.toString()

            // 스위치가 꺼져있다면
            //재찬님
            if(arr[2] == "0"){
                var time = String.format("setting,pet_feed,0,%s,%s,%s,%s",feed_hour,feed_min,feed_hour2,feed_min2)
                mqttClient.publish(PUB_TOPIC, time)
                Log.i("Mqtt_result", "전송] $time")
                Toast.makeText(this,"$feed_hour : $feed_min ~ $feed_hour2 : $feed_min2 설정완료",Toast.LENGTH_SHORT).show()

            }

            // 스위치가 켜져있다면
            else if(arr[2] == "1"){

                var time = String.format("setting,pet_feed,1,%s,%s,%s,%s",feed_hour,feed_min,feed_hour2,feed_min2)
                mqttClient.publish(PUB_TOPIC, time)
                Log.i("Mqtt_result", "전송] $time")
                Toast.makeText(this,"$feed_hour : $feed_min ~ $feed_hour2 : $feed_min2 설정완료",Toast.LENGTH_SHORT).show()

            }

        }

        //뒤로가기
        btn_back.setOnClickListener {
            finish()
        }

        //밥주기 스위치 on,off


        auto_feed.setOnCheckedChangeListener{ compoundButton, b ->
            if(b){
                // 켜질 때
                feed_btn_setting.setEnabled(true);
                var time = String.format("setting,pet_feed,1,%s,%s,%s,%s",arr[3],arr[4],arr[5],arr[6])
                mqttClient.publish(PUB_TOPIC, time)
                Log.i("Mqtt_result", "전송] $time ")
                Toast.makeText(this,"펫 먹이주기 자동설정 ON",Toast.LENGTH_SHORT).show()


            }else{
                //꺼질 때
                feed_btn_setting.setEnabled(false);
                var time = String.format("setting,pet_feed,0,%s,%s,%s,%s",arr[3],arr[4],arr[5],arr[6])
                mqttClient.publish(PUB_TOPIC,time)
                Log.i("Mqtt_result", "전송] $time ")
                Toast.makeText(this,"펫 먹이주기 자동설정 OFF",Toast.LENGTH_SHORT).show()
            }
        }

        //밥주기 ON
        feed_on.setOnClickListener{
            mqttClient.publish(FEED_TOPIC, "on")
            Log.i("Mqtt_result", "전송] FEED_ON ")
            Toast.makeText(this,"펫에게 밥주기",Toast.LENGTH_SHORT).show()

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