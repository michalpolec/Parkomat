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

    TextView viewOfDateOfParking;
    TextView viewOfTimeOfParking;
    TextView viewOfTicketFee;
    TextView viewOfPrintDate;
    TextView viewOfPrintTime;
    TextView viewOfRegistrationNumber;

    Button buttonOfTheEndOfProgram;

    static String ticketFee = "0.00";
    static String timeOfParking = "00:00";
    static String dateOfParking = "00.00.0000";
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
        viewOfDateOfParking = (TextView)findViewById(R.id.DataPostoju);
        viewOfDateOfParking.setText(dateOfParking);

        viewOfTimeOfParking = (TextView)findViewById(R.id.CzasPostoju);
        viewOfTimeOfParking.setText(timeOfParking);

        viewOfTicketFee = (TextView)findViewById(R.id.Oplata);
        viewOfTicketFee.setText(ticketFee + " PLN");

        viewOfPrintDate = (TextView)findViewById(R.id.DataRozp);
        viewOfPrintDate.setText(currentDate);

        viewOfPrintTime = (TextView)findViewById(R.id.CzasRozp);
        viewOfPrintTime.setText(currentTime);

        viewOfRegistrationNumber = (TextView)findViewById(R.id.NumerRejestr);
        viewOfRegistrationNumber.setText(registrationNumber);


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

    public static void setTicketFee(String ticketFee)
    {
        Ticket.ticketFee = ticketFee;
    }
    public static void setTimeOfParking(String timeOfParking)
    {
        Ticket.timeOfParking = timeOfParking;
    }
    public static void setCurrentTime(String currentTime)
    {
        Ticket.currentTime = currentTime;
    }
    public static void setCurrentDate(String currentDate)
    {
        Ticket.currentDate = currentDate;
    }
    public static void setDateOfParking(String dateOfParking)
    {
        Ticket.dateOfParking = dateOfParking;
    }
    public static void setNumber(String registrationNumber)
    {
        Ticket.registrationNumber = registrationNumber;
    }


}
