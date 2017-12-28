import pyrebase

config = {
    "apiKey": "AIzaSyDKR0W6uIyA9dyW0oQ6VgWMfKUq3XVQ5_s",
    "authDomain": "omnisense-35e13.firebaseapp.com",
    "databaseURL": "https://omnisense-35e13.firebaseio.com",
    "storageBucket": "omnisense-35e13.appspot.com",
    "serviceAccount": "omnisense-35e13-firebase-adminsdk-i03nj-648e08d862.json"
}

firebase =  pyrebase.initialize_app(config)

db = firebase.database()
db.child("activeUSB")


