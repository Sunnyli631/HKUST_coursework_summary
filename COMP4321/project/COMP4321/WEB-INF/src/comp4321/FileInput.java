package comp4321;

import java.io.*;

public class FileInput {
    private final String filename;
    private BufferedReader reader;
    FileInput(String filename) throws FileNotFoundException {
        this.filename = filename;
        File f = new File(filename);
        reader = new BufferedReader(new FileReader(f));
    }

    public void close() throws IOException {
        reader.close();
    }

    public String read() {
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
        return sb.toString();
    }
}
