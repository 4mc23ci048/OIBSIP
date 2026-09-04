package com.example.unitconventer;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText inputValue;
    Spinner fromUnit, toUnit;
    Button convertButton;
    TextView resultText;

    String[] units = {"Meter", "Kilometer", "Centimeter", "Mile", "Foot"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        fromUnit = findViewById(R.id.fromUnit);
        toUnit = findViewById(R.id.toUnit);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                units
        );

        fromUnit.setAdapter(adapter);
        toUnit.setAdapter(adapter);

        convertButton.setOnClickListener(v -> convert());
    }

    private void convert() {

        if (inputValue.getText().toString().isEmpty()) {
            inputValue.setError("Enter a value");
            return;
        }

        double value = Double.parseDouble(inputValue.getText().toString());

        String from = fromUnit.getSelectedItem().toString();
        String to = toUnit.getSelectedItem().toString();

        double meters = toMeters(value, from);
        double result = fromMeters(meters, to);

        resultText.setText(String.format("%.2f %s", result, to));
    }

    private double toMeters(double value, String unit) {
        switch (unit) {
            case "Kilometer":
                return value * 1000;
            case "Centimeter":
                return value / 100;
            case "Mile":
                return value * 1609.34;
            case "Foot":
                return value * 0.3048;
            default:
                return value;
        }
    }

    private double fromMeters(double value, String unit) {
        switch (unit) {
            case "Kilometer":
                return value / 1000;
            case "Centimeter":
                return value * 100;
            case "Mile":
                return value / 1609.34;
            case "Foot":
                return value / 0.3048;
            default:
                return value;
        }
    }
}