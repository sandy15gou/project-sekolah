package com.sandy.project.Integration;

import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.QueryCountHolder;

/**
 * Utility class untuk menghitung dan memverifikasi jumlah query ke database.
 *
 * Menggunakan datasource-proxy (net.ttddyy) yang sudah dikonfigurasi
 * di {@link com.sandy.project.config.DataSourceProxyBeanProcessor}.
 *
 * Cara pakai:
 * <pre>
 *   QueryCountAssert.reset();
 *   // ... jalankan operasi
 *   QueryCountAssert.assertSelectCount(1); // harus tepat 1 SELECT
 * </pre>
 *
 * @author Sandy
 */
public class QueryCountAssert {

    private QueryCountAssert() {}

    /**
     * Reset counter sebelum memulai pengukuran
     */
    public static void reset() {
        QueryCountHolder.clear();
    }

    /**
     * Ambil total SELECT queries yang terjadi
     */
    public static long getSelectCount() {
        QueryCount count = QueryCountHolder.getGrandTotal();
        return count.getSelect();
    }

    /**
     * Ambil total INSERT queries yang terjadi
     */
    public static long getInsertCount() {
        QueryCount count = QueryCountHolder.getGrandTotal();
        return count.getInsert();
    }

    /**
     * Ambil total UPDATE queries yang terjadi
     */
    public static long getUpdateCount() {
        QueryCount count = QueryCountHolder.getGrandTotal();
        return count.getUpdate();
    }

    /**
     * Ambil total DELETE queries yang terjadi
     */
    public static long getDeleteCount() {
        QueryCount count = QueryCountHolder.getGrandTotal();
        return count.getDelete();
    }

    /**
     * Ambil total semua queries (SELECT + INSERT + UPDATE + DELETE)
     */
    public static long getTotalCount() {
        QueryCount count = QueryCountHolder.getGrandTotal();
        return count.getTotal();
    }

    /**
     * Ambil total semua queries (alias untuk getTotalCount)
     */
    public static long getTotalQueryCount() {
        return getTotalCount();
    }

    /**
     * Assert bahwa jumlah SELECT tepat sama dengan expected
     */
    public static void assertSelectCount(long expected) {
        long actual = getSelectCount();
        if (actual != expected) {
            throw new AssertionError(
                String.format("Expected %d SELECT queries, but got %d SELECT queries.%n" +
                              "This may indicate an N+1 problem or missing JOIN FETCH.",
                              expected, actual));
        }
    }

    /**
     * Assert bahwa jumlah SELECT tidak melebihi batas maksimum
     */
    public static void assertSelectCountLessThanOrEqualTo(long maxAllowed) {
        long actual = getSelectCount();
        if (actual > maxAllowed) {
            throw new AssertionError(
                String.format("Expected at most %d SELECT queries, but got %d SELECT queries.%n" +
                              "This may indicate an N+1 problem!",
                              maxAllowed, actual));
        }
    }

    /**
     * Print ringkasan query count (berguna untuk debugging)
     */
    public static String getSummary() {
        QueryCount count = QueryCountHolder.getGrandTotal();
        return String.format(
            "Query Summary → SELECT: %d | INSERT: %d | UPDATE: %d | DELETE: %d | TOTAL: %d",
            count.getSelect(),
            count.getInsert(),
            count.getUpdate(),
            count.getDelete(),
            count.getTotal()
        );
    }
}

