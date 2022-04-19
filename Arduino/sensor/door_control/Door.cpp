#include "Door.h"

Door::Door()
{

}

void Door::open(Servo &servo)
{
    Serial.print("open door");
    servo.write(180);
    delay(5000);
    servo.write(0);
}

