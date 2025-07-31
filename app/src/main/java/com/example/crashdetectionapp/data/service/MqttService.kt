package com.example.crashdetectionapp.data.service

import android.content.Context
import android.util.Log
import com.example.crashdetectionapp.data.model.CrashEvent
import com.example.crashdetectionapp.utils.Constants
import com.google.gson.Gson
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MqttService(private val context: Context) {
    
    private var mqttClient: MqttClient? = null
    private val gson = Gson()
    private val tag = "MqttService"
    
    /**
     * Connects to the MQTT broker
     */
    suspend fun connect(): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val persistence = MemoryPersistence()
            mqttClient = MqttClient(Constants.MQTT_BROKER_URL, Constants.MQTT_CLIENT_ID, persistence)
            
            val options = MqttConnectOptions().apply {
                isCleanSession = true
                connectionTimeout = 10
                keepAliveInterval = 60
            }
            
            mqttClient?.connect(options)
            Log.d(tag, "Connected to MQTT broker: ${Constants.MQTT_BROKER_URL}")
            Result.success(Unit)
            
        } catch (e: Exception) {
            Log.e(tag, "Failed to connect to MQTT broker", e)
            Result.failure(e)
        }
    }
    
    /**
     * Publishes a crash event to the MQTT topic
     */
    suspend fun publishCrashEvent(crashEvent: CrashEvent): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            if (mqttClient?.isConnected != true) {
                return@withContext Result.failure(Exception("MQTT client not connected"))
            }
            
            val payload = gson.toJson(crashEvent)
            val message = MqttMessage(payload.toByteArray())
            
            mqttClient?.publish(Constants.MQTT_TOPIC, message)
            Log.d(tag, "Published crash event: $payload")
            
            Result.success(Unit)
            
        } catch (e: Exception) {
            Log.e(tag, "Failed to publish crash event", e)
            Result.failure(e)
        }
    }
    
    /**
     * Disconnects from the MQTT broker
     */
    fun disconnect() {
        try {
            mqttClient?.disconnect()
            Log.d(tag, "Disconnected from MQTT broker")
        } catch (e: Exception) {
            Log.e(tag, "Error disconnecting from MQTT broker", e)
        }
    }
    
    /**
     * Checks if the MQTT client is connected
     */
    fun isConnected(): Boolean {
        return mqttClient?.isConnected == true
    }
} 