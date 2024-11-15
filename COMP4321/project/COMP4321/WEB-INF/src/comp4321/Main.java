package comp4321;

import org.htmlparser.util.ParserException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String startURL = "https://www.cse.ust.hk/~kwtleung/COMP4321/testpage.htm";
        int maxPageCount = 300;

        if (args.length >= 1) startURL = args[0];
        if (args.length >= 2) maxPageCount = Integer.parseInt(args[1]);

        try {
            SearchEngine searchEngine = new SearchEngine(startURL, maxPageCount);
            searchEngine.setup(false);
            searchEngine.runTest();
            searchEngine.close1();
            // searchEngine.close2();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}