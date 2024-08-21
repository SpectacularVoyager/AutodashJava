package com.autodash.AutodashBackend.CSV.Data;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ChartDescription {
    List<String> columns;
    List<ColumnData> timeConstraints = new ArrayList<>();
    List<ColumnData> linearConstraints = new ArrayList<>();
    List<ColumnData> ordinalConstraints = new ArrayList<>();

}
