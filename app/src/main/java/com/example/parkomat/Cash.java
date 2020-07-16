package com.example.parkomat;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

public class Cash extends AppCompatActivity {

    // Opłata za parking
    AutoCompleteTextView viewOfTicketFee;
    TextView viewOfDate;
    TextView viewOfTime;

    Button coinOfTenGroszy;
    Button coinOfTwentyGroszy;
    Button coinOfFiftyGroszy;
    Button coinOfOneZloty;
    Button coinOfTwoZloty;
    Button coinOfFiveZloty;
    Button banknoteOfTenZloty;
    Button banknoteOfTwentyZloty;
    Button banknoteOfFiftyZloty;
    Button buttonOfRefund;
    Button buttonOfCancelPayment;

    static String money = "0.00";
    static String time = "00:00";
    static String date = "00.00.0000";
    static String kasaPoczątkowa;
    double Wplata = 0.0;
    static boolean oplataDokonana = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monety);
        viewOfTicketFee = findViewById(R.id.kosztPostoju);
        viewOfTicketFee.setText(money);

        viewOfDate = findViewById(R.id.Data);
        viewOfDate.setText(date);

        viewOfTime = findViewById(R.id.Czas);
        viewOfTime.setText(time);

        init();
    }

    private void init()
    {
            kasaPoczątkowa = money;

            coinOfTenGroszy = findViewById(R.id.dziesiecgr);
            coinOfTenGroszy.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 0.10;
                    setOplata();
                }
            });
            coinOfTwentyGroszy = findViewById(R.id.dwadziesciagr);
            coinOfTwentyGroszy.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 0.20;
                    setOplata();
                }
            });
            coinOfFiftyGroszy = findViewById(R.id.piedzesiatgr);
            coinOfFiftyGroszy.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 0.50;
                    setOplata();
                }
            });
            coinOfOneZloty = findViewById(R.id.jedenpln);
            coinOfOneZloty.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 1.00;
                    setOplata();
                }
            });
            coinOfTwoZloty = findViewById(R.id.dwapln);
            coinOfTwoZloty.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 2.00;
                    setOplata();
                }
            });
            coinOfFiveZloty = findViewById(R.id.piecpln);
            coinOfFiveZloty.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 5.00;
                    setOplata();
                }
            });
            banknoteOfTenZloty = findViewById(R.id.dziesiecpln);
            banknoteOfTenZloty.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 10.00;
                    setOplata();
                }
            });
            banknoteOfTwentyZloty = findViewById(R.id.dwadziesciapln);
            banknoteOfTwentyZloty.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 20.00;
                    setOplata();
                }
            });
            banknoteOfFiftyZloty = findViewById(R.id.piedziesiatpln);
            banknoteOfFiftyZloty.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 50.00;
                    setOplata();
                }
            });
            buttonOfRefund = findViewById(R.id.change);
            buttonOfRefund.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    viewOfTicketFee.setText(kasaPoczątkowa);
                }
            });
            buttonOfCancelPayment = findViewById(R.id.cancel);
            buttonOfCancelPayment.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
    }

    public static void setMoney(String oplata)
    {
        money = oplata;
    }

    private void setOplata()
    {

        double zloty;
        zloty = Double.parseDouble(money);
        zloty= (zaokraglanie(zloty)) - (zaokraglanie(Wplata));

        if (zloty <= 0)
        {
            zloty *= -1;

            money = zaokraglanie(zloty) + "0";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Płatność");
            builder.setMessage("Płatność została dokonana. Reszta: " + money + " Złotych");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            oplataDokonana = true;
            MainActivity.enablePrintButton(oplataDokonana);

            builder.show();

        }
        else {
            money = zaokraglanie(zloty) + "0";
            viewOfTicketFee.setText(money);
        }
    }

    public static void setTime(String czas)
    {
        time = czas;
    }
    public static void setDate(String data)
    {
        date = data;
    }

    public double zaokraglanie(double liczba)
    {
        liczba *= 100;
        liczba = Math.round(liczba);
        liczba /= 100;
        return liczba;
    }



}