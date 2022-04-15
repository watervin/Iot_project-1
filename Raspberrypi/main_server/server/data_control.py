import datetime
from multiprocessing.dummy import Value
from file_control import file_open, file_write


def data_check(message,client):

    try:
        data_split = message.split(',')

    #init 패킷
        if(data_split[0] == 'init'):
        #무드등init
            #무드등센서 init
            if(data_split[1] == 'mood'):
                value = file_open("mood_time")
                client.publish('Iot/LED',f"init_return,mood,{value}")
                print("iot/LED send")
                pass

            #안드로이드 무드등 init
            if(data_split[1] == 'android_mood'):
                value = file_open("mood_time")
                client.publish('Android',f"init_return,android_mood,{value}")
                print("Android_mood send")
                pass


        #화분init
            #수분센서 init
            if(data_split[1] == 'water'):
                value = file_open("water_setting")
                client.publish('Iot/water',f"init_return,water,{value}")
                print("iot/water send")
                pass

            #안드로이드 수분센서 init
            if(data_split[1] == 'android_water'):
                value = file_open("water_sensor")
                value2 = file_open("water_setting")
                client.publish('Android',f"init_return,android_water,{value},{value2}")
                print("Android_water send")
                pass

        #방정보init
            #방정보_안드로이드 init
            if(data_split[1] == 'android_room'):
                value = file_open("temperature_sensor")
                value2 = file_open("dust_sensor")
                client.publish('Android',f"init_return,android_room,{value},{value2}")
                print("android_room send")
                pass
        
    
    #setting요청
        if(data_split[0] == 'setting'):
            if(data_split[1] == 'mood'):
                status = data_split[2]
                start_time_h = data_split[3]
                start_time_m = data_split[4]
                end_time_h = data_split[5]
                end_time_m = data_split[6]

                value = f"{status},{start_time_h},{start_time_m},{end_time_h},{end_time_m}"
                file_write("mood_time",value)

                client.publish('Iot/LED',f"setting,mood,{value}")
                print("mood setting send")

        if(data_split[0] == 'setting'):
            if(data_split[1] == 'water'):
                status = data_split[2]
                water_setting = data_split[3]
                

                value = f"{status},{water_setting}"
                file_write("water_setting",value)

                client.publish('Iot/water',f"setting,water,{value}")
                print("water setting send")


        

    #센서
        #미세먼지 센서
        if(data_split[0] == 'dust'):
            #now = datetime.datetime.now()
            value = data_split[1]
            sensor_value = float(value)

            #file_write("dust_sensor",f"{sensor_value:.1f},{now}")
            file_write("dust_sensor",f"{sensor_value:.1f}")
            pass

        #수분센서
        if(data_split[0] == 'water'):
            #now = datetime.datetime.now()
            value = data_split[1]
            sensor_value = float(value)
            #file_write("water_sensor",f"{sensor_value:.1f},{now}")
            file_write("water_sensor",f"{sensor_value:.1f}")
            pass

        #온습도센서
        if(data_split[0] == 'temperature'):
            #now = datetime.datetime.now()
            value = data_split[1]
            value2 = data_split[2]
            temperature = float(value)
            humidity = float(value2)
            #file_write("temperature_sensor",f"{sensor_value:.1f},{now}")
            file_write("temperature_sensor",f"{temperature:.1f},{humidity:.1f}")
            pass

    except:
        print("recv_data error")
