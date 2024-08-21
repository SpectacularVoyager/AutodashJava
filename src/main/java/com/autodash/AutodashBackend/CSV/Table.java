package com.autodash.AutodashBackend.CSV;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Table {
    HashMap<String, Integer> map;
    List<String> columns;

    List<List<String>> data = new ArrayList<>();

    public Table(List<String> columns) {
        this.columns = columns;
        map = new LinkedHashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            map.put(columns.get(i), i);
        }
    }

    public void put(List<String> values) {
        data.add(values);
    }

    public void flush(OutputStream out) {
        CSVWriter writer = new CSVWriter(out, columns);
        data.forEach(writer::write);
        writer.close();
    }
}
