package WordGame;

import java.io.File;
import java.sql.*;

class PostgresSQLWordChecker {
    private static String url = "jdbc:postgresql://localhost:5432/postgres";
    private static String username = "postgres";
    private static String password = "password";

    static void findAllWordsFromArray() {
        Connection con;
        StringBuilder builder = new StringBuilder();
        try {
            con = DriverManager.getConnection(url, username, password);


            PreparedStatement ps = con.prepareStatement("create temporary table temporary_results(word varchar(15) primary key); " +
                    "COPY temporary_results FROM '/Users/daniel/IdeaProjects/telegrambotword/row_values.csv' DELIMITER ' ' ;");
            ps.executeUpdate();

            ps = con.prepareStatement(
                    "create temporary table results as " +
                    "SELECT wn_pro_mysql.wn_words.word " +
                    "From wn_pro_mysql.wn_words " +
                    "inner join temporary_results " +
                    "on wn_pro_mysql.wn_words.word=temporary_results.word");
            ps.executeUpdate();
            ps = con.prepareStatement(
                    "create temporary table results_with_meanings as " +
                    "SELECT results.word,wn_word_and_gloss.short_gloss " +
                    "From results " +
                    " inner join wn_pro_mysql.wn_word_and_gloss" +
                    " on results.word=wn_pro_mysql.wn_word_and_gloss.word;");
            ps.executeUpdate();


            ResultSet rs = con.createStatement().executeQuery("Select * from results_with_meanings");
            StringBuilder spaceBuilder = new StringBuilder();
            while(rs.next()) {
                String word = rs.getString(1);
                String meaning = rs.getString(2);
                int numberOfSpaces = 10 - word.length();
                for (int i = 0;i<numberOfSpaces;i++){
                    spaceBuilder.append(" ");
                }
                builder.append(word).append(spaceBuilder.toString()).append(meaning).append("\n\n");
                spaceBuilder.setLength(0);
            }


            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String s = builder.toString();
        builder.setLength(0);
        String[] args={s};
        TextToGraphics.main(args);
    }
}
