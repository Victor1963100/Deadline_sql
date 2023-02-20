package ru.netology.mode;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.AfterAll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {

    public DbUtils() {
    }

    public static String getVerificationCode() {
        val codeSQL = "SELECT code FROM auth_codes WHERE created = (SELECT max(created) FROM auth_codes);";
        val runner = new QueryRunner();
        String verificationCode = "";

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            val code = runner.query(conn, codeSQL, new ScalarHandler<>());
            System.out.println(code);
            verificationCode = (String) code;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return verificationCode;
    }

    private static QueryRunner runner = new QueryRunner();

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static void cleanDatabase() {
        val connection = getConn();
        runner.execute(connection, "DELETE FROM auth_codes");
        runner.execute(connection, "DELETE FROM card_transactions");
        runner.execute(connection, "DELETE FROM cards");
        runner.execute(connection, "DELETE FROM users");
    }
}
