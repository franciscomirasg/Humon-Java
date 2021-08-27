package es.shadowgunther.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringJoiner;

public class CSVWriter {
    private BufferedWriter bufferedWriter;
    private File reference;
    private boolean isOpen;
    private String rowSeperator = ",";
    private String delimiter = "\"";

    public CSVWriter(){};

    public CSVWriter(File file) {
        this.reference = file;
        isOpen = false;
    }

    public CSVWriter(String file) {
        this(new File(file));
    }

    public File getFile() {
        return reference;
    }

    public void setFile(File file) throws IllegalStateException
    {
        if (isOpen) throw new IllegalStateException("No se puede cambiar el fichero con el escritor abierto.");
        this.reference = file;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public String getRowSeperator() {
        return rowSeperator;
    }

    public void setRowSeperator(String rowSeperator) {
        isOpen = true;
        this.rowSeperator = rowSeperator;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        isOpen = true;
        this.delimiter = delimiter;
    }

    public void start() throws IOException {
        isOpen = true;
        //Existe el fichero
        if (!reference.exists()) {
            boolean result = true;
            try {
                File dir_path = reference.getParentFile();
                result = dir_path.mkdirs();
                result = result && reference.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            if (!result) {
                throw new IOException("No se ha podido crear el fichero");
            }
        }

        if (reference.isDirectory())
        {
            throw new IOException("Se esperaba un fichero");
        }

        if (reference.canWrite())
        {
            throw new IOException("No se puede leer el fichero");
        }

        bufferedWriter = new BufferedWriter(new FileWriter(reference, false));
        isOpen = true;
    }

    public void close() {
        if(isOpen)
        {
            try {
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                bufferedWriter = null;
                isOpen = false;
            }
        }
    }

    public void write(String ... head)
    {
        if(! isOpen) return;
        StringJoiner joiner = new StringJoiner(rowSeperator, delimiter, delimiter);
        Arrays.stream(head).forEach(joiner::add);
        try {
            bufferedWriter.write(joiner.toString());
            bufferedWriter.newLine();
        } catch (IOException e)
        {
            e.printStackTrace();
            isOpen = false;
        }
    }
}
