# 1 "d:\\workspace\\sensor\\dust_sensor\\dust_sensor.ino"

# 3 "d:\\workspace\\sensor\\dust_sensor\\dust_sensor.ino" 2
# 4 "d:\\workspace\\sensor\\dust_sensor\\dust_sensor.ino" 2

WiFiClient espClient;
PubSubClient client(espClient);
Sensor sensor;

const char* mqtt_location = "data/sensor";
const char* ssid = "KT_GiGA_2G_Wave2_05F8";
const char* password = "eza3kz1284";
const char* mqtt_server = "175.211.162.37";


int i = 0;
float value = 0;
char send_value[16]={0x00,};


void setup()
{
   Serial.begin(115200);
    setupWifi(ssid,password);
    client.setServer(mqtt_server, 1883);
}

void loop()
{
 if (!client.connected()) {
    reconnect(client);
  }

  else{
    if(i>=60){
      sprintf(send_value,"dust,%f",value);

      client.publish(mqtt_location,send_value);
      Serial.println("mqtt send");
      i=0;
    }
  }

  value = sensor.Dust_sensor();

  i++;
  delay(500);
}
