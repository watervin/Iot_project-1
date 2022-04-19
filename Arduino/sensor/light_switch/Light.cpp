#include "Light.h"

Light::Light()
{

}

void Light::on(Servo &servo)
{
    Serial.print("light on");
    servo.write(0);
    delay(1000);
    servo.write(90);
}

void Light::off(Servo &servo)
{
    Serial.print("light off");
    servo.write(180);
    delay(1000);
    servo.write(90);
}
