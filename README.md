# ✈️ Asia Pacific Airport Simulation

A multi-threaded airport simulation built with Java, featuring an ATC tower, gate management, emergency handling, and a refuel car system.

## 🖥️ Sample Output

```
>> [ATC Thread] ATC TOWER is ONLINE and MONITORING THE QUEUE...
>> [SIMULATOR] Plane-1 ARRIVED AND JOIN THE QUEUE...
>> [ATC] GRANTED LANDING FOR Plane-1. ASSIGNED TO GATE: 1
>> [SIMULATOR] Plane-6 [EMERGENCY] PLACED AT THE FRONT OF THE QUEUE...
>> [ATC] GRANTED LANDING FOR Plane-6 [EMERGENCY]. ASSIGNED TO GATE: 1
...
Total Planes Served: 6 planes
Total Passenger Served: 184 passengers
Average Waiting Time: 2123.67 ms
```

## ✨ Features

- 🗼 ATC Tower running on a dedicated thread monitoring landing queue
- 🚨 Emergency plane priority — jumps to the front of the queue
- 🅿️ Gate management — 3 gates with dynamic assignment
- ⛽ Single refuel car with request/reject system
- 🧹 Concurrent ground services (cleaning, food supply, refueling, boarding)
- 📊 Final report with statistics (total passengers, wait times)

## 🛠️ Tech Stack

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![NetBeans](https://img.shields.io/badge/NetBeans-1B6AC6?style=flat-square&logo=apache-netbeans-ide&logoColor=white)

## 🚀 Getting Started

### Prerequisites
- Java JDK 8+
- NetBeans IDE (or any Java IDE)

### Run in NetBeans
1. Open NetBeans → **File** → **Open Project**
2. Select the project folder
3. Click **Run** or press `F6`

### Run in Terminal
```bash
cd src
javac kaemonng/*.java
java kaemonng.Main
```

## 📚 About

Built as a university assignment to demonstrate multithreading and concurrent programming concepts in Java.
