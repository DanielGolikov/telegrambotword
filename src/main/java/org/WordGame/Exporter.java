package org.WordGame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class Exporter {
    private static String url = "jdbc:postgresql://localhost:5432/postgres";
    private static String username = "postgres";
    private static String password = "password";

    public static void printResults(){
        Connection con;

        try {
            con = DriverManager.getConnection(url, username, password);
            ResultSet rs = con.createStatement().executeQuery("Select * from wn_pro_mysql.results_with_meanings");
            while(rs.next()) {
                System.out.println("word: "+rs.getString(1)+ "                definition: "+ rs.getString(2));
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void exportIntoCSV(){
        Connection con;

        try {
            con = DriverManager.getConnection(url, username, password);
            ResultSet rs = con.createStatement().executeQuery("Select * from wn_pro_mysql.results_with_meanings");
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
}
