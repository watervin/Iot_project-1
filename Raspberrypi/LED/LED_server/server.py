import paho.mqtt.client as mqtt
from LED.data_control import data_check
from file_init import file_init_setting
import time


def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("connected OK")
    else:
        print("Bad connection Returned code=", rc)


def on_disconnect(client, userdata, flags, rc=0):
    print(str(rc))


def on_subscribe(client, userdata, mid, granted_qos):
    #print("subscribed: " + str(mid) + " " + str(granted_qos))
    pass


def on_message(client, userdata, msg):
    message = str(msg.payload.decode("utf-8"))
    current_time = time.strftime('%Y-%m-%d / %H:%M:%S', time.localtime(time.time()))
    print(f"[{current_time}] {message}")
    data = data_check(message,client)

#파일 초기화
file_init_setting()

# 새로운 클라이언트 생성
client = mqtt.Client()
# 콜백 함수 설정 on_connect(브로커에 접속), on_disconnect(브로커에 접속중료), on_subscribe(topic 구독),
# on_message(발행된 메세지가 들어왔을 때)
client.on_connect = on_connect
client.on_disconnect = on_disconnect
client.on_subscribe = on_subscribe
client.on_message = on_message
# address : localhost, port: 1883 에 연결
client.connect('175.211.162.37', 1883)

client.subscribe('data/#', 1)
client.loop_forever()
