import datetime, time
import paho.mqtt.publish as publish
from file_control import file_open, file_write, file_list_print, image_byte


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

        #택배사진 정보 init
            #안드로이드 사진정보요청
            if(data_split[1] == 'android_delivery'):
                photo_name = file_list_print()
                header = "photo,"
                photo_name_list = ','.join(photo_name)
                send_msg = header + photo_name_list
                
                #print(send_msg)

                client.publish('Android',send_msg)
                print("android_delivery send")
                pass
        
        #펫 사료 init
            #펫 사료 센서 요청
            if(data_split[1] == 'pet_feed'):
                value = file_open("pet_eat")
                client.publish('Iot/pet',f"init_return,pet_feed,{value}")
                print("iot/pet send")
                pass

            #안드로이드 펫 시간 요청
            if(data_split[1] == 'android_pet_feed'):
                value = file_open("pet_eat")
                client.publish('Android',f"init_return,android_pet_feed,{value}")
                print("Android_pet send")
                pass

        #블라인드 init
            #블라인드 센서 요청
            if(data_split[1] == 'blind'):
                value = file_open("blind_time")
                client.publish('Iot/blind',f"init_return,blind,{value}")
                print("iot/blind send")
                pass

            #안드로이드 블라인드 시간 요청
            if(data_split[1] == 'android_blind'):
                value = file_open("blind_time")
                client.publish('Android',f"init_return,android_blind,{value}")
                print("Android_blind send")
                pass
    
    #setting요청
        #무드등 세팅
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
        #수분센서 세팅
            if(data_split[1] == 'water'):
                status = data_split[2]
                water_setting = data_split[3]
                

                value = f"{status},{water_setting}"
                file_write("water_setting",value)

                client.publish('Iot/water',f"setting,water,{value}")
                print("water setting send")
        #펫 사료 시간 세팅
            if(data_split[1] == 'pet_feed'):
                status = data_split[2]
                time_h_1 = data_split[3]
                time_m_1 = data_split[4]
                time_h_2 = data_split[5]
                time_m_2 = data_split[6]

                value = f"{status},{time_h_1},{time_m_1},{time_h_2},{time_m_2}"
                file_write("pet_eat",value)

                client.publish('Iot/pet',f"setting,pet_feed,{value}")
                print("pet setting send")
        #blind 시간 세팅
            if(data_split[1] == 'blind'):
                status = data_split[2]
                time_h_1 = data_split[3]
                time_m_1 = data_split[4]
                time_h_2 = data_split[5]
                time_m_2 = data_split[6]

                value = f"{status},{time_h_1},{time_m_1},{time_h_2},{time_m_2}"
                file_write("blind_time",value)

                client.publish('Iot/blind',f"setting,blind,{value}")
                print("blind setting send")


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

    #이미지
        if(data_split[0] == 'photo'):
            value = data_split[1]
            byteArr = image_byte(value)
            time.sleep(0.3)
            publish.single("Android/image", byteArr, hostname="175.211.162.37")
            if(byteArr!=-1):
                print(f"이미지 전송 [{value}]")
            else:
                print("전송실패")

    except:
        print("recv_data error")
