package com.autodash.AutodashBackend.CSV.Data;

import com.autodash.AutodashBackend.Charts.GenericChart;
import com.autodash.AutodashBackend.Charts.PieChart;

import java.util.*;

public class DataTable {
    List<String> columns;
    LinkedHashMap<String, Integer> map;
    List<String[]> data;
    private float ratio = 0.01f;

    public ColumnData summarise(String col) {
        int c = map.get(col);
        return summarise(c);
    }

    public ColumnData summarise(int c) {

        HashSet<String> set = new LinkedHashSet<>();
        boolean isDate = true;
        boolean isNumeric = true;
        for (String[] x : data) {
            String val = x[c];
            if (val == null) continue;
            if (!val.trim().isBlank()) {
                isNumeric &= isNumeric(val);
                isDate &= isDate(val);
            }
            set.add(val);
        }
        ColumnType columnType = ColumnType.NULL;
        if (isDate) {
            columnType = ColumnType.DATE;
        } else if (isNumeric) {
            columnType = ColumnType.NUMERICAL;
        }
        return new ColumnData(columns.get(c), set.size(), columnType);
    }

    boolean isDate(String data) {
        return false;
    }

    boolean isNumeric(String data) {
        try {
            Double.parseDouble(data);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public GenericChart getChart(ColumnData columnData) {
        if (isOrdinal(columnData)) {
            //PIE CHART
            PieChart pc = new PieChart();
            int c = map.get(columnData.name);
            pc.compute(c, data);
            return pc;
        } else if (columnData.type == ColumnType.NUMERICAL) {
            //NORMAL DISTRIBUTION
            //BAR GRAPH
        }
        return null;
    }

    public ChartDescription getChartDetails(ColumnData[] columnData) {
        List<ColumnData> timeConstraints = new ArrayList<>();
        List<ColumnData> linearConstraints = new ArrayList<>();
        List<ColumnData> ordinalConstraints = new ArrayList<>();
        for (ColumnData c : columnData) {
            if (isOrdinal(c)) {
                ordinalConstraints.add(c);
            } else if (c.type == ColumnType.NUMERICAL) {
                linearConstraints.add(c);
            } else if (c.type == ColumnType.DATE) {
                timeConstraints.add(c);
            }
        }
        return new ChartDescription(columns, timeConstraints, linearConstraints, ordinalConstraints);
    }

    private boolean isOrdinal(ColumnData columnData) {
        return (columnData.unique / ((float) data.size()) < ratio);
    }

    //TRY WITH ML
    public void classifyOrdinal() {
        int len = columns.size();
        int size = data.size();
        ColumnData[] columnData = new ColumnData[len];
        for (int i = 0; i < len; i++) {
            columnData[i] = summarise(i);
        }
        for (int i = 0; i < len; i++) {
            if (columnData[i].unique / ((float) data.size()) < ratio) {
                //ORDINAL
            } else {
                //NUMERICAL
            }
        }
    }
}
