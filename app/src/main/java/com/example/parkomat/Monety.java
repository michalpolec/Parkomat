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

public class Monety extends AppCompatActivity {

    // Opłata za parking
    AutoCompleteTextView oplata;
    TextView Data;
    TextView Czas;

    Button DZIgr;
    Button DWADgr;
    Button PIEgr;
    Button JEDpln;
    Button DWApln;
    Button PIECpln;
    Button DZIpln;
    Button DWADpln;
    Button PIEpln;
    Button reszta;
    Button Anuluj;

    static String calkowitaOplata;
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
        oplata = (AutoCompleteTextView)findViewById(R.id.kosztPostoju);
        oplata.setText(money);

        Data = (TextView)findViewById(R.id.Data);
        Data.setText(date);

        Czas = (TextView)findViewById(R.id.Czas);
        Czas.setText(time);

        init();
    }

    private void init()
    {
            kasaPoczątkowa = money;

            DZIgr = (Button) findViewById(R.id.dziesiecgr);
            DZIgr.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 0.10;
                    setOplata();
                }
            });
            DWADgr = (Button) findViewById(R.id.dwadziesciagr);
            DWADgr.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 0.20;
                    setOplata();
                }
            });
            PIEgr = (Button) findViewById(R.id.piedzesiatgr);
            PIEgr.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 0.50;
                    setOplata();
                }
            });
            JEDpln = (Button) findViewById(R.id.jedenpln);
            JEDpln.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 1.00;
                    setOplata();
                }
            });
            DWApln = (Button) findViewById(R.id.dwapln);
            DWApln.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 2.00;
                    setOplata();
                }
            });
            PIECpln = (Button) findViewById(R.id.piecpln);
            PIECpln.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 5.00;
                    setOplata();
                }
            });
            DZIpln = (Button) findViewById(R.id.dziesiecpln);
            DZIpln.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 10.00;
                    setOplata();
                }
            });
            DWADpln = (Button) findViewById(R.id.dwadziesciapln);
            DWADpln.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 20.00;
                    setOplata();
                }
            });
            PIEpln = (Button) findViewById(R.id.piedziesiatpln);
            PIEpln.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    Wplata = 50.00;
                    setOplata();
                }
            });
            reszta = (Button) findViewById(R.id.change);
            reszta.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    oplata.setText(kasaPoczątkowa);
                }
            });
            Anuluj  = (Button) findViewById(R.id.cancel);
            Anuluj.setOnClickListener(new View.OnClickListener() {
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

            money = String.valueOf(zaokraglanie(zloty) + "0");
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
            money = String.valueOf(zaokraglanie(zloty) + "0");
            oplata.setText(money);
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