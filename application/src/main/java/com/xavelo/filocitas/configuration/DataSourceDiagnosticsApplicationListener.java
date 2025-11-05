package com.xavelo.filocitas.configuration;

import java.sql.SQLException;

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * Logs helpful diagnostics for datasource connectivity on platforms where shell
 * access is unavailable (for example, Render free instances). The listener
 * surfaces the effective JDBC configuration and extracts SQL error details when
 * the application fails before the context is fully initialized.
 */
public class DataSourceDiagnosticsApplicationListener implements ApplicationListener<ApplicationEvent> {

        private static final Logger log = LoggerFactory.getLogger(DataSourceDiagnosticsApplicationListener.class);

        @Override
        public void onApplicationEvent(ApplicationEvent event) {
                if (event instanceof ApplicationEnvironmentPreparedEvent environmentPreparedEvent) {
                        logEnvironmentDiagnostics(environmentPreparedEvent.getEnvironment());
                } else if (event instanceof ApplicationFailedEvent failedEvent) {
                        logFailureDiagnostics(failedEvent.getException());
                }
        }

        private void logEnvironmentDiagnostics(Environment environment) {
                String jdbcUrl = firstNonBlank(
                                environment.getProperty("spring.datasource.url"),
                                environment.getProperty("DB_URL"));
                String driverClassName = firstNonBlank(
                                environment.getProperty("spring.datasource.driver-class-name"),
                                environment.getProperty("DB_DRIVER"));
                String username = firstNonBlank(
                                environment.getProperty("spring.datasource.username"),
                                environment.getProperty("DB_USER"));
                boolean passwordPresent = StringUtils.hasText(firstNonBlank(
                                environment.getProperty("spring.datasource.password"),
                                environment.getProperty("DB_PASS")));

                log.info("[diagnostics] JDBC URL: {}", sanitizeUrl(jdbcUrl));
                log.info("[diagnostics] JDBC driver: {}", StringUtils.hasText(driverClassName) ? driverClassName : "<not set>");
                log.info("[diagnostics] JDBC user: {}", StringUtils.hasText(username) ? username : "<not set>");
                log.info("[diagnostics] JDBC password configured: {}", passwordPresent);
                if (environment instanceof ConfigurableEnvironment configurableEnvironment) {
                        log.debug("[diagnostics] Active profiles: {}", String.join(", ", configurableEnvironment.getActiveProfiles()));
                }
        }

        private void logFailureDiagnostics(Throwable exception) {
                SQLException sqlException = findSqlException(exception);
                if (sqlException == null) {
                        return;
                }

                log.error("[diagnostics] SQLState: {}", sqlException.getSQLState());
                log.error("[diagnostics] Error code: {}", sqlException.getErrorCode());
                log.error("[diagnostics] Message: {}", sqlException.getMessage());

                if (sqlException instanceof PSQLException psqlException && psqlException.getServerErrorMessage() != null) {
                        log.error("[diagnostics] PostgreSQL detail: {}", psqlException.getServerErrorMessage().toString());
                }
        }

        private SQLException findSqlException(Throwable exception) {
                Throwable cursor = exception;
                while (cursor != null) {
                        if (cursor instanceof SQLException sqlException) {
                                return sqlException;
                        }
                        cursor = cursor.getCause();
                }
                return null;
        }

        private String sanitizeUrl(String jdbcUrl) {
                if (!StringUtils.hasText(jdbcUrl)) {
                        return "<not set>";
                }
                int passwordIndex = jdbcUrl.indexOf("password=");
                if (passwordIndex < 0) {
                        return jdbcUrl;
                }
                int endIndex = jdbcUrl.indexOf('&', passwordIndex);
                if (endIndex < 0) {
                        endIndex = jdbcUrl.length();
                }
                return jdbcUrl.substring(0, passwordIndex) + "password=***" + jdbcUrl.substring(endIndex);
        }

        private String firstNonBlank(String primary, String secondary) {
                return StringUtils.hasText(primary) ? primary : (StringUtils.hasText(secondary) ? secondary : "");
        }
}
