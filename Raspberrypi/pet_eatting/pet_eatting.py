import paho.mqtt.client as mqtt
from pet_mqttdata import data_control
import time,datetime
from servo import eat_start

setting = {
    'enable' : 0,
}

def current_time():
    nw = datetime.datetime.now() # 현재 시간 설정
    nw_time_hour = nw.hour
    nw_time_minute = nw.minute
    nw_time_second = nw.second
    nw_time = nw_time_hour * 60 * 60 + nw_time_minute * 60 + nw_time_second
    return nw_time


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
    global setting
    message = str(msg.payload.decode("utf-8"))
    current_time = time.strftime('%Y-%m-%d / %H:%M:%S', time.localtime(time.time()))
    print(f"[{current_time}] {message}")
    if(data_control(client,message) != -1):
        setting = data_control(client,message)
        print(setting)

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
client.subscribe('Iot/pet', 1)
client.publish('data',f"init,pet_feed")
client.loop_start()


while(1):

    if(int(setting['enable']) == 1):
        cur_time = current_time()
        setting_time_1 = int(setting['eatting_hour_1']) * 3600 + int(setting['eatting_min_1']) * 60
        setting_time_2 = int(setting['eatting_hour_2']) * 3600 + int(setting['eatting_min_2']) * 60
        if(setting_time_1 == cur_time or setting_time_2 == cur_time):
            eat_time = time.strftime('%Y-%m-%d / %H:%M:%S', time.localtime(time.time()))
            print(f"eatting [{eat_time}]")
            eat_start()
    
    time.sleep(0.1)
    pass
