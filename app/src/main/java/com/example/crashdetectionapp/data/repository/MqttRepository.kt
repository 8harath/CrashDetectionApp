package com.example.crashdetectionapp.data.repository

import com.example.crashdetectionapp.data.model.CrashEvent
import com.example.crashdetectionapp.data.service.MqttService
import com.example.crashdetectionapp.utils.LocationUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MqttRepository(private val mqttService: MqttService) {
    
    /**
     * Connects to the MQTT broker
     */
    suspend fun connectToBroker(): Result<Unit> {
        return mqttService.connect()
    }
    
    /**
     * Simulates a crash detection event and publishes it
     */
    suspend fun simulateAndPublishCrashEvent(): Result<CrashEvent> {
        return try {
            // Generate simulated location
            val (latitude, longitude) = LocationUtils.generateSimulatedLocation()
            
            // Create crash event
            val crashEvent = CrashEvent.createMockEvent(latitude, longitude)
            
            // Publish to MQTT
            val publishResult = mqttService.publishCrashEvent(crashEvent)
            
            if (publishResult.isSuccess) {
                Result.success(crashEvent)
            } else {
                Result.failure(publishResult.exceptionOrNull() ?: Exception("Unknown error"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Simulates multiple crash events with delay
     */
    fun simulateMultipleCrashEvents(count: Int, delayMs: Long = 1000L): Flow<CrashEvent> = flow {
        repeat(count) { index ->
            val result = simulateAndPublishCrashEvent()
            if (result.isSuccess) {
                emit(result.getOrNull()!!)
                if (index < count - 1) {
                    delay(delayMs)
                }
            }
        }
    }
    
    /**
     * Checks if connected to MQTT broker
     */
    fun isConnected(): Boolean {
        return mqttService.isConnected()
    }
    
    /**
     * Disconnects from MQTT broker
     */
    fun disconnect() {
        mqttService.disconnect()
    }
} 