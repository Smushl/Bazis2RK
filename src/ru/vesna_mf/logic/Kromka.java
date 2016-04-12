package ru.vesna_mf.logic;

/**
 * Created by Некрасов on 01.12.2015.
 */
public class Kromka {
    private String name;
    private float thickness;

    public float getThickness() {
        return thickness;
    }

    public String getName() {
        return name;
    }

    public Kromka(String name) {
        this.name = "";
        this.thickness = -1.0f;  //для безымяной кромки (т.е. при отсутствии кромки) ее толщина = -1
        if (!name.isEmpty()){
            try {
                int i = name.indexOf(40);
                int j = name.indexOf("мм");
                this.thickness = Float.parseFloat(name.substring(i+1, j).replace(',', '.'));
                this.name = name.substring(0, i).trim();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
