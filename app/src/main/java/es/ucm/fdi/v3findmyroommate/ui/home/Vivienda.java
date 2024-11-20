package es.ucm.fdi.v3findmyroommate.ui.home;

import java.io.Serializable;

public class Vivienda implements Serializable {

    private String id;
    private String name;
    private double price;
    private String description;
    private String address;


    public Vivienda (){} // constructor que necesita Firebase
    public Vivienda (String name, double price, String description, String address){
        this.id = "00";
        this.name = name;
        this.price = price;
        this.description = description;
        this.address = address;
    }

    // SET
    public void setId(String id){ this.id = id; }
    public void setName(String name){ this.name = name; }
    public void setPrice(double price){ this.price = price; }
    public void setDescription(String description){ this.description = description; }
    public void setAddress(String address){ this.address = address; }

    // GET
    public String getId () { return this.id;}
    public String getName() { return this.name;}
    public Double getPrice() { return this.price;}
    public String getDescription() { return this.description;}
    public String getAddress() { return this.address;}
}
