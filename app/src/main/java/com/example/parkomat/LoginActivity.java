package com.example.parkomat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class LoginActivity extends AppCompatActivity {

    static public Set<Vehicle> vehiclesWithValidTicket = new HashSet<>();
    static public Set<Vehicle> vehiclesWithInvalidTicket = new HashSet<>();

    static MainSQLiteDBHelper dbHelper;
    static SQLiteDatabase db;

    private String correctLogin = "admin";
    private String correctPassword = "admin";

    private void init(){
        dbHelper = new MainSQLiteDBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final EditText login = findViewById(R.id.loginBlank);
        final EditText password = findViewById(R.id.passwordBlank);
        Button loginButton = findViewById(R.id.loginButton);
        Button continueButton = findViewById(R.id.continueButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String adminLogin = login.getText().toString();
                String adminPassword = password.getText().toString();

                if (adminLogin.equals(correctLogin) && adminPassword.equals(correctPassword)) {
                    checkVehicle();
                    deleteVehicle();
                    login.setText("");
                    password.setText("");
                    Intent Admin = new Intent(LoginActivity.this, AdminScreen.class);
                    startActivity(Admin);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Błąd");
                    builder.setMessage("Błędny login lub hasło");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }

            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent User = new Intent(LoginActivity.this, MainScreen.class);
                startActivity(User);
            }
        });
    }


    public static void checkVehicle() {

        List<Vehicle> vehicles = dbHelper.getAllData(LoginActivity.db);

        vehiclesWithValidTicket.clear();
        vehiclesWithInvalidTicket.clear();

        for(Vehicle vehicle : vehicles){
            vehiclesWithValidTicket.add(vehicle);
        }

        if (!vehiclesWithValidTicket.isEmpty()) {
            for (Vehicle i : vehiclesWithValidTicket) {

                String data = i.getDate();
                String Date = data.replace(".", " ");

                String[] date = Date.split(" ");
                int dzien = Integer.parseInt(date[0]);
                int miesiac = Integer.parseInt(date[1]);
                int rok = Integer.parseInt(date[2]);

                Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                int day = c.get(Calendar.DAY_OF_MONTH);

                if (rok < year) {
                    vehiclesWithInvalidTicket.add(i);
                } else if (miesiac < month) {
                    vehiclesWithInvalidTicket.add(i);
                } else if (dzien < day) {
                    vehiclesWithInvalidTicket.add(i);
                }

                if (rok == year && miesiac == month && dzien == day) {
                    int godzina = i.getHour();
                    int minuta = i.getMinute();

                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);

                    if (godzina < hour) {
                        vehiclesWithInvalidTicket.add(i);
                    }

                    if (godzina == hour && minuta < minute)
                    {
                        vehiclesWithInvalidTicket.add(i);
                    }
                }
            }
        }

    }

    public static void deleteVehicle(){
        vehiclesWithValidTicket.removeAll(vehiclesWithInvalidTicket);
    }
}
