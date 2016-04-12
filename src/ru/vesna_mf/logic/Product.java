package ru.vesna_mf.logic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Некрасов on 12.11.2015.
 */
public class Product {

    private String name="";
    private List<Detail> details;
    private List<String> listFromFile;
    private Map<String, Integer> furniture;

    public Product(File inputfile, List<String> list) {
//        listFromFile = new ArrayList<>();
        furniture = new HashMap<>();
        details = new ArrayList<>();

        try {
            listFromFile = Files.readAllLines(inputfile.toPath(), Charset.defaultCharset()); //readAllLines возвращает ArrayList<String>
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.name = listFromFile.get(1).split("\t")[1];
            System.out.println("new Product  name: " + this.name);
        } catch (Exception ex) {
            System.out.println("Неверный формат файла!");
        }

        // тут начинается самое интересное - превращение Базисного файлв а список деталей

        try{
            for (int i = 0; i < listFromFile.size(); i++) {
                if (!listFromFile.get(i).contains("Заказ")) {
                    continue;
                }
                //---------------раздел с фурнирурой---------
                if (listFromFile.get(i+2).contains("фурнитур")) {
                    i += 4;
                    while (!listFromFile.get(i).isEmpty()){
                        String furnitureName = listFromFile.get(i).split("\t")[0];
                        Integer furnitureAmount = Integer.parseInt(listFromFile.get(i).split("\t")[1]);
                        furniture.put(furnitureName, furnitureAmount);
                        i++;
                    }
                    break;
                }

                //заполняем переменные Материал, кромка1, кромка2
                String materialName = listFromFile.get(i + 2);
                String kromka1Name = "";
                String kromka2Name = "";
                boolean oblicovka = false;
                boolean paz = false;
                if (listFromFile.get(i + 3).contains("Облицовка[LLWW]")) {
                    oblicovka = true;
                    for (int j = i; j < listFromFile.size(); j++) {
                        if (listFromFile.get(j).equals("")) {
                            if (listFromFile.get(j + 1).split(" - ")[0].equals("1")) {
                                kromka1Name = listFromFile.get(j + 1).split(" - ")[1];
                                if (listFromFile.get(j + 2).split(" - ")[0].equals("2")) {
                                    kromka2Name = listFromFile.get(j + 2).split(" - ")[1];
                                }
                            }
                            break;
                        }
                    }
                }
                //кромки и материал мы теперь знаем
                //проверка наличия пазов
                if (listFromFile.get(i + 3).contains("Паз"))
                    paz = true;

                i += 5;
                do {
                    //наполняем ArrayList<Detail> details;
                    String [] elemets = listFromFile.get(i).split("\t", 7);
                    Detail newDetail = new Detail(elemets[1], elemets[2], elemets[3], Integer.parseInt(elemets[4]), kromka1Name, kromka2Name, materialName);
                    // добавляем пазы и кромку
                    if (paz) {
                        newDetail.setPaz(elemets[5]);
                        if (oblicovka){
                            newDetail.setKromka(elemets[6]);
                        }
                    }
                    else {
                        if (oblicovka) {
                            newDetail.setKromka(elemets[5]);
                        }
                    }
                    //--- проверяем, есть ли материал в списке тех, которым пох не текстуру
                    if (list.indexOf(materialName) == -1) {
                        newDetail.setTexture("*");
                    } else {
                        newDetail.setTexture("");
                    }
                    //---------------------------------------------------------------------
                    if (details.indexOf(newDetail) == -1)
                        details.add(newDetail);
                    else{
                        int k = newDetail.getAmount();
                        int index = details.indexOf(newDetail);
                        details.get(index).setAmount(details.get(index).getAmount()+k);
                    }

//                    System.out.println(details.get(details.size()-1));

                    i++;
                }
                while (!listFromFile.get(i).equals(""));
            }
        }
        catch(Exception ex){
            System.out.println("Что-то пошло не так, видимо ошибка в формате файла" );
            ex.printStackTrace();
        }
    }

    public ArrayList<String> toKuznecov (Integer amount){
        ArrayList<String> result = new ArrayList<>();
        for (Detail detail : details){
            result.add(detail.toKuznecov(amount));
        }
        return  result;
    }

    public ArrayList<String> getCurveDetailList (int amount){
        ArrayList<String> curveDetailList = new ArrayList<>();
        for (Detail detail : details){
            String ss =  detail.curveDetail(amount);
            if (!ss.isEmpty()) {
                    curveDetailList.add(ss + "\t" + this.name + "\n"); //название изделия добавляем только к первой детальке
            }

        }
        return curveDetailList;
    }

    public HashMap<String, Integer> getFurnitureForAmount(int amount) {
        HashMap<String, Integer> furnitureForAmount = new HashMap<>();
        for (Map.Entry<String, Integer> mapEntry : furniture.entrySet()){
            furnitureForAmount.put(mapEntry.getKey(), mapEntry.getValue() * amount);
        }
        return furnitureForAmount;
    }


}
