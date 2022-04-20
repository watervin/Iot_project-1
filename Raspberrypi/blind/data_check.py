


def data_check(blind_val,val_1,val_2, message):
    
    state = [0,0,0,0,0,0,0]
    print(type(f"message = {message}"))
    if(message=='UP')or(message=='DOWN'):
        print("blind UP, DOWN data")
        blind_val  = message
        state = [2,2,2,2,2,2,2]
        print(f"state1 = {state}")
        
    if(message!='UP')and(message!='DOWN'):
        val_1 = message.split(",")

        print(f"val_1={val_1}")
        if (val_1[0] == "init_return") or( val_1[0] == "setting"):
            print("init_return")
            state[0] = 1

            if(val_1[1] == "blind"):
                print("blind")
                state[1] = 1
            if(val_1[2] == "1" ):
                state[2] = 1 
            if(val_1[2] == "0" ):
                state[2] = 1
            if(24>=int(val_1[3])>=0):
                state[3] = 1
            if(60 > int(val_1[4])>=0):
                state[4] = 1
            if(24>=int(val_1[5])>=0):
                state[5] = 1
            if(60 > int(val_1[6])>=0):
                state[6] = 1 

    return state

    
    
        

    
      
            
            
    
