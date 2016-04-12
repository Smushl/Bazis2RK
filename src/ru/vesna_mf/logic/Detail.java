package ru.vesna_mf.logic;

import javafx.collections.transformation.SortedList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Roman on 11.11.2015.
 */
public class Detail {
    private String name;
    private String length;
    private String width;
    private int amount;
    private String paz = "";
    private String kromka = "";  //L1L2W1W2
    private Kromka kromka1;
    private Kromka kromka2;
    private String texture;
    private String material;

    private String roundSize(String ss){
        if (!ss.contains(",")) return ss;
        try {          //ну так, на всякий случай)
            float f = Float.parseFloat(ss.replace(',', '.'));
            return String.valueOf(Math.round(f));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ss;
        }
    }

    public Detail(String name, String length, String width, int amount, String kromka1Name, String kromka2Name, String material) {
        this.name = name;
        this.length = roundSize(length);  //округление размера
        this.width = roundSize(width);
        this.amount = amount;
        this.kromka1 = new Kromka(kromka1Name);
        this.kromka2 = new Kromka(kromka2Name);
        this.material = material;
    }

    @Override
    public String toString(){        // в принципе не нужен, но пусть будет для тестирования
        return name + " "
                + length + " "
                + width + " "
                + amount + " "
                + texture + " "
                + paz + " "
                + kromka + " "
                + kromka1.getName() + " "
                + kromka2.getName() + " "
                + material;
    }

    public String toKuznecov(int amountOfProducts){
//------------------формарование обозначения кромок для файла раскроя------------

        String kromkaL = "";
        String kromkaW = "";
        String lw1;
        String lw2;
        String k1;
        String k2;

//--баг! работает неправильно, когда одна кромка 19/0,4!!!, так как вторая кромка пустая, а ее толщина ==-1
        if (kromka1.getThickness() < 1.0){
            lw1 = "\\";
            lw2  = "/";
            k1 = kromka2.getName();
            k2 = kromka1.getName();
        }
        else{
            lw1 = "/";
            lw2 = "\\";
            k1 = kromka1.getName();
            k2 = kromka2.getName();
        }

        for (int i = 0; i < kromka.length(); i++) {
            if (kromka.charAt(i) == 'L'){
                switch (kromka.charAt(i+1)) {
                    case '1':
                        kromkaL += lw1;
                        break;
                    case '2':
                        kromkaL += lw2;
                        break;
                }
            }
            if (kromka.charAt(i) == 'W'){
                switch (kromka.charAt(i+1)) {
                    case '1':
                        kromkaW += lw1;
                        break;
                    case '2':
                        kromkaW += lw2;
                        break;
                }
            }
        }

//---------------непосредсвенно создание строки для файла раскроя-----------
// --------------Если МДФЛ - уменьшаем размеры на 1мм
        if (material.contains("МДФЛ")){
            length = String.valueOf(Integer.parseInt(length) - 1);
            width = String.valueOf(Integer.parseInt(width) - 1);
        }

//----------получаем общее количество во всех изделиях---------------
        int amountInRaskroy = amountOfProducts * amount;

        return name + "\t"
                + length + "\t"
                + kromkaL + "\t"
                + width + "\t"
                + kromkaW + "\t"
                + texture + "\t"
                + amountInRaskroy + "\t"
                + material + "\t"
                + "\t"
                + k1 + "\t"
                + k2 + "\n";

    }//----------------------------------------------
    public String curveDetail (int amount){

        if (kromka.contains("*")){
            return this.material + "\t" + "\t" + this.length + "x" + this.width + "\t" + Integer.toString(this.amount*amount);
        }
        else return "";
    }

//    public String getName() {
//        return name;
//    }

    public String getLength() {
        return length;
    }

    public String getWidth() {
        return width;
    }

    public int getAmount() {
        return amount;
    }

    public String getPaz() {
        return paz;
    }

    public String getKromka() {
        return kromka;
    }

    public String getTexture() {
        return texture;
    }

    public String getMaterial() {
        return material;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Detail detail = (Detail) o;

        if (!getLength().equals(detail.getLength())) return false;
        if (!getWidth().equals(detail.getWidth())) return false;
        if (!getPaz().equals(detail.getPaz())) return false;
        if (!getTexture().equals(detail.getTexture())) return false;
        if (!getMaterial().equals(detail.getMaterial())) return false;
        if (getKromka().equals(detail.getKromka())) return true;
        if (getKromka().length() != detail.getKromka().length()) return false;
/*
        Варианты с кромками:
        L1L2 == L2L1
        L1W1W2 == L1W2W1
        L1L2W1 == L2L1W1
        L1L2W1W2 == L1L2W2W1 == L2L1W1W2 == L2L1W2W1

        Решение: если kromka.get(0) != '*' :
        - разбить kromka на пары символов
        - отсортировать
        - сранить
*/
        if (this.kromka.charAt(0) != '*'){
            ArrayList<String > thisKromkaCharPairs = new ArrayList<>();
            ArrayList<String > detailKromkaCharPairs = new ArrayList<>();

            for (int i = 0; i < this.kromka.length(); i+=2) {  //i от 0 до 7
                thisKromkaCharPairs.add(this.kromka.substring(i, i+2));
                detailKromkaCharPairs.add(detail.getKromka().substring(i, i + 2));
            }
            Collections.sort(thisKromkaCharPairs);
            Collections.sort(detailKromkaCharPairs);

            return thisKromkaCharPairs.equals(detailKromkaCharPairs);
        }
        return false;  // хз все ли предусмотрел, но лучше в крайнем случае false вернуть
    }

    public void setPaz(String paz) {
        this.paz = paz;
    }

    public void setKromka(String kromka) {
        if (kromka.contains("*"))
            this.name = "R - " + this.name;
        this.kromka = kromka;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }
}

