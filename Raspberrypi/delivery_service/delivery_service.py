# 거리 측정 센서 모듈
from gpiozero import DistanceSensor
from time import sleep

# 이미지 캡쳐

from time import sleep
from picamera import PiCamera
from datetime import datetime 


# mqtt

import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish




MQTT_SERVER = "175.211.162.37"  #Write Server IP Address
MQTT_PATH = "Image"
MQTT_PATH1 = "Android"


# 거리측정
sensor = DistanceSensor(23,24) # echo, trig

# 이미지 캡쳐
nw = datetime.now()

state = 0
state_a = 0 

while True:
    print(f"Distance = {sensor.distance:.2f}m")
    dis = round(sensor.distance,2)
    sleep(1)
    if (dis <= 0.2) :

        state = 1
        # 택배가 도착 했을 때 한 번만 울리게 하기위하여 지정해준 변수 
        state_a += 1 

    if(state_a == 1) and (dis <= 0.2):
        publish.single(MQTT_PATH1, "arrived_box", hostname=MQTT_SERVER)
        print("send message")
            
    if(state == 1) and (dis > 0.5):
        state = 0 
        state_a = 0
        
        # Create File
        my_file = open(f'delivery_service/img{nw.year}-{nw.month}-{nw.day}_{nw.hour}:{nw.minute}:{nw.second}.jpg', 'wb')
        camera = PiCamera()
        camera.start_preview()
        sleep(2)

        camera.capture(my_file)

        # Read File
        f = open(f"delivery_service/img{nw.year}-{nw.month}-{nw.day}_{nw.hour}:{nw.minute}:{nw.second}.jpg","rb")
        fileContent= f.read()
        byteArr = bytearray(fileContent)
        publish.single(MQTT_PATH, byteArr, hostname=MQTT_SERVER)
        print("Send file")
        camera.close()
        

        


