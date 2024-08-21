package com.autodash.AutodashBackend.MySQLStuff;

import com.autodash.AutodashBackend.CSV.CSVReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
public class TempTable {
    @Getter
    String tableName;
    CSVReader reader;
    JdbcTemplate template;

    public TempTable(JdbcTemplate template) {
        this.template = template;
    }

    public String createTempTable(String csv_path, String numeric) throws IOException {
        this.reader = new CSVReader(csv_path);

        tableName = UUID.randomUUID().toString().replace("-", "");


        String tableQuery = String.format("CREATE TABLE %s (%s)", tableName,
                Strings.join(reader.getHeaders().stream().map(x -> String.format("%s %s", x,!x.equalsIgnoreCase(numeric)?"varchar(500)":"int")).iterator(), ','));
        System.out.println(tableQuery);
        template.execute(tableQuery);
        return tableName;
    }

    public void insertAll() throws IOException {
        ;
        String cols = Strings.join(reader.getHeaders(), ',');

        String rep = Strings.join(Collections.nCopies(reader.getHeaders().size(), "?"), ',');
        String q = String.format("INSERT INTO %s (%s) values (%s)", tableName, cols, rep);
        List<String[]> data = new ArrayList<>();
        while (reader.hasNext()) {
            String[] _temp = new String[reader.getHeaders().size()];
            String[] _read = reader.read();
            System.arraycopy(_read, 0, _temp, 0, _read.length);
            data.add(_temp);

        }

        template.batchUpdate(q, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int index) throws SQLException {
                String[] v = data.get(index);
                for (int j = 1; j <= reader.getHeaders().size(); j++) {
                    if (v[j - 1] == null) {
                        ps.setString(j, null);
                    } else if (v[j - 1].isBlank()) {
                        ps.setString(j, null);
                    } else {
                        ps.setString(j, v[j - 1]);
                    }
                }
            }

            @Override
            public int getBatchSize() {
                return data.size();
            }
        });
    }

    public void selectAll() {
        log.info("SELECTING");
        template.query("select * from " + tableName, rs -> {
            for (int i = 1; i <= reader.getHeaders().size(); i++) {
                System.out.print(rs.getString(i) + ",");
            }
            System.out.println();
        });
    }

    public void drop() {
        String dropTable = String.format("DROP TABLE %s", tableName);
//        template.execute(dropTable);
    }

}
