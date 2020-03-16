package WordGame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

class Exporter {
    private static String url = "jdbc:postgresql://localhost:5432/postgres";
    private static String username = "postgres";
    private static String password = "password";
    private static String query = "Select * from results_with_meanings";

     static void exportIntoCSV(){
        Connection con;

        try {
            con = DriverManager.getConnection(url, username, password);
            ResultSet rs = con.createStatement().executeQuery(query);
            new File("result.csv");
            try (PrintWriter csvWriter = new PrintWriter(new FileWriter("result.csv"))) {
                while(rs.next()){
                    csvWriter.println(rs.getString(1)+"|"+ rs.getString(2));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static String makeString(){
        Connection con;
        StringBuilder builder = new StringBuilder();
        try {
            con = DriverManager.getConnection(url, username, password);
            ResultSet rs = con.createStatement().executeQuery(query);
            StringBuilder spaceBuilder = new StringBuilder();
            while(rs.next()) {
                String word = rs.getString(1);
                String meaning = rs.getString(2);
                int numberOfSpaces = 10 - word.length();
                for (int i = 0;i<numberOfSpaces;i++){
                    spaceBuilder.append(" ");
                }
               builder.append(word).append(spaceBuilder.toString()).append(meaning).append("\n").append("\n");
                spaceBuilder.setLength(0);
            }
//            con.createStatement().executeUpdate("truncate wn_pro_mysql.results_with_meanings");
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
