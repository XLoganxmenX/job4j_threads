package ru.job4j.io;

import java.io.*;

public class UtilFileWriter {
    private final File file;

    public UtilFileWriter(File file) {
        this.file = file;
    }

    public void saveContent(String content) throws IOException {
        try (BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file))) {
            output.write(content.getBytes());
        }
    }
}
