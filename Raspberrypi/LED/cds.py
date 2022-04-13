import spidev 
import time

# 딜레이 시간(센서 측정 간격) 
delay = 0.5
# MCP3008 채널 중 센서에 연결한 채널 설정 
pot_channel = 0
# SPI 인스턴스 spi 생성 
spi = spidev.SpiDev()
# SPI 통신 시작하기 
spi.open(0, 0)
#SPI통신 속도 설정 
spi.max_speed_hz = 100000

#0~7 까지 8개의 채널에서 SPI 데이터 읽기 
def readadc(adcnum):
    if adcnum < 0 or adcnum > 7:
        return -1
    r = spi.xfer2([1, 8+adcnum <<4, 0])
    data = ((r[1] & 3) << 8) + r[2]
    return data

while True:
    # readadc 함수로 pot_channel의 SPI 데이터를 읽기 
    pot_value = readadc(pot_channel) 
    voltage = pot_value * 3.3/1024
    


    print("---------------------------") 
    print(f"Reading={pot_value}\tVoltage={voltage:.2f}")

    time.sleep(delay)