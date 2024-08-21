package com.autodash.AutodashBackend.Charts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@NoArgsConstructor
public class PieChart implements GenericChart {
    List<PieChartValue> values = new ArrayList<>();

    public void add(String col, Number value) {
        values.add(new PieChartValue(col, value));
    }

    public void compute(int c, List<String[]> data) {
        HashMap<String, Integer> pcmap = new LinkedHashMap<>();

        for (String[] x : data) {
            pcmap.putIfAbsent(x[c], 0);
            pcmap.computeIfPresent(x[c], (k, v) -> v + 1);
        }
        for (Map.Entry<String, Integer> e : pcmap.entrySet()) {
            add(e.getKey(), e.getValue());
        }
    }
}

