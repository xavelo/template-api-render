package com.xavelo.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.xavelo.template.render.api.configuration.DataSourceDiagnosticsApplicationListener;

@SpringBootApplication
@EnableScheduling
public class TemplateApiRenderApplication {

        private static final Logger log = LoggerFactory.getLogger(TemplateApiRenderApplication.class);

        public static void main(String[] args) {
                SpringApplication application = new SpringApplication(TemplateApiRenderApplication.class);
                application.addListeners(new DataSourceDiagnosticsApplicationListener());
                if (log.isDebugEnabled()) {
                        log.debug("DataSource diagnostics listener registered for startup logging.");
                }
                application.run(args);
        }

}
