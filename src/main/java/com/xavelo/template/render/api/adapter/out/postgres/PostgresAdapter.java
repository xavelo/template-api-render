package com.xavelo.template.render.api.adapter.out.postgres;

import com.xavelo.template.render.api.application.domain.quote.Quote;
import com.xavelo.template.render.api.port.out.SaveQuotePort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class PostgresAdapter implements SaveQuotePort {

    private static final String INSERT_QUOTE_SQL = "INSERT INTO quotes (author_name, author_wikipedia, work, year, translator, language, text, reference_system, work_part, locator, theme_tags, century, source_url, source_institution, license) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public PostgresAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveQuote(Quote quote) {
        jdbcTemplate.update(INSERT_QUOTE_SQL, preparedStatement -> mapQuote(preparedStatement, quote));
    }

    private void mapQuote(PreparedStatement ps, Quote quote) throws SQLException {
        ps.setString(1, quote.getAuthor().getName());
        ps.setString(2, quote.getAuthor().getWikipediaUrl());
        ps.setString(3, quote.getWork());
        if (quote.getYear() != null) {
            ps.setInt(4, quote.getYear());
        } else {
            ps.setNull(4, java.sql.Types.INTEGER);
        }
        ps.setString(5, quote.getTranslator());
        ps.setString(6, quote.getLanguage());
        ps.setString(7, quote.getText());
        ps.setString(8, quote.getReferenceSystem());
        ps.setString(9, quote.getWorkPart());
        ps.setString(10, quote.getLocator());
        ps.setString(11, joinThemeTags(quote));
        ps.setString(12, quote.getCentury());
        ps.setString(13, quote.getSourceUrl());
        ps.setString(14, quote.getSourceInstitution());
        ps.setString(15, quote.getLicense());
    }

    private String joinThemeTags(Quote quote) {
        return String.join(",", quote.getThemeTags());
    }
}
