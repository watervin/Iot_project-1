#---- publisher.py   보내기 (브로커 : 라즈베리파이 )
import datetime as dt
import time
import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish
import Adafruit_DHT
import urllib2
sensor = Adafruit_DHT.DHT11
pin = 4


# broker_address="192.xxx.x.xx" # WindowsPC
broker_address="localhost"  # raspberryPi
broker_port=1883
client = mqtt.Client() #create new instance
print("connecting to broker")
client.connect(host=broker_address, port=broker_port)
print("Subscribing to topic","temp/temp")
# client.publish("temp/temp","HI~~~~")   #publish

while True:
       h,t = Adafruit_DHT.read_retry(sensor, pin)
       client.publish("temp/temp", str(t))
       client.publish("temp/temp", str(h))
       print("Temperature = {0:0.1f}*C Humidity = {1:0.1f}%".format(t, h))

       time.sleep(15)
# 15초에 한번씩 온도/습도 보내기  
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

client.connect('192.168.1.34', 1883)  # 라즈베리파이 커넥트  
client.subscribe('temp/temp', 1)  # 토픽 : temp/temp  | qos : 1
client.loop_forever()