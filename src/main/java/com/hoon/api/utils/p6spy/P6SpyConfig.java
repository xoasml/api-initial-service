package com.hoon.api.utils.p6spy;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Stack;
import java.util.function.Predicate;

import static java.util.Arrays.stream;


@Configuration
public class P6SpyConfig implements MessageFormattingStrategy {

    private static final String NEW_LINE = System.lineSeparator();
    private static final String P6SPY_FORMATTER = "P6spyPrettySqlFormatter";
    private static final String PACKAGE = "io.p6spy";
    private static final String CREATE = "create";
    private static final String ALTER = "alter";
    private static final String COMMENT = "comment";

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return sqlFormater(sql, category, getMessage(connectionId, elapsed, getStackBuilder(), url));
    }

    private String sqlFormater(String sql, String category, String message) {
        if (sql.trim().isEmpty()) {
            return "";
        }
        return new StringBuilder()
                .append(NEW_LINE)
                .append(sqlFormater(sql, category))
                .append(message)
                .toString();
    }

    private String sqlFormater(String sql, String category) {
        if (isStatementDDL(sql, category)) {
            return FormatStyle.DDL
                    .getFormatter()
                    .format(sql)
//                    .toUpperCase(Locale.ROOT)
                    .replace("+0900", "")
                    .replaceAll("(?i)\\s+AS\\s+\\w+", "");
        }
        return FormatStyle.BASIC
                .getFormatter()
                .format(sql)
//                .toUpperCase(Locale.ROOT)
                .replace("+0900", "")
                .replaceAll("(?i)\\s+AS\\s+\\w+", "");
    }

    private boolean isStatementDDL(String sql, String category) {
        return isStatement(category) && isDDL(sql.trim().toLowerCase(Locale.ROOT));
    }

    private boolean isStatement(String category) {
        return Category.STATEMENT.getName().equals(category);
    }

    private boolean isDDL(String lowerSql) {
        return lowerSql.startsWith(CREATE) || lowerSql.startsWith(ALTER) || lowerSql.startsWith(COMMENT);
    }

    private String getMessage(int connectionId, long elapsed, StringBuilder callStackBuilder, String url) {
        return new StringBuilder()
                .append(NEW_LINE)
                .append(NEW_LINE)
                .append("\t").append(String.format("Connection ID: %s", connectionId))
                .append(NEW_LINE)
                .append("\t").append(String.format("Execution Time: %s ms", elapsed))
                .append(NEW_LINE)
                .append("\t").append(String.format("URL: %s", url))
                .append(NEW_LINE)
                .append(NEW_LINE)
                .append("\t").append(String.format("Call Stack (number 1 is entry point): %s", callStackBuilder))
                .append(NEW_LINE)
                .toString();
    }

    private StringBuilder getStackBuilder() {
        Stack<String> callStack = new Stack<>();
        stream(new Throwable().getStackTrace())
                .map(StackTraceElement::toString)
                .filter(isExcludeWords())
                .forEach(callStack::push);

        int order = 1;
        StringBuilder callStackBuilder = new StringBuilder();
        while (!callStack.empty()) {
            callStackBuilder.append(MessageFormat.format("{0}\t\t{1}. {2}", NEW_LINE, order++, callStack.pop()));
        }
        return callStackBuilder;
    }

    private Predicate<String> isExcludeWords() {
        return charSequence -> charSequence.startsWith(PACKAGE) && !charSequence.contains(P6SPY_FORMATTER);
    }

}
