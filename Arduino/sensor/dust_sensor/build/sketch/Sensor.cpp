#line 1 "d:\\workspace\\sensor\\dust_sensor\\Sensor.cpp"
#include "Sensor.h"

#define debug 1

Sensor::Sensor()
{
    pinMode(ledP,OUTPUT);
    smoothDensity = 20;

}

Sensor::~Sensor()
{

}

float Sensor::Dust_sensor()
{
    float dustDensity = 0;
    digitalWrite(ledP,LOW); // power on the LED  
    //delayMicroseconds(280);
    delayMicroseconds(400);
    voMeasured = analogRead(dustPin); // read the dust value  
    delayMicroseconds(40); 
    digitalWrite(ledP,HIGH); // turn the LED off  
    delayMicroseconds(9680);  
    // 0 - 5V mapped to 0 - 1023 integer values
    
    calcVoltage = voMeasured*(5.0/1024);
    //dustDensity = (0.17 * calcVoltage - 0.1) *1000;
    dustDensity = (calcVoltage - vol_setting)/0.005;

    if(dustDensity<-15){
        return smoothDensity;
    }
    else if(dustDensity<0){
        dustDensity = 0;
    }


    if(smoothDensity>3 && smoothDensity<200)
        smoothDensity = dustDensity * 0.1 + smoothDensity * 0.9;  
    else
        smoothDensity = 10.0;

    #if debug
    Serial.println(voMeasured);
    Serial.println(dustDensity);
    Serial.println(smoothDensity);
    Serial.println("-------------");
    #endif
    return smoothDensity; 
}
