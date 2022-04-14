
#include "Watersensor.h"
#include "wificontrol.h"
#include "message_check.h"

#define servo_pin 13

WiFiClient espClient;
PubSubClient client(espClient);
Watersensor watersensor(A0);
Servo myservo;

int auto_watering = 0;
int auto_setting = 0;
int end_watering = 0;
int watering_counter = 0;

int timer = 190;
int i = 295;
int value = 0;
char send_value[16]={0x00,};

const char* mqtt_location = "data/sensor";
const char* ssid = "KT_GiGA_2G_Wave2_05F8";
const char* password = "eza3kz1284";
const char* mqtt_server = "175.211.162.37";

void callback(char* topic, byte* payload, unsigned int length) {

  Serial.print("Message arrived [");
  Serial.print(topic);
  Serial.print("] ");
  String msg = "";
  for (int i = 0; i < length; i++) {
    msg +=(char)payload[i];
  }
  Serial.print(msg);
  Serial.println();

  message_check(msg, auto_watering,auto_setting);
  Serial.println("########");
  Serial.print(auto_watering);
  Serial.print(" ");
  Serial.println(auto_setting);
  Serial.println("########");
}


void mqtt_send(){
  client.publish(mqtt_location,send_value);
  Serial.println("mqtt send");
  i=0;
}


void setup() {
  Serial.begin(115200);
  setupWifi(ssid,password);
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);

  myservo.attach(servo_pin);
  myservo.write(watering_close);

}

void loop() {

  if(timer>200)
  {
    if (!client.connected()) {
      reconnect(client);
    }
    
    else{
      if(i>=15){
        sprintf(send_value,"water,%d",value);

        mqtt_send();

      }
    }

    value = watersensor.scan();  
    i = i+1;
    timer = 0;


    //물주기
    if(end_watering == 0 && auto_watering==1 && auto_setting>value){
      watersensor.watering(myservo);
      end_watering = 1;
      
    }
    else if(end_watering==1)
    {
      watering_counter++;

      if(watering_counter>=10)
      {
        watering_counter = 0;
        end_watering = 0;
      }
    }

  }
  timer++;
  client.loop();
  delay(10);

}