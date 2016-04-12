package ru.vesna_mf.old;

/**
 * Created by Roman on 17.10.2015.
 */
public class Detail {
    private String number;
    private String name;
    private String lenght;
    private String width;
    private String quantity;
    private String paz;
    private String kromka;
    private String kromka1Name;
    private String kromka2Name;
    private String texture;
    private String material;


    public Detail(String number, String name, String lenght, String width, String quantity, String texture) {
        this.number = number;
        this.name = name;
        this.lenght = lenght;
        this.width = width;
        this.quantity = quantity;
        this.texture = texture;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getLenght() {
        return lenght;
    }

    public String getWidth() {
        return width;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPaz() {
        return paz;
    }

    public String getKromka() {
        return kromka;
    }

    @Override
    public String toString(){
        return number + " " + name + " " + lenght + " " + width + " " + quantity + " "+ texture + " " + paz + " " + kromka;
    }
//    public String toKuznecov(){
//        return  name + "\t" +
//                lenght + "\t" +
//                "\t" +  //кромка по длине
//                width + "\t" +
//                "\t" +   //кромка по ширине
//                texture + "\t" +   //учитывать текстуру
//                quantity + "\t" +
//                material+ "\t" +   //название метериала
//                "\t" +
//                "\t";
//    }

    public String getTexture() {
        return texture;
    }

    public void setPaz(String paz) {
        this.paz = paz;
    }

    public void setKromka(String kromka) {
        this.kromka = kromka;
        if (kromka.contains("*"))
                name = "R - " + name;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setKromka1Name(String kromka1Name) {
        this.kromka1Name = kromka1Name;
    }

    public void setKromka2Name(String kromka2Name) {
        this.kromka2Name = kromka2Name;
    }
}
