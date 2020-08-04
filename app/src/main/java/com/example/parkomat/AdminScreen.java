package com.example.parkomat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class AdminScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        Button validVehicle = findViewById(R.id.validVehicle);
        Button invalidVehicle = findViewById(R.id.invalidVehicle);
        final TextView titleOfTable = findViewById(R.id.titleOfTable);
        final TableLayout tableWithVehicles = findViewById(R.id.table_layout);

        tableWithVehicles.setStretchAllColumns(true);
        tableWithVehicles.bringToFront();


        validVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableWithVehicles.clearDisappearingChildren();
                titleOfTable.setText("Pojazdy z ważnym biletem.");
                if (!LoginActivity.vehiclesWithValidTicket.isEmpty()) {
                    for (Vehicle vehicle : LoginActivity.vehiclesWithValidTicket) {

                        TableRow newRow = new TableRow(AdminScreen.this);

                        TextView number = new TextView(AdminScreen.this);
                        TextView hour = new TextView(AdminScreen.this);
                        TextView date = new TextView(AdminScreen.this);
                        TextView payment = new TextView(AdminScreen.this);

                        newRow.addView(number);
                        newRow.addView(hour);
                        newRow.addView(date);
                        newRow.addView(payment);

                        tableWithVehicles.addView(newRow);

                        String minutesInText = "";
                        if (vehicle.getMinute() >= 0 && vehicle.getMinute() < 10) {
                            minutesInText = "0" + vehicle.getMinute();
                        } else {
                            minutesInText = String.valueOf(vehicle.getMinute());
                        }

                        number.setText(vehicle.getRegistrationNumber());
                        hour.setText(String.valueOf(vehicle.getHour()) + ":" + minutesInText);
                        date.setText(vehicle.getDate());
                        payment.setText(vehicle.getPayment().toString());
                    }
                }
            }

        });

        invalidVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tableWithVehicles.clearDisappearingChildren();
                titleOfTable.setText("Pojazdy z nieważnym biletem.");
                if (!LoginActivity.vehiclesWithInvalidTicket.isEmpty()) {
                    for (Vehicle vehicle : LoginActivity.vehiclesWithInvalidTicket) {

                        TableRow newRow = new TableRow(AdminScreen.this);

                        TextView number = new TextView(AdminScreen.this);
                        TextView hour = new TextView(AdminScreen.this);
                        TextView date = new TextView(AdminScreen.this);
                        TextView payment = new TextView(AdminScreen.this);

                        newRow.addView(number);
                        newRow.addView(hour);
                        newRow.addView(date);
                        newRow.addView(payment);

                        tableWithVehicles.addView(newRow);

                        String minutesInText = "";
                        if (vehicle.getMinute() >= 0 && vehicle.getMinute() < 10) {
                            minutesInText = "0" + vehicle.getMinute();
                        } else {
                            minutesInText = String.valueOf(vehicle.getMinute());
                        }

                        number.setText(vehicle.getRegistrationNumber());
                        hour.setText(String.valueOf(vehicle.getHour()) + ":" + minutesInText);
                        date.setText(vehicle.getDate());
                        payment.setText(vehicle.getPayment().toString());

                    }
                }
            }
        });

    }

}
