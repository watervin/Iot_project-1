# -*- coding: utf-8 -*- 
#---- subscriber.py  데이터 받기 
import paho.mqtt.client as mqtt
import time


def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))

#서버로부터 publish message를 받을 때 호출되는 콜백
def on_subscribe(client, userdata, mid, granted_qos):
    print("subscribed: " + str(mid) + " " + str(granted_qos))
    
def on_message(client, userdata, msg):
    print(str(msg.payload.decode("utf-8")))
    

client = mqtt.Client() #client 오브젝트 생성
client.on_connect = on_connect #콜백설정
client.on_subscribe = on_subscribe
client.on_message = on_message #콜백설정

client.connect('175.211.162.37', 1883)  # 라즈베리파이 커넥트  
client.subscribe('temp/temp', 0)  # 토픽 : temp/temp  | qos : 1
client.loop_forever()