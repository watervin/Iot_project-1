
file_location = "../data"

def file_open(name):
    with open(f"{file_location}/{name}",'r') as f:
        data = f.read()
    
    return data

def file_write(name,data):
    with open(f"{file_location}/{name}",'w') as f:
        f.write(data)
    
