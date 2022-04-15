

import time

import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish

# Initial the dht device, with data pin connected to:
broker_address="175.211.162.37"  # raspberryPi
broker_port=1883
client = mqtt.Client() #create new instance
print("connecting to broker")
client.connect(host=broker_address, port=broker_port)
print("Subscribing to topic","Iot/LED")

while True:
    try:
        # Print the values to the serial port
        led_state = "on"

        select_time1_hour = "10"
        select_time1_minute = "49"

        select_time2_hour = "10"
        select_time2_minute = "50"
        val = "on"

        client.publish("Iot/LED", val)
        # client.publish("Iot/LED", str(select_time1_hour))
        # client.publish("Iot/LED", str(select_time1_minute))
        # client.publish("Iot/LED", str(select_time2_hour))
        # client.publish("Iot/LED", str(select_time2_minute))

        print(f"Led 상태 ={led_state}")

    except RuntimeError as error:
        # Errors happen fairly often, DHT's are hard to read, just keep going
        print(error.args[0])
        time.sleep(2.0)
        continue
    except Exception as error:
        raise error
    except KeyboardInterrupt:
        print("bye")

    time.sleep(1.0)

