package com.example.ble;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    private BluetoothDevice connectedDevice;
    private TextView temperatureValue;
    private TextView humidityValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView connectionStatus = findViewById(R.id.connection_status);
        TextView flagValue = findViewById(R.id.flag_value);
        TextView manufacturerDataValue = findViewById(R.id.manufacturer_data_value);
        TextView temperatureValue = findViewById(R.id.temperature_value);
        TextView humidityValue = findViewById(R.id.humidity_value);
        Button buttonDisconnect = findViewById(R.id.button_disconnect);

        // Get data from the intent
        byte[] scanRecord = getIntent().getByteArrayExtra("SCAN_RECORD");
        if (scanRecord != null) {
            // Parse scanRecord
            int flag = (scanRecord[0] & 0xFF) | ((scanRecord[1] & 0xFF)<<8) | ((scanRecord[2] & 0xFF)<<16);
            String s_flag = String.format("%06x", flag);
            int manufacturerData = (scanRecord[3] & 0xFF) | ((scanRecord[4] & 0xFF)<<8) | ((scanRecord[5] & 0xFF)<<16)| ((scanRecord[6] & 0xFF) << 24);
            String s_manufacturerData = String.format("%08x",manufacturerData);
            int temperature = extractTemperature(scanRecord);
            String s_temperature = Integer.toString(temperature);
            int humidity =  extractHumidity(scanRecord);
            String s_humidity = Integer.toString(humidity);

            // Update UI
            flagValue.setText("Flag: " + s_flag);
            manufacturerDataValue.setText("Manufacturer Data: " + s_manufacturerData.toUpperCase());
            temperatureValue.setText("Temperature: " + s_temperature + "°C");
            humidityValue.setText("Humidity: " + s_humidity + "%");

            connectionStatus.setText("Connected to device with Company ID: " + s_manufacturerData.toUpperCase());
        }
        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectDevice();
            }
        });
    }
    private int extractTemperature(byte[] scanRecord) {
        // Extract temperature from the scanRecord byte array
        // Assuming temperature is at byte index 9 (10th byte)
        return (scanRecord[7] & 0xFF); // Convert signed byte to unsigned value
    }

    private int extractHumidity(byte[] scanRecord) {
        // Extract humidity from the scanRecord byte array
        // Assuming humidity is at byte index 10 (11th byte)
        return (scanRecord[8] & 0xFF);// Convert signed byte to unsigned value
    }


    private void disconnectDevice() {
        if (connectedDevice != null) {
            // Add logic to disconnect from the device if necessary
            // This example assumes the connection is managed elsewhere
            Toast.makeText(this, "Disconnected from device", Toast.LENGTH_SHORT).show();
        }

        // Return to MainActivity
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
