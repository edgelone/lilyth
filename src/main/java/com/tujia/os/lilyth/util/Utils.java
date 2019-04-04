package com.tujia.os.lilyth.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Utils {

    public static void writeToFile(String data, String fileName) throws Exception {
        File file = new File("./" + fileName + ".txt");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(data);
        writer.close();
    }
}
