# Module 2 Final Assignment
This repository contains the final assignment of the Network module.
It contains a application that can serve as a server/client for reliable data transfer using UPD
#Setup the application
**I Only tested the application using the Raspberry Pi's provided by Nedap, running both server and client on the same machine might be problematic**

#Run the application#

Clone the repository.
Create a folder named 'files' in your home directory on both Pi and Computer.
Connect with the Raspberry using Wifi.
Build the project using gradle.
Deploy the application using the gradle deploy function
run the NUM2 jar(present in the Pi's home directory).
run the NUM2 jar on your own machine
The TUI that starts should be self explanatory


#Sidenotes#

Unfortunately the program is very buggy. Restarting using different ports(Due to unhandled Address already in use exeptions) and removing empty files from the file folder is sometimes necessary. Also the TUI can be really unclear sometimes. 
