package com.flipkart.mdm;

import java.io.*;
import java.util.UUID;

/**
 * Created by setu.poddar on 10/08/17.
 */
public class FileUtils {

    private static final String FILE_DIR = "/tmp/";
    private static final String FILE_EXT = ".jpg";
    private static final String FILE_URL = "file:///tmp/";


    public static String saveFile(InputStream inputStream) throws Exception {
        OutputStream outputStream = null;
        String fileName = UUID.randomUUID() + ".jpg";
        String filePath = FILE_DIR + fileName;
        try {
            outputStream = new FileOutputStream(new File(filePath));
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
        } finally {
            if (inputStream != null)
                inputStream.close();

            if (outputStream != null)
                outputStream.close();

        }
        return FILE_URL + fileName;
    }
}
