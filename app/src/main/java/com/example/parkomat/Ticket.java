package com.example.parkomat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Ticket extends AppCompatActivity {

    TextView textOfDateOfParking;
    TextView textOfTimeOfParking;
    TextView textOfPayment;
    TextView textOfPrintDate;
    TextView textOfPrintTime;
    TextView textOfRegistrationNumber;

    Button buttonOfTheEndOfProgram;

    static String money = "0.00";
    static String time = "00:00";
    static String date = "00.00.0000";
    static String currentTime = "00:00";
    static String currentDate = "00.00.0000";
    static String registrationNumber = "000 0000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilet);
        init();

    }

    private void init()
    {
        textOfDateOfParking = (TextView)findViewById(R.id.DataPostoju);
        textOfDateOfParking.setText(date);

        textOfTimeOfParking = (TextView)findViewById(R.id.CzasPostoju);
        textOfTimeOfParking.setText(time);

        textOfPayment = (TextView)findViewById(R.id.Oplata);
        textOfPayment.setText(money + " PLN");

        textOfPrintDate = (TextView)findViewById(R.id.DataRozp);
        textOfPrintDate.setText(currentDate);

        textOfPrintTime = (TextView)findViewById(R.id.CzasRozp);
        textOfPrintTime.setText(currentTime);

        textOfRegistrationNumber = (TextView)findViewById(R.id.NumerRejestr);
        textOfRegistrationNumber.setText(registrationNumber);


        buttonOfTheEndOfProgram = (Button) findViewById(R.id.TheEnd);
        buttonOfTheEndOfProgram.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Ticket.this);
                builder.setTitle("Odbierz swój bilet");
                builder.setMessage("Dziękujemy za skorzystanie z naszego parkomatu!");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent restart = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(restart);
                    }
                });
                builder.show();


            }
        });
    }

    public static void setMoney(String oplata)
    {
        money = oplata;
    }
    public static void setTime(String czas)
    {
        time = czas;
    }
    public static void setCurrentTime(String aktualnyCzas)
    {
        currentTime = aktualnyCzas;
    }
    public static void setCurrentDate(String aktualnaData)
    {
        currentDate = aktualnaData;
    }
    public static void setDate(String data)
    {
        date = data;
    }
    public static void setNumber(String numer)
    {
        registrationNumber = numer;
    }


}
