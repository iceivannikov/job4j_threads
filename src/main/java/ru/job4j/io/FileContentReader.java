package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

public class FileContentReader {
    private final File file;

    public FileContentReader(File file) {
        this.file = file;
    }

    public String getContent() throws IOException {
        return getContent(c -> true);
    }

    public String getContentWithoutUnicode() throws IOException {
        return getContent(c -> c < 0x80);
    }

    private String getContent(Predicate<Character> filter) throws IOException {
        StringBuilder output;
        try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
            output = new StringBuilder();
            int data;
            while ((data = input.read()) != -1) {
                if (filter.test((char) data)) {
                    output.append((char) data);
                }
            }
        }
        return output.toString();
    }
}
