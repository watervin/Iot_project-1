from servo import eat_start

def data_control(client, message):
    return_value = {}

    try:
        data_split = message.split(',')
        if(data_split[0] == 'init_return' or data_split[0] == 'setting'):
            if(data_split[1] == 'pet_feed'):
                return_value['enable'] = data_split[2]
                return_value['eatting_hour_1'] = data_split[3]
                return_value['eatting_min_1'] = data_split[4]
                return_value['eatting_hour_2'] = data_split[5]
                return_value['eatting_min_2'] = data_split[6]
                return return_value

        if(data_split[0] == 'on'):
            eat_start()

        else:        
            print("recv_data error")
        return -1


    except:
        print("recv_data error")