package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FileSystem {

    private static final String PROPERTIES_FILE = "tests.properties";

    public static String fileSeparator = System.getProperty("file.separator");
    public static String userDir = System.getProperty("user.dir");

    public static String getPropertyFromConfigFile(String propertyName) {
        String configFilePath = userDir + fileSeparator + "config" + fileSeparator + PROPERTIES_FILE;
        FileInputStream fileInputStream;
        Properties property = new Properties();
        try {
            fileInputStream = new FileInputStream(configFilePath);
            property.load(fileInputStream);
            return property.getProperty(propertyName);
        } catch (IOException e) {
            System.err.println("ERROR READING PROPERTIES FILE " + configFilePath);
            System.exit(0);
        }
        return null;
    }

    public static void clearFolder(String folderName) {
        String dir = userDir + fileSeparator + folderName;
        File path = new File(dir);
        if (path.exists() && path.isDirectory()) {
            File[] files = path.listFiles();
            if (files.length != 0) {
                for (int i = 0; i < files.length; ++i) {
                    files[i].delete();
                }
            }
        }
    }

}
