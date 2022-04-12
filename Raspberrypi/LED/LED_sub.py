# 시간 설정
import datetime
from multiprocessing import parent_process 

from gpiozero import PWMLED
from signal import pause
import time

# -*- coding: utf-8 -*- 
#---- subscriber.py  데이터 받기 
import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish

import time

led = PWMLED(14)
nw = datetime.datetime.now() # 현재 시간 설정

status = 0
test = "" 
val = [0,0,0,0,0,0,0]

    


def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))

#서버로부터 publish message를 받을 때 호출되는 콜백
def on_subscribe(client, userdata, mid, granted_qos):
    print("subscribed: " + str(mid) + " " + str(granted_qos))
    
def on_message(client, userdata, msg):
    global val
    print(str(msg.payload.decode("utf-8")))
    test = str(msg.payload.decode("utf-8"))
    val = test.split(",")
    
    

    
try:
    client = mqtt.Client() #client 오브젝트 생성
    client.on_connect = on_connect #콜백설정
    client.on_subscribe = on_subscribe
    client.on_message = on_message #콜백설정

    client.connect('175.211.162.37', 1883)  # 라즈베리파이 커넥트  
    client.subscribe('Iot/LED', 0)  # 토픽 : temp/temp  | qos : 1
    client.publish('data',"init,mood")
    client.loop_start()
    
    
    while 1:
        if((val[2] == "1")&(val[3]=="12")&(val[4]=="0")):
                led.value = 0 # off
                time.sleep(1)
                led.value = 0.5 # half brightness 
                time.sleep(1)
                led.value = 1 # full brightness 
                time.sleep(1)
        elif((val[2] == "1")&(val[5]=="1")&(val[6]=="0")):
                value = 0
                led.value = value
        elif((val[2]=="0")):
                value = 0
                led.value = value

        time.sleep(1)
        pass

except KeyboardInterrupt:
    print("bye")