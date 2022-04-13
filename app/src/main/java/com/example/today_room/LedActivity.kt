package com.example.today_room

import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import com.example.mqtt_ex.Mqtt
import kotlinx.android.synthetic.main.activity_humidity.*
import kotlinx.android.synthetic.main.activity_led.*
import kotlinx.android.synthetic.main.activity_led.btn_back
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.text.SimpleDateFormat
import java.util.*

private const val SUB_TOPIC = "Android" //받아오기
private const val LED_TOPIC = "data/time" //led 시간 보내기
private const val PUB_TOPIC = "data/led"
private const val SERVER_URI = "tcp://175.211.162.37:1883"

class LedActivity : AppCompatActivity() {
    val TAG = "MqttActivity"

    lateinit var mqttClient: Mqtt

    lateinit var tPicker: TimePicker
    lateinit var tvHour : TextView
    lateinit var tvMinute : TextView
    var selectHour : Int = 0
    var selectMinute : Int = 0

    lateinit var start_hour : EditText
    lateinit var start_min : EditText
    lateinit var end_hour : EditText
    lateinit var end_min : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_led)

//        start_hour.text = mqttClient.mqttClient.subscribe(SUB_TOPIC, )


        btn_setting.setOnClickListener { view ->

            var start_hour = start_hour.text.toString()
            var start_min = start_min.text.toString()
            var end_hour = end_hour.text.toString()
            var end_min = end_min.text.toString()

            var time = String.format("%s,%s,%s,%s",start_hour,start_min,end_hour,end_min)

            mqttClient.publish(LED_TOPIC, time)


        }


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



