

import time
import board
import adafruit_dht
import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish

# Initial the dht device, with data pin connected to:
dhtDevice = adafruit_dht.DHT11(4)
broker_address="175.211.162.37"  # raspberryPi
broker_port=1883
client = mqtt.Client() #create new instance
print("connecting to broker")
client.connect(host=broker_address, port=broker_port)
print("Subscribing to topic","data/sensor")

while True:
    try:
        # Print the values to the serial port
        temperature_c = dhtDevice.temperature
        temperature_f = temperature_c * (9 / 5) + 32
        humidity = dhtDevice.humidity
        message = f"temperature,{temperature_c},{humidity}"
        
        client.publish("data/sensor", message)
        print(
            "Temp: {:.1f} C    Humidity: {}% ".format(
                temperature_c, humidity
            )
        )

    except RuntimeError as error:
        # Errors happen fairly often, DHT's are hard to read, just keep going
        print(error.args[0])
        time.sleep(2.0)
        continue
    except Exception as error:
        dhtDevice.exit()
        raise error
    except KeyboardInterrupt:
        print("bye")

    time.sleep(30.0)

