package com.autodash.AutodashBackend.CSV;

import lombok.Getter;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CSVReader {
    Scanner in;
    @Getter
    List<String> headers;

    public CSVReader(String path) throws IOException {
        in = new Scanner(new FileReader(path));
        headers = Arrays.stream(in.nextLine().split(",")).toList();
    }

    public String[] read() throws IOException {
        return (in.nextLine().split(","));
    }

    public boolean hasNext() {
        return in.hasNext();
    }

}
