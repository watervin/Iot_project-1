package com.example.today_room

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.mqtt_ex.Mqtt
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.client.mqttv3.MqttMessage

private const val SUB_TOPIC = "Android" //받아오기
private const val PUB_TOPIC = "data" //led 시간 보내기
private const val SERVER_URI = "tcp://175.211.162.37:1883"
var test_data = ""


class MainActivity : AppCompatActivity() {
    lateinit var mqttClient: Mqtt
    lateinit var msg : String

    fun notification(): Unit{


        Log.i("Mqtt_result", "***여기가 실행되야함")
        var builder = NotificationCompat.Builder(this, "MY_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("현관문 알림 서비스")
            .setContentText("집 앞에 무언가가 도착했습니다.")
            .setSmallIcon(R.drawable.ic_box)
//                .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 오레오 버전 이후에는 알림을 받을 때 채널이 필요
            val channel_id = "MY_channel" // 알림을 받을 채널 id 설정
            val channel_name = "today_room" // 채널 이름 설정
            val descriptionText = "현관문 알림 서비스" // 채널 설명글 설정
            val importance = NotificationManager.IMPORTANCE_DEFAULT // 알림 우선순위 설정
            val channel = NotificationChannel(channel_id, channel_name, importance).apply {
                description = descriptionText
            }
            // 만든 채널 정보를 시스템에 등록
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            // 알림 표시: 알림의 고유 ID(ex: 1002), 알림 결과
            notificationManager.notify(1002, builder.build())
        }
    }


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

        btn4.setOnClickListener {
            val intent = Intent(this, deliveryActivity::class.java)
            mqttClient.publish(PUB_TOPIC, "init,android_delivery")
            //0.05초 뒤 화면전환
            Handler().postDelayed({ intent_call(intent) }, 100L)
            Log.i("Mqtt_result", "전송] android_delivery ")
        }

        btn5.setOnClickListener {
            val intent = Intent(this, BlindActivity::class.java)
            mqttClient.publish(PUB_TOPIC, "init,android_blind")
            //0.05초 뒤 화면전환
            Handler().postDelayed({ intent_call(intent) }, 100L)
            Log.i("Mqtt_result", "전송] android_delivery ")
        }

        btn6.setOnClickListener {
            val intent = Intent(this, PetFeedActivity::class.java)
            mqttClient.publish(PUB_TOPIC, "init,android_pet_feed")
            //0.05초 뒤 화면전환
            Handler().postDelayed({ intent_call(intent) }, 100L)
            Log.i("Mqtt_result", "전송] android_pet_feed ")
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


    fun onReceived(topic: String?, message: MqttMessage) {
        // 토픽 수신 처리
        val msg = String(message.payload)
        Log.i("Mqtt_result","수신메세지: $msg")
        test_data = msg //문자열, 비트
        //알람
        if (msg == "arrived_box"){
            notification()
        }
        Log.i("Mqtt_result","수신메세지: "+test_data)
    }

    fun intent_call(intent: Intent){
        Log.i("Mqtt_test","수신메세지: " +test_data)
        intent.putExtra("time_data",test_data)
        startActivity(intent)

    }
    override fun onDestroy() {
        super.onDestroy()
        if (msg == "arrived_box"){
            notification()
        }
    }
}