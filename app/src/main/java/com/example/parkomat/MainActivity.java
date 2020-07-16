package com.example.parkomat;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    Set<Vehicle> pojazdy = new HashSet<>();

    Set<Vehicle> doUsuniecia = new HashSet<>();

    static public TimePicker picker;
    AutoCompleteTextView godzinaDo; //godzina
    AutoCompleteTextView oplaty; //tu pole oplaty
    public AutoCompleteTextView nr;

    static public Button pay;
    static public Button print;
    Button anuluj;
    private String currentDate;
    private String Date;

    MainSQLiteDBHelper dbHelper;

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
                Intent payment = new Intent(MainActivity.this, Cash.class);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Błąd");
                    builder.setMessage("Nie wpisano numeru rejestracji. Wpisz ją!");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                } else {
                    Intent ticket = new Intent(MainActivity.this, Ticket.class);
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

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
        dbHelper.onUpgrade(db, 2, 3);

        ContentValues values = new ContentValues();
        values.put(MainSQLiteDBHelper.VEHICLE_COLUMN_NR, "TOP01EA");
        values.put(MainSQLiteDBHelper.VEHICLE_COLUMN_DATE, "1000-01-01 00:00:00");
        values.put(MainSQLiteDBHelper.VEHICLE_COLUMN_COST, 2.00);

        long newRowId = db.insert(dbHelper.VEHICLE_TABLE_NAME, null, values);
//        String test = "Dodano " + newRowId;

//        String[] projection = {
//                dbHelper.VEHICLE_COLUMN_ID,
//                dbHelper.VEHICLE_COLUMN_NR,
//                dbHelper.VEHICLE_COLUMN_DATE,
//                dbHelper.VEHICLE_COLUMN_COST
//        };

//        String selection = MainSQLiteDBHelper.VEHICLE_COLUMN_ID + " like ?";
//        String[] selectionArgs = {"%" + 1};
//
//        Cursor cursor = db.query(
//                dbHelper.VEHICLE_TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null
//        );

//        Log.d("TAG", "The total cursor count is " + cursor.getCount());
//
//        List itemIds = new ArrayList<>();
//        while(cursor.moveToNext()) {
//            long itemId = cursor.getLong(
//                    cursor.getColumnIndexOrThrow(MainSQLiteDBHelper.VEHICLE_COLUMN_ID));
//            itemIds.add(itemId);
//        }
//        cursor.close();
//
//        for (int i = 0; i<itemIds.size(); i++) {
//            System.out.println("------ " + itemIds.get(i));
//        }
//
//        System.out.println("------------------Test--------------------");


        Cursor res = dbHelper.getData(1);
        String nr = res.getString(res.getColumnIndex(MainSQLiteDBHelper.VEHICLE_COLUMN_NR));
        String date = res.getString(res.getColumnIndex(MainSQLiteDBHelper.VEHICLE_COLUMN_DATE));
        Double cost = res.getDouble(res.getColumnIndex(MainSQLiteDBHelper.VEHICLE_COLUMN_COST));

        System.out.println("Result: " + nr + " " + date + " " + cost);
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

        Vehicle px = new Vehicle(numer, Integer.parseInt(hour), Integer.parseInt(minutes), Float.parseFloat(cena), Date);

        pojazdy.add(px);
        saveToFile();
    }

    //zapisywanie do pliku
    public void saveToFile() {
        String pathFile = "C:/parkomat";
        File plik = new File(pathFile + "/plik.txt");

        if (!pojazdy.isEmpty()) {
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(plik));
                outputStream.writeObject(pojazdy);
                outputStream.close();
            } catch (IOException ex) {
                System.out.println("Wystąpił błąd zapisu");
                ex.printStackTrace();
            }
        }
    }

    public void readFromFile() {
        try {
            FileInputStream os = new FileInputStream("C:\\parkomat\\plik.txt");
            ObjectInputStream inputStream = new ObjectInputStream(os);
            pojazdy = (Set<Vehicle>) inputStream.readObject();
            inputStream.close();
        } catch (Exception ex) {
            System.out.println("Wystąpił błąd odczytu");
            ex.printStackTrace();
        }
    }

    private void checkVehicle() {

        readFromFile();
        if (!pojazdy.isEmpty()) {
            for (Vehicle i : pojazdy) {

                //sprawdzanie daty
                String data = i.getDate();
                String[] date = data.split(".");
                int dzien = Integer.parseInt(date[0]);
                int miesiac = Integer.parseInt(date[1]);
                int rok = Integer.parseInt(date[2]);

                Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                if (rok < year) {
                    doUsuniecia.add(i);
                } else if (miesiac < month) {
                    doUsuniecia.add(i);
                } else if (dzien < day) {
                    doUsuniecia.add(i);
                }

                if (rok == year && miesiac == month && dzien == day) {
                    int godzina = i.getHour();
                    int minuta = i.getMinute();

                    int hour = c.get(Calendar.HOUR);
                    int minute = c.get(Calendar.MINUTE);

                    if (godzina < hour) {
                        doUsuniecia.add(i);
                    } else if (minuta < minute) {
                        doUsuniecia.add(i);
                    }
                }
            }
        }
    }

    private void deleteVehicle() throws IOException {
        pojazdy.removeAll(doUsuniecia);
        saveToFile();
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
            System.out.println("Bład podczas dokonywania opłaty");
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
