import pyrebase
import re
import subprocess
import pyudev

config = {
    "apiKey": "AIzaSyDKR0W6uIyA9dyW0oQ6VgWMfKUq3XVQ5_s",
    "authDomain": "omnisense-35e13.firebaseapp.com",
    "databaseURL": "https://omnisense-35e13.firebaseio.com",
    "storageBucket": "omnisense-35e13.appspot.com",
    "serviceAccount": "./omnisense-35e13-firebase-adminsdk-i03nj-648e08d862.json"
}

firebase = pyrebase.initialize_app(config)
db = firebase.database()


def printDevices():
    device_re = re.compile(
        "Bus\s+(?P<bus>\d+)\s+Device\s+(?P<device>\d+).+ID\s(?P<id>\w+:\w+)\s(?P<tag>.+)$", re.I)
    df = subprocess.check_output("lsusb", universal_newlines=True)
    devices = []
    for i in df.split('\n'):
        if i:
            info = device_re.match(i)
            if info:
                dinfo = info.groupdict()
                dinfo['device'] = '/dev/bus/usb/%s/%s' % (
                    dinfo.pop('bus'), dinfo.pop('device'))
                devices.append(dinfo)
    db.child("activeUSB").set(devices)
    

printDevices()

context = pyudev.Context()
monitor = pyudev.Monitor.from_netlink(context)
monitor.filter_by(subsystem='usb')

for device in iter(monitor.poll, None):
    if device.action == 'add' or device.action == 'remove':
        #print(device.action+'{}'.format(device))
        printDevices()