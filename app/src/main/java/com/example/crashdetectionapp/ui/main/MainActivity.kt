package com.example.crashdetectionapp.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.crashdetectionapp.R
import com.example.crashdetectionapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        
        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.initialize(this)
        
        // Setup UI observers
        setupObservers()
        
        // Setup click listeners
        setupClickListeners()
    }
    
    private fun setupObservers() {
        // Observe connection status
        viewModel.connectionStatus.observe(this) { status ->
            updateConnectionStatus(status)
        }
        
        // Observe last crash event
        viewModel.lastCrashEvent.observe(this) { crashEvent ->
            updateLastEventDisplay(crashEvent)
        }
        
        // Observe simulation state
        viewModel.isSimulating.observe(this) { isSimulating ->
            updateSimulationState(isSimulating)
        }
        
        // Observe status messages
        viewModel.message.observe(this) { message ->
            updateStatusMessage(message)
        }
    }
    
    private fun setupClickListeners() {
        binding.connectButton.setOnClickListener {
            viewModel.connectToBroker()
        }
        
        binding.disconnectButton.setOnClickListener {
            viewModel.disconnect()
        }
        
        binding.simulateSingleButton.setOnClickListener {
            viewModel.simulateCrashEvent()
        }
        
        binding.simulateMultipleButton.setOnClickListener {
            viewModel.simulateMultipleCrashEvents(5)
        }
    }
    
    private fun updateConnectionStatus(status: ConnectionStatus) {
        val statusText = when (status) {
            ConnectionStatus.CONNECTED -> "Connected"
            ConnectionStatus.CONNECTING -> "Connecting..."
            ConnectionStatus.DISCONNECTED -> "Disconnected"
            ConnectionStatus.ERROR -> "Connection Error"
        }
        
        binding.connectionStatusText.text = statusText
        
        // Update button states
        binding.connectButton.isEnabled = status == ConnectionStatus.DISCONNECTED || status == ConnectionStatus.ERROR
        binding.disconnectButton.isEnabled = status == ConnectionStatus.CONNECTED
        binding.simulateSingleButton.isEnabled = status == ConnectionStatus.CONNECTED
        binding.simulateMultipleButton.isEnabled = status == ConnectionStatus.CONNECTED
        
        // Update status text color
        val colorRes = when (status) {
            ConnectionStatus.CONNECTED -> R.color.success_green
            ConnectionStatus.CONNECTING -> R.color.warning_orange
            ConnectionStatus.DISCONNECTED -> R.color.text_secondary
            ConnectionStatus.ERROR -> R.color.error_red
        }
        binding.connectionStatusText.setTextColor(getColor(colorRes))
    }
    
    private fun updateLastEventDisplay(crashEvent: com.example.crashdetectionapp.data.model.CrashEvent?) {
        if (crashEvent != null) {
            binding.lastEventCard.visibility = View.VISIBLE
            binding.eventStatusText.text = "Status: ${crashEvent.status}"
            binding.eventLocationText.text = "Location: ${viewModel.formatCoordinates(crashEvent.latitude, crashEvent.longitude)}"
            binding.eventTimestampText.text = "Timestamp: ${crashEvent.timestamp}"
        }
    }
    
    private fun updateSimulationState(isSimulating: Boolean) {
        binding.progressIndicator.visibility = if (isSimulating) View.VISIBLE else View.GONE
        
        binding.simulateSingleButton.isEnabled = !isSimulating && viewModel.connectionStatus.value == ConnectionStatus.CONNECTED
        binding.simulateMultipleButton.isEnabled = !isSimulating && viewModel.connectionStatus.value == ConnectionStatus.CONNECTED
    }
    
    private fun updateStatusMessage(message: String?) {
        message?.let {
            binding.statusMessageText.text = it
            
            // Show snackbar for important messages
            if (it.contains("successfully") || it.contains("Failed")) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        viewModel.disconnect()
    }
} 