package comp4321;

import java.io.*;

public class FileOutput {
    private final String filename;
    private final FileWriter fwriter;
    FileOutput(String filename) throws IOException {
        File f = new File(filename);
        this.filename = filename;
        fwriter = new FileWriter(filename);
    }

    public void close() throws IOException {
        fwriter.close();
    }

    public int write(String str) throws IOException {
        fwriter.write(str);
        return str.length();
    }

}