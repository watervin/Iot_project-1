from logging import error
import time
import RPi.GPIO as GPIO
import datetime

# -*- coding: utf-8 -*- 
#---- subscriber.py  데이터 받기 
import paho.mqtt.client as mqtt

from data_check import data_check


blind_val = [0]
val_1 = [0,0,0,0,0,0,0]
val_2 = [0,0,0,0,0,0,0]
state = [0,0,0,0,0,0,0]

StepCounter = 0

StepCount = 4

step = 1000 # 돌리는 횟수



auto_blind = 0




Seq = [[0,0,0,1],
        [0,0,1,0],
        [0,1,0,0],
        [1,0,0,0]]

Seq_r = [[1,0,0,0],
        [0,1,0,0],
        [0,0,1,0],
        [0,0,0,1]]
seq_init = [0,0,0,0]


def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))

#서버로부터 publish message를 받을 때 호출되는 콜백
def on_subscribe(client, userdata, mid, granted_qos):
    print("subscribed: " + str(mid) + " " + str(granted_qos))
    
def on_message(client, userdata, msg):

    global val_1
    global val_2
    global blind_val
    global message

    print(str(msg.payload.decode("utf-8")))
    message = str(msg.payload.decode("utf-8")) 

    state = data_check(blind_val,val_1, val_2, message)

    print(f"state = {state}")
 
    if ( state == [1,1,1,1,1,1,1]):
        val_2 = message.split(",")
    
    if( state == [2,2,2,2,2,2,2]):
        blind_val = message  
    message = ""

def blind_up(step):
    GPIO.setmode(GPIO.BCM)
    StepPins = [12,16,20,21]


    for pin in StepPins:
        GPIO.setup(pin,GPIO.OUT)
        GPIO.output(pin,False)
    i = 0
    global Seq_r, StepCounter, StepCount, blind_val
    while i <= step:
        for pin in range(0, 4):
            xpin = StepPins[pin] # GPIO pin
            if Seq[StepCounter][pin]!=0:
                GPIO.output(xpin, True)
            else:
                GPIO.output(xpin, False)

        StepCounter += 1

        if (StepCounter == StepCount):
            StepCounter = 0
        if (StepCounter<0):
            StepCounter = StepCount
        i = i + 1
        blind_val = ""
        time.sleep(0.01)
    if(i >= step):
        print("blind UP")

        GPIO.cleanup()

def blind_down(step):
    GPIO.setmode(GPIO.BCM)
    StepPins = [12,16,20,21]


    for pin in StepPins:
        GPIO.setup(pin,GPIO.OUT)
        GPIO.output(pin,False)
    i = 0
    global Seq_r, StepCounter, StepCount, blind_val
    while i <= step:
        for pin in range(0, 4):
            xpin = StepPins[pin] # GPIO pin
            if Seq_r[StepCounter][pin]!=0:
                GPIO.output(xpin, True)
            else:
                GPIO.output(xpin, False)

        StepCounter += 1

        if (StepCounter == StepCount):
            StepCounter = 0
        if (StepCounter<0):
            StepCounter = StepCount
        i = i + 1
        blind_val = ""
        time.sleep(0.01)
    if(i >= step):    
        print("blind DOWN")
        GPIO.cleanup()



try:
    client = mqtt.Client() #client 오브젝트 생성
    client.on_connect = on_connect #콜백설정
    client.on_subscribe = on_subscribe
    client.on_message = on_message #콜백설정

    client.connect('175.211.162.37', 1883)  # 라즈베리파이 커넥트  
    client.subscribe('Iot/blind', 0)  
    client.publish('data',"init,blind")
    client.loop_start()
    
    
    
    
    select1 = -1
    select2 = -1
    while 1:  

        
        nw = datetime.datetime.now() # 현재 시간 설정
        try:
            # readadc 함수로 pot_channel의 SPI 데이터를 읽기 
            if(blind_val=="UP"):
                blind_up(step)
                blind_val = ""

            elif(blind_val=="DOWN"):
                blind_down(step)
                blind_val = ""
        
            if(auto_blind == 1):
                nw_time_hour = nw.hour
                nw_time_minute = nw.minute
                nw_time_second = nw.second
                nw_time = nw_time_hour * 3600 + nw_time_minute * 60 + nw_time_second
                if(nw_time == select1 * 60):
                    blind_up(step)
                if(nw_time == select2 * 60):
                    blind_down(step)


                
            if (val_2[0] == "init_return")or(val_2[0]=="setting"):
                print(f"result1 = {val_2[0]}")
              
                if val_2[1] == "blind":
                    print(f"result2 = {val_2[1]}")
                    

                    # 첫 번째 설정 시간
                    select_hour1 = int(val_2[3])
                    select_minute1 = int(val_2[4])
                    select1 = select_hour1 * 60 + select_minute1

                    # 두 번째 설정 시간
                    select_hour2 = int(val_2[5])
                    select_minute2 = int(val_2[6])
                    select2 = select_hour2 * 60 + select_minute2

                    if(val_2[2] == "1"):
                        print(f"result3 = {val_2[2]}")
                        auto_blind = 1
                    if(val_2[2]=="0"):
                            auto_blind = 0
                            print("시간을 비활성화 합니다.")

                val_2 = [0,0,0,0,0,0,0]                                                                          
            time.sleep(0.1)
        except KeyboardInterrupt:
            print("bye!")
            exit()
        except:
            print(error)
            time.sleep(1)
            pass

except KeyboardInterrupt:
    GPIO.cleanup()