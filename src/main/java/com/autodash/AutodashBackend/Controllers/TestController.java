package com.autodash.AutodashBackend.Controllers;

import com.autodash.AutodashBackend.CSV.DictionaryTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("java/api/test/")
public class TestController {
    @GetMapping("test")
    public String test() {
        return "Hello World";
    }

    @PostMapping("json")
    public ResponseEntity<?> json(@RequestBody ArrayList<HashMap<String, String>> map) {

        DictionaryTable table = new DictionaryTable();
        map.forEach(table::put);
        table.flush(System.out);

        return ResponseEntity.ok("OK");
    }
}
