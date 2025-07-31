# MQTT Broker Setup Guide

This guide will help you set up an MQTT broker on your laptop to receive crash detection messages from the Android app.

## Option 1: Mosquitto (Recommended)

### Windows Setup

1. **Install Mosquitto**:
   ```bash
   # Using Chocolatey (install Chocolatey first if needed)
   choco install mosquitto
   
   # Or download from: https://mosquitto.org/download/
   ```

2. **Create configuration file** (`mosquitto.conf`):
   ```
   # Basic configuration
   listener 1883
   allow_anonymous true
   
   # Logging
   log_type all
   log_dest file mosquitto.log
   
   # Persistence
   persistence true
   persistence_location ./
   ```

3. **Start Mosquitto**:
   ```bash
   mosquitto -c mosquitto.conf
   ```

### macOS Setup

1. **Install Mosquitto**:
   ```bash
   brew install mosquitto
   ```

2. **Create configuration file** (`mosquitto.conf`):
   ```
   listener 1883
   allow_anonymous true
   log_type all
   ```

3. **Start Mosquitto**:
   ```bash
   mosquitto -c mosquitto.conf
   ```

### Linux Setup (Ubuntu/Debian)

1. **Install Mosquitto**:
   ```bash
   sudo apt update
   sudo apt install mosquitto mosquitto-clients
   ```

2. **Configure Mosquitto**:
   ```bash
   sudo nano /etc/mosquitto/mosquitto.conf
   ```
   
   Add these lines:
   ```
   listener 1883
   allow_anonymous true
   ```

3. **Start Mosquitto**:
   ```bash
   sudo systemctl start mosquitto
   sudo systemctl enable mosquitto
   ```

## Option 2: HiveMQ (Alternative)

1. **Download HiveMQ** from: https://www.hivemq.com/downloads/
2. **Extract and run**:
   ```bash
   cd hivemq-4.x.x
   ./bin/run.sh
   ```
3. **Access web interface** at: http://localhost:8080

## Finding Your Laptop's IP Address

### Windows
```bash
ipconfig
```
Look for "IPv4 Address" under your active network adapter.

### macOS/Linux
```bash
ifconfig
# or
ip addr show
```
Look for "inet" followed by your IP address.

## Testing the MQTT Connection

### Subscribe to Crash Alerts

1. **Open a terminal** on your laptop
2. **Subscribe to the topic**:
   ```bash
   mosquitto_sub -h localhost -t "crash/alerts" -v
   ```

### Test with Sample Message

1. **Open another terminal**
2. **Publish a test message**:
   ```bash
   mosquitto_pub -h localhost -t "crash/alerts" -m '{"status":"test","latitude":40.7128,"longitude":-74.0060,"timestamp":"2024-01-15T10:30:45.123Z"}'
   ```

## Updating the Android App

1. **Open** `app/src/main/java/com/example/crashdetectionapp/utils/Constants.kt`
2. **Replace** the MQTT_BROKER_URL with your laptop's IP:
   ```kotlin
   const val MQTT_BROKER_URL = "tcp://192.168.1.100:1883"  // Your laptop's IP
   ```

## Troubleshooting

### Connection Issues

1. **Check if Mosquitto is running**:
   ```bash
   # Windows
   tasklist | findstr mosquitto
   
   # macOS/Linux
   ps aux | grep mosquitto
   ```

2. **Check if port 1883 is open**:
   ```bash
   # Windows
   netstat -an | findstr 1883
   
   # macOS/Linux
   netstat -an | grep 1883
   ```

3. **Test local connection**:
   ```bash
   mosquitto_sub -h localhost -t "test" -v
   ```

### Firewall Issues

1. **Windows**: Allow Mosquitto through Windows Firewall
2. **macOS**: Allow incoming connections in System Preferences
3. **Linux**: Configure UFW if enabled:
   ```bash
   sudo ufw allow 1883
   ```

### Network Issues

1. **Ensure devices are on same network**
2. **Check router settings** (some routers block local traffic)
3. **Try using laptop's hostname** instead of IP address

## Advanced Configuration

### Security (Optional)

For production use, consider adding authentication:

1. **Create password file**:
   ```bash
   mosquitto_passwd -c password_file username
   ```

2. **Update configuration**:
   ```
   listener 1883
   allow_anonymous false
   password_file password_file
   ```

3. **Update Android app** to include credentials

### SSL/TLS (Optional)

For encrypted communication:

1. **Generate certificates**:
   ```bash
   openssl req -new -x509 -days 365 -extensions v3_ca -keyout ca.key -out ca.crt
   ```

2. **Update configuration**:
   ```
   listener 8883
   cafile ca.crt
   certfile server.crt
   keyfile server.key
   ```

3. **Update Android app** to use `ssl://` instead of `tcp://`

## Monitoring and Logs

### View Mosquitto Logs

```bash
# If logging to file
tail -f mosquitto.log

# If using systemd
sudo journalctl -u mosquitto -f
```

### Web-based MQTT Client

Install a web-based MQTT client for easier monitoring:

1. **MQTT Explorer**: https://mqtt-explorer.com/
2. **HiveMQ Web Client**: http://www.hivemq.com/demos/websocket-client/

## Next Steps

Once your MQTT broker is running:

1. **Update the Android app** with your laptop's IP address
2. **Test the connection** using the Android app
3. **Monitor messages** using a subscriber client
4. **Integrate with your IoT devices** for real sensor data

## Support

If you encounter issues:

1. Check the Mosquitto documentation: https://mosquitto.org/documentation/
2. Review the troubleshooting section above
3. Check system logs for error messages
4. Ensure all network configurations are correct 