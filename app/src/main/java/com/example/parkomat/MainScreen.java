package com.example.parkomat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.logging.Logger;

public class MainScreen extends AppCompatActivity {

    static public TimePicker picker;
    AutoCompleteTextView godzinaDo;
    AutoCompleteTextView oplaty;
    public AutoCompleteTextView nr;

    static public Button pay;
    static public Button print;
    Button anuluj;
    private String currentDate;
    private String Date;

    Logger logger = Logger.getLogger(MainScreen.class.getName());


    public void init() {
        anuluj = findViewById(R.id.cancel);
        anuluj.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                finish();
                Intent restart = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(restart);

            }
        });

        pay = findViewById(R.id.payButton);
        pay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent payment = new Intent(MainScreen.this, Cash.class);
                Cash.setLeftToPayAndRestOfMoney(howMuchToPay());
                whichDate();
                Cash.setDateOfParking(Date);
                Cash.setTimeOfParking(ileCzasu());
                startActivity(payment);

            }
        });

        print = findViewById(R.id.button);
        print.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (whichNumber() == "error") {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainScreen.this);
                    builder.setTitle("Błąd");
                    builder.setMessage("Nie wpisano numeru rejestracji. Wpisz ją!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else {
                    Intent ticket = new Intent(MainScreen.this, Ticket.class);
                    Ticket.setTicketFee(howMuchToPay());
                    Ticket.setTimeOfParking(ileCzasu());
                    Ticket.setCurrentTime(currentTime());
                    Ticket.setCurrentDate(currentDate);
                    Ticket.setDateOfParking(Date);
                    Ticket.setNumber(whichNumber());
                    addVehicle();

                    startActivity(ticket);
                }

            }
        });
    }

    public void addVehicle() {
        String registrationNumber = whichNumber();
        String czas = String.valueOf(godzinaDo.getText());

        String[] time = czas.split(":");
        String hour = time[0];
        String minutes = time[1];

        String cena = String.valueOf(oplaty.getText());

        Vehicle px = new Vehicle(registrationNumber, Integer.parseInt(hour), Integer.parseInt(minutes), Double.parseDouble(cena), Date);

        LoginActivity.dbHelper.insertData(LoginActivity.db, px);


        LoginActivity.checkVehicle();
        LoginActivity.deleteVehicle();
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oplaty = findViewById(R.id.autoCompleteTextView2);
        godzinaDo = findViewById(R.id.autoCompleteTextView3);
        nr = findViewById(R.id.nrText);
        picker = findViewById(R.id.timePicker1);

        picker.setIs24HourView(true);
        picker.setHour(0);
        picker.setMinute(0);

        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                godzinaDo.setText(ileCzasu());
                oplaty.setText(howMuchToPay());

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private String ileCzasu() {
        String napis;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        int godzina = picker.getHour();
        int minuty = picker.getMinute();

        int h, m;
        h = hour + godzina;
        if (h > 23) {
            h = h - 24;
        }
        m = minutes + minuty;
        if (m > 59) {
            h++;
            m = m - 60;
            if (h > 23) {
                h = h - 24;
            }
        }

        if (h < 10 && m < 10) {
            napis = "0" + h + ":0" + m;
        } else if (h < 10) {
            napis = "0" + h + ":" + m;
        } else if (m < 10) {
            napis = h + ":0" + m;
        } else napis = h + ":" + m;

        return napis;
    }

    private String currentTime() {

        String whatTime;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        if (hour < 10 && minutes < 10) {
            whatTime = "0" + hour + ":0" + minutes;
        } else if (hour < 10) {
            whatTime = "0" + hour + ":" + minutes;
        } else if (minutes < 10) {
            whatTime = hour + ":0" + minutes;
        } else whatTime = hour + ":" + minutes;

        return whatTime;
    }

    
    @RequiresApi(api = Build.VERSION_CODES.M)
    private String howMuchToPay() {
        String whatPrice;
        int hour = picker.getHour();
        int minutes = picker.getMinute();
        if (hour == 0) {
            if (minutes <= 30) {
                whatPrice = "1";
            } else whatPrice = "2";
        } else if (hour == 1) {
            if (minutes > 0) {
                whatPrice = "6.60";
            } else whatPrice = "3";
        } else if (hour == 2) {
            if (minutes > 0) {
                whatPrice = "10.80";
            } else whatPrice = "6.60";
        } else if (hour == 3) {
            if (minutes > 0) {
                whatPrice = "13.80";
            } else whatPrice = "10.80";
        } else {
            int x = 3;
            whatPrice = (x * (hour - 3)) + 10.80 + "0";
        }
        return whatPrice;
    }

    private String whichNumber() {

        String registrationNumber;
        registrationNumber = String.valueOf(nr.getText());
        registrationNumber = registrationNumber.toUpperCase();
        if (registrationNumber.length() == 0) {
            return "error";
        } else {
            return registrationNumber;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isItAnotherDay() {
        Calendar calendar = Calendar.getInstance();
        int calendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        int pickerHour = picker.getHour();
        int day = (calendarHour + pickerHour) / 24;

        return day == 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void whichDate() {

        String data;

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        if (day < 10 && month < 10) {
            data = "0" + day + ".0" + month + "." + year;
        } else if (day < 10) {
            data = "0" + day + "." + month + "." + year;
        } else if (month < 10) {
            data = day + "." + month + "." + year;
        } else {
            data = day + "." + month + "." + year;
        }

        currentDate = data;

        if (isItAnotherDay()) {
            day++;
            if (day < 10 && month < 10) {
                data = "0" + day + ".0" + month + "." + year;
            } else if (day < 10) {
                data = "0" + day + "." + month + "." + year;
            } else if (month < 10) {
                data = day + "." + month + "." + year;
            } else {
                data = day + "." + month + "." + year;
            }
            Date = data;
        } else {
            Date = data;
        }

    }

    public static void enablePrintButton(boolean isOplata) {
        print.setEnabled(isOplata);
        picker.setEnabled(false);
    }
}
