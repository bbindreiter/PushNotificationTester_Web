# Push Notification Tester Web

## How to get this thing running

Register a new Firebase Project on firebase.google.com. On Settings of your newly generated project you'll find a Cloud Messaging tab. Copy the Server Key from there and add it in [NotificationTaskServlet](/src/main/java/com/firstrowria/pushnotificationtester/task/NotificationTaskServlet.java) 

Then deploy the server part (e.g. Google App Engine, Amazon AWS or your own infrastructure) and head over to the client (https://github.com/berndfinal/PushNotificationTester_App)
