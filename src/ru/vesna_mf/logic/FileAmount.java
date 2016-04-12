package ru.vesna_mf.logic;

import java.io.File;

/**
 * Created by Некрасов on 13.11.2015.
 */
public class FileAmount {
    public File file;
    public Integer amount;

    public FileAmount(File file, Integer amount) {
        this.file = file;
        this.amount = amount;
    }
}
