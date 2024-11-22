package es.ucm.fdi.v3findmyroommate.ui.home;

import java.io.Serializable;

public class Vivienda implements Serializable {

    private String id;
    private String title;
    private double price;
    private String description;
    private String location;
    private int metr;


    public Vivienda (){} // constructor que necesita Firebase
    public Vivienda (String name, double price, String description, String address, int metr){
        this.id = "00";
        this.title = name;
        this.price = price;
        this.description = description;
        this.location = address;
        this.metr = metr;
    }

    // SET
    public void setId(String id){ this.id = id; }
    public void setTitle(String name){ this.title = name; }
    public void setPrice(double price){ this.price = price; }
    public void setDescription(String description){ this.description = description; }
    public void setLocation(String address){ this.location = address; }
    public void setMetr(int metr) {this.metr = metr; }

    // GET
    public String getId () { return this.id;}
    public String getTitle() { return this.title;}
    public Double getPrice() { return this.price;}
    public String getDescription() { return this.description;}
    public String getLocation() { return this.location;}
    public int getMetr() { return this.metr; }
}
