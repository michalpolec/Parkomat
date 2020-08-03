package com.example.parkomat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class MainScreen extends AppCompatActivity {


    Set<Vehicle> vehiclesWithValidTicket = new HashSet<>();
    Set<Vehicle> vehiclesWithInvalidTicket = new HashSet<>();

    static public TimePicker picker;
    AutoCompleteTextView godzinaDo; //godzina
    AutoCompleteTextView oplaty; //tu pole oplaty
    public AutoCompleteTextView nr;

    static public Button pay;
    static public Button print;
    Button anuluj;
    private String currentDate;
    private String Date;

    Logger logger = Logger.getLogger(MainScreen.class.getName());

    MainSQLiteDBHelper dbHelper;
    SQLiteDatabase db;

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
                    Ticket.setCurrentTime(actualTime());
                    Ticket.setCurrentDate(currentDate);
                    Ticket.setDateOfParking(Date);
                    Ticket.setNumber(whichNumber());
                    addVehicle();

                    startActivity(ticket);
                }

            }
        });

        dbHelper = new MainSQLiteDBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        dbHelper.onCreate(db);


    }

    // Funckja odpowiadajaca za przycicisk drukuj, bedzie dodawac pojazdy do listy
    public void addVehicle() {
        String numer = whichNumber();
        String czas = String.valueOf(godzinaDo.getText());

        //teraz rozdzialanie godzin od minut
        String[] time = czas.split(":");
        String hour = time[0];
        String minutes = time[1];

        String cena = String.valueOf(oplaty.getText());

        Vehicle px = new Vehicle(numer, Integer.parseInt(hour), Integer.parseInt(minutes), Double.parseDouble(cena), Date);

        //zapis do bazy
        dbHelper.insertData(db, px);

        //odczyt wszystkich danych z bazy
        List<Vehicle> vehicles = dbHelper.getAllData(db);
        for(Vehicle vehicle : vehicles){
            vehiclesWithValidTicket.add(vehicle);
        }

        checkVehicle();
        deleteVehicle();
        printVehcile();
    }

    private void printVehcile()
    {
        for(Vehicle vehicle : vehiclesWithInvalidTicket){
            System.out.println(vehicle.getRegistrationNumber());
        }
        for(Vehicle vehicle : vehiclesWithValidTicket){
            System.out.println(vehicle.getRegistrationNumber());
        }
    }

    private void checkVehicle() {

        if (!vehiclesWithValidTicket.isEmpty()) {
            for (Vehicle i : vehiclesWithValidTicket) {

                //sprawdzanie daty
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
                        vehiclesWithValidTicket.remove(i);
                    } else if (minuta < minute) {
                        vehiclesWithInvalidTicket.add(i);
                        vehiclesWithValidTicket.remove(i);
                    }
                }
            }
        }

    }

    private void deleteVehicle(){
        vehiclesWithValidTicket.removeAll(vehiclesWithInvalidTicket);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //to do gory musi byc zawsze zeby dzialalo

        //PrimeThread p = new PrimeThread();


        oplaty = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        godzinaDo = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView3);
        nr = (AutoCompleteTextView) findViewById(R.id.nrText);
        picker = (TimePicker) findViewById(R.id.timePicker1);

        picker.setIs24HourView(true);
        picker.setHour(0);
        picker.setMinute(0);

        try {
            init();
        } catch (Exception e) {
//            System.out.println("Bład podczas dokonywania opłaty");
            e.printStackTrace();
        }

        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                godzinaDo.setText(ileCzasu());
                oplaty.setText(howMuchToPay());

            }
        });
        //uruchomienie watku
        //p.start();
    }

    //bedzie ustawiac czas, do ktorego jest oplacone parkowanie
    @RequiresApi(api = Build.VERSION_CODES.M)
    private String ileCzasu() {
        String napis;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);

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

    private String actualTime() {

        String czas;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);

        if (hour < 10 && minutes < 10) {
            czas = "0" + hour + ":0" + minutes;
        } else if (hour < 10) {
            czas = "0" + hour + ":" + minutes;
        } else if (minutes < 10) {
            czas = hour + ":0" + minutes;
        } else czas = hour + ":" + minutes;

        return czas;
    }

    //tu bedzie ustawialo ile trzeba zaplacic zaleznie od godziny
    @RequiresApi(api = Build.VERSION_CODES.M)
    private String howMuchToPay() {
        String cena;
        int godzina = picker.getHour();
        int minuty = picker.getMinute();
        if (godzina == 0) {
            if (minuty <= 30) {
                cena = "1";
            } else cena = "2";
        } else if (godzina == 1) {
            if (minuty > 0) {
                cena = "6.60";
            } else cena = "3";
        } else if (godzina == 2) {
            if (minuty > 0) {
                cena = "10.80";
            } else cena = "6.60";
        } else if (godzina == 3) {
            if (minuty > 0) {
                cena = "13.80";
            } else cena = "10.80";
        } else {
            int x = 3;
            cena = (x * (godzina - 3)) + 10.80 + "0";
        }
        return cena;
    }

    private String whichNumber() {

        String Numer;
        Numer = String.valueOf(nr.getText());
        Numer = Numer.toUpperCase();
        if (Numer.length() == 0) {
            return "error";
        } else {
            return Numer;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isItAnotherDay() {
        Calendar c = Calendar.getInstance();
        int calendarHour = c.get(Calendar.HOUR_OF_DAY);
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
