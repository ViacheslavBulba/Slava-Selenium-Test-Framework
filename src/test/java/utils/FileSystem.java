package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FileSystem {

    private static final String PROPERTIES_FILE = "tests.properties";

    private static String fileSeparator = System.getProperty("file.separator");
    private static String userDir = System.getProperty("user.dir");

    public static String getPropertyFromFile(String propertyName) {
        FileInputStream fileInputStream;
        Properties property = new Properties();
        try {
            fileInputStream = new FileInputStream(userDir + fileSeparator + "config" + fileSeparator + PROPERTIES_FILE);
            property.load(fileInputStream);
            return property.getProperty(propertyName);
        } catch (IOException e) {
            System.err.println("ERROR READING PROPERTIES FILE " + PROPERTIES_FILE);
            return null;
        }
    }

    public static void clearFolder(String folderName) {
        String dir = userDir + fileSeparator + folderName;
        File path = new File(dir);
        if(path.exists() && path.isDirectory()) {
            File[] files = path.listFiles();
            if(files.length != 0) {
                for(int i = 0; i < files.length; ++i) {
                    files[i].delete();
                }
            }
        }
    }

}
