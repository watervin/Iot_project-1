package com.example.today_room

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.mqtt_ex.Mqtt
import kotlinx.android.synthetic.main.activity_delivery.*
import kotlinx.android.synthetic.main.activity_led.btn_back
import org.eclipse.paho.client.mqttv3.MqttMessage

// 그 집앞 택배

private const val SUB_TOPIC = "Android" //받아오기
private const val PUB_TOPIC = "data/time" //led 시간 보내기
private const val SERVER_URI = "tcp://175.211.162.37:1883"

class deliveryActivity : AppCompatActivity() {


    val TAG = "MqttActivity"
    lateinit var mqttClient: Mqtt
    lateinit var text: String
    lateinit var arr: List<String>
    lateinit var photo_arr: List<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery)

        //리스트뷰
        val items = mutableListOf<ListViewItem>()
        val adapter = ListViewAdapter(items)
        photoListView.adapter = adapter

        photoListView.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->

            val myIntent = Intent(this, PhotoDetailActivity::class.java)
            val photoDate = items.get(position).title

            mqttClient.publish(PUB_TOPIC, "photo,"+ photoDate)

            myIntent.putExtra("photo_data", photoDate)
            startActivity(myIntent)
        }


        //값 받아오기
        if (intent.hasExtra("time_data")) {
            text = intent.getStringExtra("time_data").toString()
            arr = text.split(",")
            photo_arr = arr.drop(1)
            Log.i("Mqtt_result", "수신] 데이터 값 : $text // $arr")
        } else {
            Log.i("Mqtt_result", "수신] 값 못받아왔음")
        }


        for(i in photo_arr){
            items.add(ListViewItem(i))
        }


        btn_back.setOnClickListener {
            finish()
        }

        mqttClient = Mqtt(this, SERVER_URI)
        try {
            mqttClient.setCallback(::onReceived)
            mqttClient.connect(arrayOf<String>(SUB_TOPIC))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //***여기서부터
        //알람
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent
            .getActivity(this, 0, notificationIntent, FLAG_UPDATE_CURRENT)

        if (arr[0] == "arrived_box") {
            Log.i("Mqtt_result", "***여기가 실행되야함")
            var builder = NotificationCompat.Builder(this, "MY_channel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("현관문 알림 서비스")
                .setContentText("집 앞에 무언가가 도착했습니다.")
                .setSmallIcon(R.drawable.ic_box)
                .setContentIntent(pendingIntent)
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
    }

    fun onReceived(topic: String, message: MqttMessage) {
        // 토픽 수신 처리
        val msg = String(message.payload)
    }
    fun publish() {
        mqttClient.publish(PUB_TOPIC, "1")
    }



    //홈화면
    override fun onStop() {
        super.onStop()
        Log.d("Mqtt_result", "onStop")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent
            .getActivity(this, 0, notificationIntent, FLAG_CANCEL_CURRENT)

        if (arr[0] == "arrived_box") {
            Log.i("Mqtt_result", "***알람 실행**")
            var builder = NotificationCompat.Builder(this, "MY_channel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("현관문 알림 서비스")
                .setContentText("집 앞에 무언가가 도착했습니다.")
                .setSmallIcon(R.drawable.ic_box)
                .setContentIntent(pendingIntent)
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
    }


    override fun onDestroy() {
        super.onDestroy()

        Log.d("Mqtt_result", "onDestroy")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent
            .getActivity(this, 0, notificationIntent, FLAG_CANCEL_CURRENT)

        if (arr[0] == "arrived_box") {
            Log.i("Mqtt_result", "***알람 실행**")
            var builder = NotificationCompat.Builder(this, "MY_channel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("현관문 알림 서비스")
                .setContentText("집 앞에 무언가가 도착했습니다.")
                .setSmallIcon(R.drawable.ic_box)
                .setContentIntent(pendingIntent)
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
    }
}