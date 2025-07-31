package com.example.crashdetectionapp.utils

import kotlin.random.Random

object LocationUtils {
    
    /**
     * Generates a simulated location with small random variation
     * around the default coordinates
     */
    fun generateSimulatedLocation(): Pair<Double, Double> {
        val latVariation = (Random.nextDouble() - 0.5) * Constants.LOCATION_VARIATION
        val lngVariation = (Random.nextDouble() - 0.5) * Constants.LOCATION_VARIATION
        
        val latitude = Constants.DEFAULT_LATITUDE + latVariation
        val longitude = Constants.DEFAULT_LONGITUDE + lngVariation
        
        return Pair(latitude, longitude)
    }
    
    /**
     * Formats coordinates for display
     */
    fun formatCoordinates(latitude: Double, longitude: Double): String {
        return "%.6f, %.6f".format(latitude, longitude)
    }
    
    /**
     * Validates if coordinates are within reasonable bounds
     */
    fun isValidLocation(latitude: Double, longitude: Double): Boolean {
        return latitude in -90.0..90.0 && longitude in -180.0..180.0
    }
} 