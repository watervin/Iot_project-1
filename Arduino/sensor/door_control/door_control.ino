
#include "wificontrol.h"
#include "Door.h"

WiFiClient espClient;
PubSubClient client(espClient);
Servo servo;
Door door;

int servoPin = 4;

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

  if(msg.equals("open"))
  {
      door.open(servo);
  }

}

void setup()
{
	Serial.begin(115200);
    setupWifi(ssid,password);
    client.setServer(mqtt_server, 1883);
    client.setCallback(callback);
    servo.attach(servoPin); 
    servo.write(0);
}

void loop()
{
	if (!client.connected()) {
      reconnect(client);
    }

    client.loop();
}
