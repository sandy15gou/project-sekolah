package com.sandy.project.Integration;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Query Logger untuk menampilkan SETIAP QUERY SQL yang dieksekusi ke database.
 *
 * Digunakan untuk debugging dan mendeteksi N+1 problem dengan cara visual.
 * Log akan menampilkan counter yang meningkat setiap ada query baru.
 *
 * @author Sandy
 */
public class QueryLogger implements StatementInspector {

    private static final Logger log = LoggerFactory.getLogger(QueryLogger.class);
    private static final AtomicInteger queryCounter = new AtomicInteger(0);

    /**
     * Reset counter — panggil di @BeforeEach untuk setiap test
     */
    public static void reset() {
        queryCounter.set(0);
    }

    /**
     * Hook yang dipanggil oleh Hibernate sebelum eksekusi query
     */
    @Override
    public String inspect(String sql) {
        int queryNum = queryCounter.incrementAndGet();
        
        // Tentukan tipe query
        String queryType = determineQueryType(sql);
        
        // Log dengan format yang SANGAT JELAS dan mudah ditemukan
        log.warn("");
        log.warn("╔════════════════════════════════════════════════════════════════════════════╗");
        log.warn("║ 🔥 HIBERNATE QUERY #{} — Type: {} 🔥", String.format("%-3d", queryNum), queryType);
        log.warn("╠════════════════════════════════════════════════════════════════════════════╣");
        log.warn("║ SQL: {}", formatSql(sql));
        log.warn("╚════════════════════════════════════════════════════════════════════════════╝");
        log.warn("");
        
        return sql;
    }

    /**
     * Tentukan tipe query (SELECT, INSERT, UPDATE, DELETE, etc.)
     */
    private String determineQueryType(String sql) {
        String upperSql = sql.trim().toUpperCase();
        
        if (upperSql.startsWith("SELECT")) return "SELECT";
        if (upperSql.startsWith("INSERT")) return "INSERT";
        if (upperSql.startsWith("UPDATE")) return "UPDATE";
        if (upperSql.startsWith("DELETE")) return "DELETE";
        if (upperSql.startsWith("CREATE")) return "CREATE";
        if (upperSql.startsWith("ALTER")) return "ALTER";
        if (upperSql.startsWith("DROP")) return "DROP";
        
        return "OTHER";
    }

    /**
     * Format SQL agar lebih mudah dibaca (menghapus whitespace berlebih)
     */
    private String formatSql(String sql) {
        return sql.trim().replaceAll("\\s+", " ");
    }

    /**
     * Ambil jumlah query yang sudah terjadi
     */
    public static int getQueryCount() {
        return queryCounter.get();
    }
}

