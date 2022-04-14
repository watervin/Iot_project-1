package com.example.today_room

import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
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
private const val SERVER_URI = "tcp://175.211.162.37:1883"

class LedActivity : AppCompatActivity() {

    val TAG = "MqttActivity"

    lateinit var mqttClient: Mqtt

    lateinit var text : String
    lateinit var arr: List<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_led)

        if (intent.hasExtra("time_data")){
            text = intent.getStringExtra("time_data").toString()
            arr = text.split(",")

            Log.i("Mqtt_result", "수신] 데이터 값 : $text // $arr")

        } else{
            Log.i("Mqtt_result", "수신] 값 못받아왔음")
        }
//        start_hour.text = mqttClient.mqttClient.subscribe(SUB_TOPIC, )

        start_hour.setText(arr[3])
        start_min.setText(arr[4])
        end_hour.setText(arr[5])
        end_min.setText(arr[6])

        btn_setting.setOnClickListener { view ->

            var start_hour = start_hour.text.toString()
            var start_min = start_min.text.toString()
            var end_hour = end_hour.text.toString()
            var end_min = end_min.text.toString()


            var time = String.format("setting,mood,1,%s,%s,%s,%s",start_hour,start_min,end_hour,end_min)

            mqttClient.publish(PUB_TOPIC, time)
            Log.i("Mqtt_result", "전송] $time")


        }


        btn_back.setOnClickListener {
            finish()
        }

        auto_led.setOnCheckedChangeListener{ compoundButton, b ->
            if(b){

                var time = String.format("setting,mood,1,%s,%s,%s,%s",arr[3],arr[4],arr[5],arr[6])
                mqttClient.publish(PUB_TOPIC, time)
                Log.i("Mqtt_result", "전송] $time ")

            }else{
                var time = String.format("setting,mood,0,%s,%s,%s,%s",arr[3],arr[4],arr[5],arr[6])
                mqttClient.publish(PUB_TOPIC,"time")
                Log.i("Mqtt_result", "전송] $time ")
            }
        }

        led_on.setOnClickListener{
            mqttClient.publish(LED_TOPIC, "on")
            Log.i("Mqtt_result", "전송] LED_ON ")

        }

        led_off.setOnClickListener{
            mqttClient.publish(LED_TOPIC, "off")
            Log.i("Mqtt_result", "전송] LED_OFF")
        }
//        mqttClient.mqttClient.subscribe(SUB_TOPIC,hour)


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


    //시간 받기
    fun getTime(button: Button, context: Context){

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            button.text = SimpleDateFormat("HH:mm").format(cal.time)
        }

        TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

    }

}

