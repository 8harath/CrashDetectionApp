# 🚀 Quick Start Guide - Crash Detection App

This guide will walk you through setting up and running the Android MQTT Crash Detection App step by step.

## 📋 Prerequisites

Before you start, make sure you have:

- ✅ **Android Studio** (latest version)
- ✅ **Android Device** or **Emulator** (API level 26+)
- ✅ **Laptop/PC** for running MQTT broker
- ✅ **WiFi Network** (both devices on same network)

## 🎯 Step-by-Step Setup

### Step 1: Set Up MQTT Broker (Laptop/PC)

#### Option A: Windows
```bash
# Install Mosquitto using Chocolatey
choco install mosquitto

# Or download from: https://mosquitto.org/download/
```

#### Option B: macOS
```bash
# Install using Homebrew
brew install mosquitto
```

#### Option C: Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install mosquitto mosquitto-clients
```

### Step 2: Configure and Start MQTT Broker

1. **Create configuration file** (`mosquitto.conf`):
   ```
   listener 1883
   allow_anonymous true
   log_type all
   ```

2. **Start the broker**:
   ```bash
   mosquitto -c mosquitto.conf
   ```

3. **Find your laptop's IP address**:
   ```bash
   # Windows
   ipconfig
   
   # macOS/Linux
   ifconfig
   ```
   Look for your IP address (e.g., `192.168.1.100`)

### Step 3: Open Project in Android Studio

1. **Launch Android Studio**
2. **Open Project**: File → Open → Select `CrashDetectionApp` folder
3. **Wait for Gradle sync** to complete
4. **Connect your Android device** via USB (or start an emulator)

### Step 4: Configure MQTT Connection

1. **Open** `app/src/main/java/com/example/crashdetectionapp/utils/Constants.kt`
2. **Update the broker URL** with your laptop's IP:
   ```kotlin
   const val MQTT_BROKER_URL = "tcp://YOUR_LAPTOP_IP:1883"
   ```
   Example: `"tcp://192.168.1.100:1883"`

### Step 5: Build and Run the App

1. **Click the green ▶️ button** in Android Studio
2. **Select your device** when prompted
3. **Wait for installation** to complete
4. **App will launch** on your device

## 🎮 How to Use the App

### Basic Workflow

1. **Launch the app** on your Android device
2. **Click "Connect"** button
3. **Wait for "Connected" status** (green indicator)
4. **Click "Simulate Single Crash Event"** to publish one event
5. **Click "Simulate Multiple Events (5)"** to publish multiple events

### What You'll See

- **Connection Status**: Shows if connected to MQTT broker
- **Last Published Event**: Displays details of the most recent crash event
- **Status Messages**: Real-time feedback about operations
- **Progress Indicator**: Shows when simulating events

## 📡 Testing the MQTT Messages

### Monitor Messages on Your Laptop

Open a terminal on your laptop and run:
```bash
mosquitto_sub -h localhost -t "crash/alerts" -v
```

This will show you the crash messages in real-time:
```json
{"status":"crash_detected","latitude":40.7128,"longitude":-74.0060,"timestamp":"2024-01-15T10:30:45.123Z"}
```

### Test with Sample Message

You can also test by publishing a message manually:
```bash
mosquitto_pub -h localhost -t "crash/alerts" -m '{"status":"test","latitude":40.7128,"longitude":-74.0060,"timestamp":"2024-01-15T10:30:45.123Z"}'
```

## 🔧 Troubleshooting

### Common Issues and Solutions

#### 1. **Connection Failed**
- ✅ Check if MQTT broker is running on laptop
- ✅ Verify IP address in `Constants.kt`
- ✅ Ensure both devices are on same WiFi network
- ✅ Check Windows Firewall settings

#### 2. **Build Errors**
- ✅ Sync Gradle files: File → Sync Project with Gradle Files
- ✅ Clean project: Build → Clean Project
- ✅ Rebuild: Build → Rebuild Project

#### 3. **App Crashes**
- ✅ Check logcat for error messages
- ✅ Verify all permissions are granted
- ✅ Ensure proper network connectivity

#### 4. **No Messages Received**
- ✅ Verify MQTT broker is running
- ✅ Check if subscription topic matches: `crash/alerts`
- ✅ Ensure devices can communicate on port 1883

### Debug Commands

#### Check if Mosquitto is Running
```bash
# Windows
tasklist | findstr mosquitto

# macOS/Linux
ps aux | grep mosquitto
```

#### Check Port 1883
```bash
# Windows
netstat -an | findstr 1883

# macOS/Linux
netstat -an | grep 1883
```

#### Test Local Connection
```bash
mosquitto_sub -h localhost -t "test" -v
```

## 📱 App Features

### What the App Does

1. **MQTT Publisher**: Connects to your local MQTT broker
2. **Simulated Data**: Generates realistic crash event data
3. **Real-time UI**: Shows connection status and event details
4. **Error Handling**: Provides feedback for all operations

### Message Format

The app publishes JSON messages to topic `crash/alerts`:
```json
{
  "status": "crash_detected",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "timestamp": "2024-01-15T10:30:45.123Z"
}
```

## 🎯 Next Steps

Once the app is working:

1. **Test different scenarios** by simulating multiple events
2. **Monitor the MQTT messages** on your laptop
3. **Integrate with real sensors** (future enhancement)
4. **Set up a subscriber application** to process the crash data

## 📞 Getting Help

If you encounter issues:

1. **Check the troubleshooting section** above
2. **Review the main README.md** for detailed documentation
3. **Check Android Studio logcat** for error messages
4. **Verify network connectivity** between devices

## 🎉 Success Indicators

You'll know everything is working when:

- ✅ App connects to MQTT broker (green "Connected" status)
- ✅ Simulating events shows "Published successfully" message
- ✅ MQTT messages appear in your laptop terminal
- ✅ Last event details are displayed in the app

---

**🎯 You're now ready to use your Crash Detection App!**

The app is designed to be the foundation for your IoT crash detection system. Once this basic version is working, you can expand it with real sensor integration, GPS location, and more advanced features. 