package com.example.parkomat;

public class Vehicle {
    private String nr_rej;
    private int godzina;
    private int minuta;
    private float oplata;
    private String data;

    public Vehicle(String nr, int h, int min, float cena, String data) {
        this.nr_rej = nr;
        this.godzina = h;
        this.minuta = min;
        this.oplata = cena;
        this.data = data;
    }

    public String getNr_rej() {
        return nr_rej;
    }

    public void setNr_rej(String nr_rej) {
        this.nr_rej = nr_rej;
    }

    public void setOplata(float cena) {
        this.oplata = cena;
    }

    public float getOplata() {
        return oplata;
    }

    public int getGodzina() {
        return godzina;
    }

    public void setGodzina(int godzina) {
        this.godzina = godzina;
    }

    public int getMinuta() {
        return minuta;
    }

    public void setMinuta(int minuta) {
        this.minuta = minuta;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
