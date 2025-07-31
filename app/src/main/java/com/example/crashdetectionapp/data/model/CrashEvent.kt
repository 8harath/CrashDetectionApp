package com.example.crashdetectionapp.data.model

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class CrashEvent(
    @SerializedName("status")
    val status: String = "crash_detected",
    
    @SerializedName("latitude")
    val latitude: Double,
    
    @SerializedName("longitude")
    val longitude: Double,
    
    @SerializedName("timestamp")
    val timestamp: String
) {
    companion object {
        fun createMockEvent(latitude: Double, longitude: Double): CrashEvent {
            val timestamp = Instant.now()
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ISO_8601)
            
            return CrashEvent(
                latitude = latitude,
                longitude = longitude,
                timestamp = timestamp
            )
        }
    }
} 