package ru.vesna_mf.logic;

import org.omg.PortableInterceptor.ServerRequestInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by Некрасов on 13.11.2015.
 */
public class Order {
    private String name;
    private ArrayList<OrderItem> items;
    private HashMap<String, Integer> furniture;
    private List<String> list;  //список мат-лов, для которых текстура не важна
    private File file = new File("NonTexturedMaterials.txt");

    public Order(String name, ArrayList<FileAmount> fileAmounts) {
        //---считывание списка материалов, для которых текстура не важна------------
        try {
            this.list = Files.readAllLines(file.toPath(), Charset.defaultCharset());
//            this.list.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.furniture = new HashMap<>();
        this.items = new ArrayList<>();
        this.name = name;
        for (FileAmount fileAmount : fileAmounts){
            items.add(new OrderItem(new Product(fileAmount.file, list), fileAmount.amount));
        }
    }

    public void makeRaskroyFile(String currentDirectory) {
        ArrayList<String> listOfAllDetailsOfAllProducts = new ArrayList<>();
        for (OrderItem item : items) {
            listOfAllDetailsOfAllProducts.addAll(item.product.toKuznecov(item.amount));
        }
        HashSet<String> materials = new HashSet<>();
        for (String raskroyString : listOfAllDetailsOfAllProducts){
            materials.add(raskroyString.split("\t")[7]);
        }
        for (String s1 : materials) {
            File file = new File(currentDirectory + "\\" + this.name + "_" +s1 +".txt");
            try {
                FileWriter writer = new FileWriter(file);
                for (String s2 : listOfAllDetailsOfAllProducts) {
                    if (s2.split("\t")[7].equals(s1)) {
                        writer.write(s2);
                    }
                }
                writer.close();
            } catch (IOException e) {  //Unsupported characters -> Exception!!!
                e.printStackTrace();
            }

        }
    }

    public void makeCurveDetailFile (String currentDirectory){
        ArrayList<String> curveDetailList = new ArrayList<>();
        for (OrderItem item : items) {
            curveDetailList.addAll(item.product.getCurveDetailList(item.amount));
        }
        File file = new File(currentDirectory + "\\" + this.name + " (R).txt");
        try {
            FileWriter writer = new FileWriter(file);
            for (String s1 : curveDetailList) {
                writer.write(s1);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeFurnitureFile(String currentDirectory){
        for (OrderItem item : items) {
            HashMap<String, Integer> furnitureOfCurrentProduct = item.product.getFurnitureForAmount(item.amount);
            for (Map.Entry<String, Integer> mapEntry : furnitureOfCurrentProduct.entrySet()){
                if (furniture.containsKey(mapEntry.getKey())){  //если такая фигня в списке фурнитуры уже есть
                    int i = furniture.get(mapEntry.getKey());  // получаем количество из общего списка фурнитуры
                    furniture.put(mapEntry.getKey(), mapEntry.getValue() + i);   //плюсуем кол-во фурнитуры
                }
                else
                    furniture.put(mapEntry.getKey(), mapEntry.getValue()); //если нет - просто добавляем
            }
        }

        File file = new File(currentDirectory + "\\" + this.name + " furniture.txt");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write("\r" + "\n" + "\r" + "\n" + this.name + "\r" + "\n" + "----------------------------" + "\r" + "\n" + "\r" + "\n");

            for (Map.Entry<String, Integer> mapEntry : furniture.entrySet()){
                writer.write(mapEntry.getKey() + " -> " + mapEntry.getValue() + "\r" + "\n"+ "\r" + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

