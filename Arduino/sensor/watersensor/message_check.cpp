#include "message_check.h"

void message_check(String msg, int &auto_watering, int &auto_setting)
{
    int spl1,spl2,spl3,spl4,spl5,spl6,spl7;
    String value = "";
    spl1 = msg.indexOf(",");
    value = msg.substring(0, spl1);
    
    //init packet return
    if(value.equals("init_return"))
    {
        spl2 = msg.indexOf(",",spl1+1);
        value = msg.substring(spl1+1, spl2);
        
        if(value.equals("water"))
        {
            spl3 = msg.indexOf(",",spl2+1);
            value = msg.substring(spl2+1, spl3);

            //setting disable
            if(value.equals("0"))
            {
                auto_watering = 0;
                
            }

            if(value.equals("1"))
            {
                auto_watering = 1;

                spl4 = msg.indexOf(",",spl3+1);
                value = msg.substring(spl3+1, spl4);
                
                auto_setting = value.toInt();
                
            }

            Serial.print("init setting : ");
            Serial.print(auto_watering);
            Serial.print(" ");
            Serial.println(auto_setting);
        }
    }


    //setting
    if(value.equals("setting"))
    {
        spl2 = msg.indexOf(",",spl1+1);
        value = msg.substring(spl1+1, spl2);
        
        if(value.equals("water"))
        {
            spl3 = msg.indexOf(",",spl2+1);
            value = msg.substring(spl2+1, spl3);

            auto_watering = value.toInt();
            if(auto_watering==0)
            {
                auto_setting = 0;
            }
            else if(auto_watering==1)
            {
                spl4 = msg.indexOf(",",spl3+1);
                value = msg.substring(spl3+1, spl4);
                auto_setting = value.toInt();
            }


            Serial.print("setting packet : ");
            Serial.print(auto_watering);
            Serial.print(" ");
            Serial.println(auto_setting);


        }

        
    }

}