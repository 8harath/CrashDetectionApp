package com.example.crashdetectionapp.utils

object Constants {
    // MQTT Configuration
    const val MQTT_BROKER_URL = "tcp://192.168.1.100:1883" // Update with your laptop's IP
    const val MQTT_CLIENT_ID = "android_crash_detector"
    const val MQTT_TOPIC = "crash/alerts"
    const val MQTT_QOS = 1
    
    // Location simulation bounds (example coordinates - update as needed)
    const val DEFAULT_LATITUDE = 40.7128
    const val DEFAULT_LONGITUDE = -74.0060
    const val LOCATION_VARIATION = 0.001 // Small variation for simulation
    
    // UI Constants
    const val SIMULATION_DELAY_MS = 1000L
    const val MAX_RETRY_ATTEMPTS = 3
} 