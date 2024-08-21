package com.autodash.AutodashBackend.MySQLStuff;

import com.autodash.AutodashBackend.Charts.GenericChart;
import com.autodash.AutodashBackend.Charts.PieChart;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Chart {
    public GenericChart getChart(JdbcTemplate template, String table, String col, String type) {
        if (type.trim().equalsIgnoreCase("PIE")) {
            //PIE CHART
            List<v> vals;
            String query = String.format("select %s as field,count(%s) from %s group by %s", col, col, table, col);
            vals = template.queryForStream(query, new vmapper()).toList();
            PieChart pc = new PieChart();
            for (v v : vals) {
                pc.add(v.name, v.val);
            }
            return pc;
        } else if (type.trim().equalsIgnoreCase("BAR")) {
            AtomicInteger _min = new AtomicInteger(), _max = new AtomicInteger();
            String q1=String.format("select min(%s) as min,max(%s) as max from %s", col, col, table);
            System.out.println(q1);
            template.query(q1, rs -> {
                _min.set(rs.getInt("min"));
                _max.set(rs.getInt("max"));
            });
            int min = _min.get();
            int max = _max.get();
//            System.out.println(min + "\t" + max);
//            template.query("select floor((%s-?)/?*?) as floor,%s");
        }

        return null;
    }
}

@AllArgsConstructor
class v {
    String name;
    int val;
}

class vmapper implements RowMapper<v> {
    @Override
    public v mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new v(rs.getString(1), rs.getInt(2));
    }
}