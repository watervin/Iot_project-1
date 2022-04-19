import os
file_location = "../data"

def file_open(name):
    with open(f"{file_location}/{name}",'r') as f:
        data = f.read()
    
    return data

def file_write(name,data):
    with open(f"{file_location}/{name}",'w') as f:
        f.write(data)
    

def file_list_print():
    file_list = os.listdir(f"{file_location}/delivery_photo")
    file_list.sort(reverse=True)
    return file_list

def image_byte(name):
    try:
        f = open(f"{file_location}/delivery_photo/{name}","rb")
        fileContent= f.read()
        byteArr = bytearray(fileContent)
        f.close()

        return byteArr

    except Exception as e:
        print("이미지 파일 없음")
        return -1