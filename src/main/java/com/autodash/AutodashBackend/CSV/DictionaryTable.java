package com.autodash.AutodashBackend.CSV;

import java.io.OutputStream;
import java.util.*;

public class DictionaryTable {
    HashMap<String, Integer> values = new LinkedHashMap<>();
    public List<HashMap<String, String>> data = new ArrayList<>();

    public void put(HashMap<String, String> input) {
        //ADD COLUMN IF NOT EXIST
        for (Map.Entry<String, String> entry : input.entrySet()) {
            if (!values.containsKey(entry.getKey())) {
                values.put(entry.getKey(), values.size());
            }
        }
        data.add(input);
    }

    public void flush(OutputStream out) {
        List<String> headers = getHeaders();
        CSVWriter writer = new CSVWriter(out, headers);
        for (HashMap<String, String> obj : data) {
            String[] arr = new String[headers.size()];
            for (Map.Entry<String, String> entry : obj.entrySet()) {
                arr[values.get(entry.getKey())] = entry.getValue();
            }
            writer.write(arr);
        }
        writer.close();
    }

    private List<String> getHeaders() {
        return values.keySet().stream().toList();
    }

}
