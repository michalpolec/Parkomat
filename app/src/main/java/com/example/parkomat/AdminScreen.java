package com.example.parkomat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AdminScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        System.out.println("Nieważne");
        for(Vehicle vehicle : LoginActivity.vehiclesWithInvalidTicket){
            System.out.println(vehicle.getRegistrationNumber());
        }
        System.out.println("Ważne");
        for(Vehicle vehicle : LoginActivity.vehiclesWithValidTicket){
            System.out.println(vehicle.getRegistrationNumber());
        }
    }

    private void addEmployee()  {
        String plateNumber;
        String hourOfPark;
        String dateOfPark;
        String paymentForTicket;

    }
}
