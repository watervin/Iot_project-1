import datetime
from multiprocessing.dummy import Value
from file_control import file_open, file_write


def data_check(message,client):

    try:
        data_split = message.split(',')

    #init 패킷
        if(data_split[0] == 'init'):
            #무드등init
            if(data_split[1] == 'mood'):
                value = file_open("mood_time")
                client.publish('Iot/LED',f"init_return,mood,{value}")
                print("iot/LED send")
                pass

            #안드로이드init
            if(data_split[1] == 'android_mood'):
                value = file_open("mood_time")
                client.publish('Android',f"init_return,android_mood,{value}")
                print("Android send")
                pass
    
    #setting요청
        if(data_split[0] == 'setting'):
            if(data_split[1] == 'mood_time'):
                status = data_split[2]
                start_time_h = data_split[3]
                start_time_m = data_split[4]
                end_time_h = data_split[5]
                end_time_m = data_split[6]

                value = f"{status},{start_time_h},{start_time_m},{end_time_h},{end_time_m}"
                file_write("mood_time_test",value)

                client.publish('Iot/LED',f"setting,mood,{value}")
                print("setting send")


        

    #센서
        #미세먼지 센서
        if(data_split[0] == 'dust'):
            now = datetime.datetime.now()
            value = data_split[1]
            file_write("dust_sensor",f"{value},{now}")
            pass

    except:
        print("recv_data error")
