package com.autodash.AutodashBackend.CSV;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class CSVWriter implements Closeable {
    PrintStream out;
    List<String> headers;

    public CSVWriter(OutputStream o, List<String> headers) {
        this.out = new PrintStream(o);
        this.headers = headers;
        write(headers);

    }

    public void write(List<String> values) {

        StringBuffer sb = new StringBuffer();
        values.forEach(x -> sb.append(x != null ? x : "").append(","));
        if (!sb.isEmpty()) {
            out.println(sb.substring(0, sb.length() - 1));
        } else {
            out.println(sb.toString().trim());
        }
    }

    public void write(String[] values) {
        write(Arrays.stream(values).toList());
    }


    @Override
    public void close() {
        out.close();
    }
}
