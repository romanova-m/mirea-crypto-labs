package mirea;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper {
    public String read(String filename) {
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void write(String text, String filename) {
        byte[] strToBytes = text.getBytes();
        try {
            Files.write(Paths.get(filename), strToBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
