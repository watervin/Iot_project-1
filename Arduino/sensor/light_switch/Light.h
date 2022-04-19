#ifndef LIGHT_H
#define LIGHT_H

#pragma once
#include <Servo.h> 
class Light
{
public:
    Light();
    void on(Servo &servo);
    void off(Servo &servo);

private:

};

#endif