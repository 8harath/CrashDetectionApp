package com.example.crashdetectionapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crashdetectionapp.data.model.CrashEvent
import com.example.crashdetectionapp.data.repository.MqttRepository
import com.example.crashdetectionapp.data.service.MqttService
import com.example.crashdetectionapp.utils.LocationUtils
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    
    private lateinit var mqttService: MqttService
    private lateinit var mqttRepository: MqttRepository
    
    fun initialize(context: android.content.Context) {
        if (!::mqttService.isInitialized) {
            mqttService = MqttService(context)
            mqttRepository = MqttRepository(mqttService)
        }
    }
    
    // LiveData for UI state
    private val _connectionStatus = MutableLiveData<ConnectionStatus>()
    val connectionStatus: LiveData<ConnectionStatus> = _connectionStatus
    
    private val _lastCrashEvent = MutableLiveData<CrashEvent>()
    val lastCrashEvent: LiveData<CrashEvent> = _lastCrashEvent
    
    private val _isSimulating = MutableLiveData<Boolean>()
    val isSimulating: LiveData<Boolean> = _isSimulating
    
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    
    init {
        _connectionStatus.value = ConnectionStatus.DISCONNECTED
        _isSimulating.value = false
    }
    
    /**
     * Connects to the MQTT broker
     */
    fun connectToBroker() {
        viewModelScope.launch {
            _connectionStatus.value = ConnectionStatus.CONNECTING
            _message.value = "Connecting to MQTT broker..."
            
            val result = mqttRepository.connectToBroker()
            
            if (result.isSuccess) {
                _connectionStatus.value = ConnectionStatus.CONNECTED
                _message.value = "Connected to MQTT broker successfully!"
            } else {
                _connectionStatus.value = ConnectionStatus.ERROR
                _message.value = "Failed to connect: ${result.exceptionOrNull()?.message}"
            }
        }
    }
    
    /**
     * Simulates a single crash event
     */
    fun simulateCrashEvent() {
        if (_connectionStatus.value != ConnectionStatus.CONNECTED) {
            _message.value = "Please connect to MQTT broker first"
            return
        }
        
        viewModelScope.launch {
            _isSimulating.value = true
            _message.value = "Simulating crash event..."
            
            val result = mqttRepository.simulateAndPublishCrashEvent()
            
            if (result.isSuccess) {
                _lastCrashEvent.value = result.getOrNull()
                _message.value = "Crash event published successfully!"
            } else {
                _message.value = "Failed to publish crash event: ${result.exceptionOrNull()?.message}"
            }
            
            _isSimulating.value = false
        }
    }
    
    /**
     * Simulates multiple crash events
     */
    fun simulateMultipleCrashEvents(count: Int) {
        if (_connectionStatus.value != ConnectionStatus.CONNECTED) {
            _message.value = "Please connect to MQTT broker first"
            return
        }
        
        viewModelScope.launch {
            _isSimulating.value = true
            _message.value = "Simulating $count crash events..."
            
            mqttRepository.simulateMultipleCrashEvents(count).collect { crashEvent ->
                _lastCrashEvent.value = crashEvent
            }
            
            _message.value = "Completed simulating $count crash events"
            _isSimulating.value = false
        }
    }
    
    /**
     * Disconnects from the MQTT broker
     */
    fun disconnect() {
        mqttRepository.disconnect()
        _connectionStatus.value = ConnectionStatus.DISCONNECTED
        _message.value = "Disconnected from MQTT broker"
    }
    
    /**
     * Formats coordinates for display
     */
    fun formatCoordinates(latitude: Double, longitude: Double): String {
        return LocationUtils.formatCoordinates(latitude, longitude)
    }
    
    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}

enum class ConnectionStatus {
    CONNECTED,
    CONNECTING,
    DISCONNECTED,
    ERROR
} 