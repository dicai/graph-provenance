package cs265.hf.parse;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Scanner;

/**
 *
 */
public abstract class Parser<T> {

    private final Scanner dataScanner;
    protected T next;
    private boolean scannerOpen;

    public Parser(InputStream data) {
        this.dataScanner = new Scanner(data);
        this.scannerOpen = true;
        populateNext();
    }

    private void populateNext() {
        while (dataScanner.hasNextLine()) {
            String line = dataScanner.nextLine();
            try {
                next = parseT(line);
                return;
            } catch (ParseException ex) {
                //line didn't contain type T
                continue;
            }
        }
        //end of data, hasNext should return false.
        scannerOpen = false;
        dataScanner.close();
    }

    public T getNext() {
        T ret = next;
        populateNext();
        return ret;
    }

    public boolean hasNext() {
        return scannerOpen;
    }

    protected abstract T parseT(String s) throws ParseException;
}
