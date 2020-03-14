package org.WordGame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostgresSQLWordChecker {
    private static String url = "jdbc:postgresql://localhost:5432/postgres";
    private static String username = "postgres";
    private static String password = "password";

    static void findAllWordsFromArray() {
        Connection con;

        try {
            con = DriverManager.getConnection(url, username, password);


            PreparedStatement ps = con.prepareStatement("create temporary table temporary_results(word varchar(15) primary key);");
            ps.executeUpdate();


            ps = con.prepareStatement("COPY temporary_results FROM '/Users/daniel/IdeaProjects/telegrambotword/row_values.csv' DELIMITER ' ' ;");
            ps.executeUpdate();

            ps = con.prepareStatement("drop table if exists wn_pro_mysql.results;" +
                    "create table wn_pro_mysql.results as " +
                    "SELECT wn_pro_mysql.wn_words.word " +
                    "From wn_pro_mysql.wn_words\n" +
                    "inner join temporary_results\n" +
                    "on wn_pro_mysql.wn_words.word=temporary_results.word");
            ps.executeUpdate();
            ps = con.prepareStatement("drop table if exists wn_pro_mysql.results_with_meanings;" +
                    "create table wn_pro_mysql.results_with_meanings as " +
                    "SELECT wn_pro_mysql.results.word,wn_word_and_gloss.gloss " +
                    "From wn_pro_mysql.results " +
                    " inner join wn_pro_mysql.wn_word_and_gloss" +
                    " on wn_pro_mysql.results.word=wn_pro_mysql.wn_word_and_gloss.word;");
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
