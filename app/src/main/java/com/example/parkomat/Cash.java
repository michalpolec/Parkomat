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

    static String leftToPayAndRestOfMoney = "0.00";
    static String timeOfParking = "00:00";
    static String dateOfParking = "00.00.0000";
    static String initialCheckout;
    double moneyThrownIn = 0.0;
    static boolean wasThePaymentMade = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monety);
        viewOfTicketFee = findViewById(R.id.kosztPostoju);
        viewOfTicketFee.setText(leftToPayAndRestOfMoney);

        viewOfDate = findViewById(R.id.Data);
        viewOfDate.setText(dateOfParking);

        viewOfTime = findViewById(R.id.Czas);
        viewOfTime.setText(timeOfParking);

        init();
    }

    public static void setLeftToPayAndRestOfMoney(String ticketFee)
    {
        leftToPayAndRestOfMoney = ticketFee;
    }

    private void init()
    {
            initialCheckout = leftToPayAndRestOfMoney;

            coinOfTenGroszy = findViewById(R.id.dziesiecgr);
            coinOfTenGroszy.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    moneyThrownIn = 0.10;
                    setAmountToPay();
                }
            });
            coinOfTwentyGroszy = findViewById(R.id.dwadziesciagr);
            coinOfTwentyGroszy.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    moneyThrownIn = 0.20;
                    setAmountToPay();
                }
            });
            coinOfFiftyGroszy = findViewById(R.id.piedzesiatgr);
            coinOfFiftyGroszy.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    moneyThrownIn = 0.50;
                    setAmountToPay();
                }
            });
            coinOfOneZloty = findViewById(R.id.jedenpln);
            coinOfOneZloty.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    moneyThrownIn = 1.00;
                    setAmountToPay();
                }
            });
            coinOfTwoZloty = findViewById(R.id.dwapln);
            coinOfTwoZloty.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    moneyThrownIn = 2.00;
                    setAmountToPay();
                }
            });
            coinOfFiveZloty = findViewById(R.id.piecpln);
            coinOfFiveZloty.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    moneyThrownIn = 5.00;
                    setAmountToPay();
                }
            });
            banknoteOfTenZloty = findViewById(R.id.dziesiecpln);
            banknoteOfTenZloty.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    moneyThrownIn = 10.00;
                    setAmountToPay();
                }
            });
            banknoteOfTwentyZloty = findViewById(R.id.dwadziesciapln);
            banknoteOfTwentyZloty.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    moneyThrownIn = 20.00;
                    setAmountToPay();
                }
            });
            banknoteOfFiftyZloty = findViewById(R.id.piedziesiatpln);
            banknoteOfFiftyZloty.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    moneyThrownIn = 50.00;
                    setAmountToPay();
                }
            });
            buttonOfRefund = findViewById(R.id.change);
            buttonOfRefund.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    viewOfTicketFee.setText(initialCheckout);
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

    private void setAmountToPay()
    {
        double leftToPayAndRest;
        leftToPayAndRest = Double.parseDouble(leftToPayAndRestOfMoney);
        leftToPayAndRest= (roundingNumbers(leftToPayAndRest)) - (roundingNumbers(moneyThrownIn));

        if (leftToPayAndRest <= 0)
        {
            leftToPayAndRest *= -1;
            leftToPayAndRestOfMoney = roundingNumbers(leftToPayAndRest) + "0";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Płatność");
            builder.setMessage("Płatność została dokonana. Reszta: " + leftToPayAndRestOfMoney + " Złotych");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            wasThePaymentMade = true;
            MainScreen.enablePrintButton(wasThePaymentMade);

            builder.show();

        }
        else {
            leftToPayAndRestOfMoney = roundingNumbers(leftToPayAndRest) + "0";
            viewOfTicketFee.setText(leftToPayAndRestOfMoney);
        }
    }

    public static void setTimeOfParking(String czas)
    {
        timeOfParking = czas;
    }
    public static void setDateOfParking(String data)
    {
        dateOfParking = data;
    }

    public double roundingNumbers(double liczba)
    {
        liczba *= 100;
        liczba = Math.round(liczba);
        liczba /= 100;
        return liczba;
    }



}