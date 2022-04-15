#line 1 "d:\\workspace\\sensor\\watersensor\\wificontrol.h"
#include <ESP8266WiFi.h>
#include <PubSubClient.h>


void setupWifi(const char* ssid, const char* password);
void reconnect(PubSubClient &client);
