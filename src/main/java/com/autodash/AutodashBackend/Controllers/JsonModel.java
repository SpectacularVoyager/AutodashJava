package com.autodash.AutodashBackend.Controllers;

import com.autodash.AutodashBackend.CSV.DictionaryTable;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class JsonModel {
    public static File writeJSON(DictionaryTable table) throws IOException {
        File f = getOut();
        try (OutputStream os = new FileOutputStream(f)) {
            table.flush(os);
            return f;
        }

    }

    public static File getOut() throws IOException {
        File f = new File(String.format("%s/%s.%s", "res", UUID.randomUUID(), "csv"));
        if (!f.createNewFile()) {
            throw new IOException("Cannot Create File:" + f);
        }
        return f;
    }

}
