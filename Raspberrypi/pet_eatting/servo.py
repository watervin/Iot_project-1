import RPi.GPIO as GPIO
import time

#서보모터를 PWM으로 제어할 핀 번호 설정
SERVO_PIN = 2


def eat_start():

    GPIO.setmode(GPIO.BCM)
    # 서보핀의 출력 설정
    GPIO.setup(SERVO_PIN, GPIO.OUT)

    # PWM 인스턴스 servo 생성, 주파수 50으로 설정
    servo = GPIO.PWM(SERVO_PIN,50)

    servo.start(0)
    servo.ChangeDutyCycle(4.0) # open
    time.sleep(3)

    servo.ChangeDutyCycle(9.0) # close
    time.sleep(0.3)
    servo.stop()
    GPIO.cleanup()
