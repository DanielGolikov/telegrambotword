package WordGame;

import java.sql.*;

class PostgresSQLWordChecker {
    private static String url = "jdbc:postgresql://localhost:5432/postgres";
    private static String username = "postgres";
    private static String password = "password";

    static String findAllWordsFromArray() {
        Connection con;
        StringBuilder builder = new StringBuilder();
        try {
            con = DriverManager.getConnection(url, username, password);


            PreparedStatement ps = con.prepareStatement("drop table if exists temporary_results; " +
                    "create table temporary_results(word varchar(15) primary key); " +
                    "COPY temporary_results FROM '/Users/daniel/IdeaProjects/telegrambotword/row_values.csv' DELIMITER ' ' ;");
            ps.executeUpdate();

            ps = con.prepareStatement( 
                    "drop table if exists results;" +
                            " create table results as " +
                            " SELECT wn_pro_mysql.wn_words.word " +
                            " From wn_pro_mysql.wn_words " +
                            " inner join temporary_results " +
                            " on wn_pro_mysql.wn_words.word=temporary_results.word");
            ps.executeUpdate();
            ps = con.prepareStatement(
                    "drop table if exists results_with_meanings;" +
                            "create table results_with_meanings as " +
                            "SELECT results.word,wn_word_and_gloss.short_gloss " +
                            "From results " +
                            " inner join wn_pro_mysql.wn_word_and_gloss" +
                            " on results.word=wn_pro_mysql.wn_word_and_gloss.word;");
            ps.executeUpdate();


            ResultSet rs = con.createStatement().executeQuery("Select * from results_with_meanings");
            int counter = 3900;
            while (rs.next()) {
                String word = rs.getString(1);
                String meaning = rs.getString(2);
                builder.append(word).append("-").append(meaning).append(";\n\n");
                if (builder.length() > counter) {
                    builder.append("@");
                    counter = counter + 3900;
                }
            }


            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String s = builder.toString();
        builder.setLength(0);
        return s;
    }
}
