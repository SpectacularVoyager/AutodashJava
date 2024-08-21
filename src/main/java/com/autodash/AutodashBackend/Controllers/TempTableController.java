package com.autodash.AutodashBackend.Controllers;

import com.autodash.AutodashBackend.Charts.GenericChart;
import com.autodash.AutodashBackend.MySQLStuff.Chart;
import com.autodash.AutodashBackend.MySQLStuff.TempTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("java/api/sql/")
public class TempTableController {
    @Autowired
    JdbcTemplate template;

    @PostMapping("csv")
    public GenericChart procCSV(@RequestBody CreateChartDAO dao) throws IOException {
        TempTable table = new TempTable(template);
        table.createTempTable(dao.getCsv(), dao.getType().equalsIgnoreCase("BAR") ? dao.getColumn() : "");
        table.insertAll();
//        table.selectAll();
        Chart chart = new Chart();
        GenericChart genericChart = chart.getChart(template, table.getTableName(), dao.column, dao.getType());
        table.drop();
        return genericChart;
    }

}

@Getter
@NoArgsConstructor
class CreateChartDAO {
    String csv;
    String column;
    String type;
}
