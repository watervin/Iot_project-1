#ifndef WATERSENSOR_H
#define WATERSENSOR_H

#pragma once
#include<Arduino.h>
#include <Servo.h>

#define watering_open 90
#define watering_close -50

class Watersensor
{
    int pin;
    int value;


public:
    Watersensor(int pin);
    int scan();
    int watering(Servo &myservo);


private:

};

#endif