import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish
from data_control import data_check
from file_init import file_init_setting
import time

picture_file_location = "../data/delivery_photo"


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

def on_message2(client, userdata, msg):
    now = time.localtime()
    current_time = "%04d-%02d-%02d_%02d%02d%02d" % (now.tm_year, now.tm_mon, now.tm_mday, now.tm_hour, now.tm_min, now.tm_sec)

    file_name = f"{picture_file_location}/{current_time}.jpg"
    print(file_name)
    f = open(file_name, "wb")
    f.write(msg.payload)
    print("Image Received")
    f.close()
    
    print("file save")

    #f = open(f"output.jpg","rb")
    #fileContent= f.read()
    #byteArr = bytearray(fileContent)
    #publish.single("Android/image", byteArr, hostname="175.211.162.37")
    #print("Send file")


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
client.loop_start()


# 새로운 클라이언트 생성
client2 = mqtt.Client()
# 콜백 함수 설정 on_connect(브로커에 접속), on_disconnect(브로커에 접속중료), on_subscribe(topic 구독),
# on_message(발행된 메세지가 들어왔을 때)
client2.on_connect = on_connect
client2.on_disconnect = on_disconnect
client2.on_subscribe = on_subscribe
client2.on_message = on_message2
# address : localhost, port: 1883 에 연결
client2.connect('175.211.162.37', 1883)

client2.subscribe('Image/#', 1)
client2.loop_forever()