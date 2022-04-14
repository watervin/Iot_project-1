#line 1 "d:\\workspace\\sensor\\watersensor\\Watersensor.cpp"
#include "Watersensor.h"


Watersensor::Watersensor(int pin) : pin(pin)
{
    value = 0;
}

int Watersensor::scan()
{
    int percent = 0;
    int i = analogRead(pin);
    Serial.println(i);
    
    percent = i*100/1024;
    if (percent>100){
        percent = 100;
    }

    if(value>0)
    {
        value = percent*0.2+value*0.8;
    }
    else{
        value = percent;
    }
    Serial.println(value);

    return value;
}

int Watersensor::watering(Servo &myservo)
{
    myservo.write(watering_open);
    delay(5000);
    myservo.write(watering_close);

    return 0;
}
