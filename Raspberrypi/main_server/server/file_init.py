import os.path
from file_control import file_write



def file_init_setting():
    file_location = "../data"

    file_name = [
        "mood_time",
        "dust_sensor",
        "water_sensor",
        "water_setting",
        "temperature_sensor",

    ]

    for file in file_name:
        if os.path.exists(f"{file_location}/{file}")==False:
            if (file == "mood_time"):
                file_write(file,"0,0,0,2,0")
            
            if (file == "dust_sensor"):
                file_write(file,"0")

            if (file == "temperature_sensor"):
                file_write(file,"0,0")
            
            if (file == "water_sensor"):
                file_write(file,"0")

            if (file == "water_setting"):
                file_write(file,"0,10")