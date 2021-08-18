package java.com.shadowgunther.util;

import java.util.Map;

public interface CSVDumper {
    public String[] entryToString();
    public String[] getHeads();
    public boolean hasData();
    public boolean isHead();
    public boolean isStop();
}
