# Crash Detection App

A modern Android application that simulates crash detection events and publishes them to an MQTT broker. This app is part of a larger IoT-based crash detection system for RC cars.

## Features

- **MQTT Integration**: Connects to a local MQTT broker to publish crash events
- **Simulated Data**: Generates realistic crash event data with location and timestamps
- **Modern UI**: Clean Material Design interface with real-time status updates
- **Clean Architecture**: MVVM pattern with separation of concerns
- **Error Handling**: Comprehensive error handling and user feedback

## Architecture

The app follows the **MVVM (Model-View-ViewModel)** architecture pattern:

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│     View        │    │    ViewModel     │    │     Model       │
│  (MainActivity) │◄──►│  (MainViewModel) │◄──►│  (Repository)   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                                       │
                                              ┌─────────────────┐
                                              │   MQTT Service  │
                                              └─────────────────┘
```

### Key Components

1. **Data Layer**
   - `CrashEvent`: Data model for crash events
   - `MqttService`: Handles MQTT connection and publishing
   - `MqttRepository`: Abstracts MQTT operations

2. **UI Layer**
   - `MainActivity`: Main UI controller
   - `activity_main.xml`: Modern Material Design layout

3. **Business Logic**
   - `MainViewModel`: Manages UI state and business logic
   - `LocationUtils`: Generates simulated location data
   - `Constants`: App configuration constants

## Setup Instructions

### Prerequisites

1. **Android Studio** (latest version)
2. **Android Device** or **Emulator** (API level 26+)
3. **MQTT Broker** (e.g., Mosquitto) running on your laptop

### MQTT Broker Setup

1. **Install Mosquitto** on your laptop:
   ```bash
   # Windows (using Chocolatey)
   choco install mosquitto
   
   # macOS (using Homebrew)
   brew install mosquitto
   
   # Linux (Ubuntu/Debian)
   sudo apt-get install mosquitto mosquitto-clients
   ```

2. **Configure Mosquitto**:
   - Edit `mosquitto.conf`:
     ```
     listener 1883
     allow_anonymous true
     ```
   - Start the broker:
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

### App Configuration

1. **Update MQTT Broker URL** in `Constants.kt`:
   ```kotlin
   const val MQTT_BROKER_URL = "tcp://YOUR_LAPTOP_IP:1883"
   ```

2. **Update default location** (optional) in `Constants.kt`:
   ```kotlin
   const val DEFAULT_LATITUDE = 40.7128  // Your preferred latitude
   const val DEFAULT_LONGITUDE = -74.0060 // Your preferred longitude
   ```

### Building and Running

1. **Open the project** in Android Studio
2. **Sync Gradle** files
3. **Connect your Android device** or start an emulator
4. **Run the app** by clicking the green play button

## Usage

### Basic Workflow

1. **Launch the app** on your Android device
2. **Click "Connect"** to establish MQTT connection
3. **Wait for "Connected" status** (green indicator)
4. **Click "Simulate Single Crash Event"** to publish one event
5. **Click "Simulate Multiple Events (5)"** to publish multiple events

### Message Format

The app publishes JSON messages to the topic `crash/alerts`:

```json
{
  "status": "crash_detected",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "timestamp": "2024-01-15T10:30:45.123Z"
}
```

### Testing with MQTT Client

You can test the MQTT messages using a subscriber client:

```bash
# Subscribe to crash alerts
mosquitto_sub -h YOUR_LAPTOP_IP -t "crash/alerts" -v
```

## Project Structure

```
app/src/main/java/com/example/crashdetectionapp/
├── data/
│   ├── model/
│   │   └── CrashEvent.kt              # Data model for crash events
│   ├── repository/
│   │   └── MqttRepository.kt          # Repository layer
│   └── service/
│       └── MqttService.kt             # MQTT service implementation
├── ui/
│   └── main/
│       ├── MainActivity.kt            # Main activity
│       └── MainViewModel.kt           # ViewModel for UI logic
├── utils/
│   ├── Constants.kt                   # App constants
│   └── LocationUtils.kt               # Location utilities
└── res/
    ├── layout/
    │   └── activity_main.xml          # Main UI layout
    ├── values/
    │   ├── colors.xml                 # Color definitions
    │   ├── strings.xml                # String resources
    │   └── themes.xml                 # App theme
    └── drawable/
        ├── ic_wifi.xml                # WiFi icon
        ├── ic_wifi_off.xml            # WiFi off icon
        ├── ic_car_crash.xml           # Car crash icon
        └── ic_multiple_events.xml     # Multiple events icon
```

## Dependencies

- **MQTT**: Eclipse Paho MQTT client
- **UI**: Material Design components
- **Architecture**: Android Architecture Components (ViewModel, LiveData)
- **JSON**: Gson for serialization
- **Coroutines**: Kotlin coroutines for async operations

## Future Enhancements

### IoT Integration
- **Bluetooth Integration**: Connect to real sensor devices
- **WiFi Direct**: Direct communication with IoT devices
- **Sensor Fusion**: Combine multiple sensor inputs

### Advanced Features
- **Real-time Location**: Use GPS for actual location data
- **Crash Detection Algorithm**: Implement actual crash detection logic
- **Data Persistence**: Store crash events locally
- **Push Notifications**: Alert users of crash events
- **Analytics Dashboard**: Web interface for crash data

### Code Modularization
- **Dependency Injection**: Use Hilt or Koin
- **Navigation**: Implement Navigation Component
- **Testing**: Add unit and integration tests
- **CI/CD**: Automated build and deployment

## Troubleshooting

### Common Issues

1. **Connection Failed**
   - Check if MQTT broker is running
   - Verify IP address in `Constants.kt`
   - Ensure devices are on same network
   - Check firewall settings

2. **Build Errors**
   - Sync Gradle files
   - Clean and rebuild project
   - Check Android Studio version compatibility

3. **App Crashes**
   - Check logcat for error messages
   - Verify all permissions are granted
   - Ensure proper network connectivity

### Debug Mode

Enable debug logging by checking logcat with tag:
- `MqttService`: MQTT connection and publishing logs
- `MainViewModel`: UI state changes
- `MainActivity`: User interactions

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For questions or issues:
1. Check the troubleshooting section
2. Review the code comments
3. Create an issue on GitHub
4. Contact the development team 