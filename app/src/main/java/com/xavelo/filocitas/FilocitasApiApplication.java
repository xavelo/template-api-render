package com.xavelo.filocitas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.xavelo.filocitas.configuration.DataSourceDiagnosticsApplicationListener;

@SpringBootApplication
@EnableScheduling
public class FilocitasApiApplication {

        private static final Logger log = LoggerFactory.getLogger(FilocitasApiApplication.class);

        public static void main(String[] args) {
                SpringApplication application = new SpringApplication(FilocitasApiApplication.class);
                application.addListeners(new DataSourceDiagnosticsApplicationListener());
                if (log.isDebugEnabled()) {
                        log.debug("DataSource diagnostics listener registered for startup logging.");
                }
                application.run(args);
        }

}
