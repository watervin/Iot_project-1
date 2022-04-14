#ifndef SENSOR_H
#define SENSOR_H

#pragma once
#include<Arduino.h>
#include<DHT.h>


#define dustPin A0
#define ledP 5  //3번포트
#define vol_setting 0.65    //영점




class Sensor
{
protected: 
    float voMeasured = 0;  
    float calcVoltage = 0;
    

public:
    float temperature = 0; 
    float humidity = 0;
    float smoothDensity = 0; 
    Sensor();
    ~Sensor();
    float Dust_sensor();

private:

};

#endif