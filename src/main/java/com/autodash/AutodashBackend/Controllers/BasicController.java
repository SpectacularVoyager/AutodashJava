package com.autodash.AutodashBackend.Controllers;

import com.autodash.AutodashBackend.CSV.DictionaryTable;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@RestController
@RequestMapping("java/api/convert/")
public class BasicController {
    @PostMapping("json")
    public ResponseEntity<String> fromJsonStream(@RequestBody List<HashMap<String, String>> list) throws IOException {
        DictionaryTable table = new DictionaryTable();
        for (HashMap<String, String> val : list) {
            table.put(val);
        }
        try {
            File f = JsonModel.writeJSON(table);
            return ResponseEntity.ok(f.getAbsolutePath());
        } catch (IOException e) {
            return ResponseEntity.status(500).body(e.toString());
        }
    }

    List<String> getKeys(MongoDBConn conn) throws IOException {
        MongoClient mongoClient = new MongoClient(conn.getHost(), conn.getPort());
        MongoDatabase db = mongoClient.getDatabase(conn.getDatabase());

        ListCollectionsIterable collections = db.listCollections();

        MongoCursor collectionsCursor = collections.iterator();
        HashSet<String> keys = new HashSet<>();
        while (collectionsCursor.hasNext()) {
            Document collectionDocument = (Document) collectionsCursor.next();

            String name = collectionDocument.getString("name");
            if (!name.equalsIgnoreCase("system.indexes")) {
                MongoCollection collectionTemp = db.getCollection(name);

                boolean collectionFirst = true;
                for (Document collectionElement : (Iterable<Document>) collectionTemp.find()) {

                    boolean first = true;
                    Set<String> keySet = collectionElement.keySet();

                    keys.addAll(keySet);


                }
            }
        }
        return keys.stream().toList();
    }

    @PostMapping("mongodb")
    public ResponseEntity<?> fromMongoDB(@RequestBody MongoDBConn conn) throws IOException {
        MongoClient mongoClient = new MongoClient(conn.getHost(), conn.getPort());
        MongoDatabase db = mongoClient.getDatabase(conn.getDatabase());

        ListCollectionsIterable collections = db.listCollections();

        MongoCursor collectionsCursor = collections.iterator();
        File f = getOut();

        DictionaryTable table = new DictionaryTable();

        while (collectionsCursor.hasNext()) {
            Document collectionDocument = (Document) collectionsCursor.next();

            String name = collectionDocument.getString("name");
            if (!name.equalsIgnoreCase("system.indexes")) {
                MongoCollection collectionTemp = db.getCollection(name);

                boolean collectionFirst = true;
                MongoCursor<Document> cursorDoc = collectionTemp.find().iterator();
                while (cursorDoc.hasNext()) {
                    Document collectionElement = cursorDoc.next();
                    Set<String> keySet = collectionElement.keySet();
                    HashMap<String, String> temp = new LinkedHashMap<>();
                    for (String key : keySet)
                        temp.put(key, collectionElement.get(key).toString());
                    table.put(temp);
                }
            }
        }
        table.flush(new FileOutputStream(f));
        return ResponseEntity.ok(f.getAbsolutePath());
    }


    @PostMapping("json_file")
    public ResponseEntity<?> fromJsonFile(@RequestBody String file) {
        try {
            String json = Files.readString(Path.of(file));
            ObjectMapper objectMapper = new ObjectMapper();
            List<HashMap<String, String>> map = List.of();
            return fromJsonStream(objectMapper.readValue(json, (JavaType) map));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(e);
        }
    }

    public File getOut() throws IOException {
        File f = new File(String.format("%s/%s.%s", "res", UUID.randomUUID(), "csv"));
        if (!f.createNewFile()) {
            throw new IOException("Cannot Create File:" + f);
        }
        return f;
    }
}

@NoArgsConstructor
@Getter
@Setter
class MongoDBConn {
    String host;
    int port;
    String database;
}
