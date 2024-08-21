package com.autodash.AutodashBackend;

import com.autodash.AutodashBackend.MySQLStuff.TempTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class AutodashBackendApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(AutodashBackendApplication.class, args);
	}

}
