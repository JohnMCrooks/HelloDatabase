package com.crooks;

import org.h2.tools.Server;

import java.sql.*;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();

        Connection connection = DriverManager.getConnection("jdbc:h2:./main");  //Open the connection to the Server
        Statement stmt = connection.createStatement();                          // create the connection to the server

        //Creates table
        stmt.execute("CREATE TABLE IF NOT EXISTS players (id IDENTITY, name VARCHAR, is_alive BOOLEAN, score INT, health DOUBLE)");

        //Inserts values into the table, Must follow same Order of attributes as the table is laid out.
        stmt.execute("INSERT INTO players VALUES(NULL, 'Alice', TRUE, 0, 100)");
        stmt.execute("UPDATE players SET is_alive = FALSE WHERE name = 'Alice' ");
        stmt.execute("DELETE FROM players WHERE name = 'Alice'");

        //BAD WAY OF DOING THINGS BELOW -- Leaves you open to vulnerabilities and sql injection attacks
//        String name = "Charlie";
//        stmt.execute(String.format("INSERT INTO players VALUES(NULL,'%s',TRUE,0,100)",name));

        //GOOD WAY BELOW-- Using a prepared Statement
        String name = "Charlie";
        PreparedStatement stmt2 = connection.prepareStatement("INSERT INTO players VALUES(NULL, ?, ?, 0 , 100)");
        stmt2.setString(1, name);           //Set placeholders
        stmt2.setBoolean(2, true);
        stmt2.execute();                    //Execute

        PreparedStatement stmt3 = connection.prepareStatement("SELECT * FROM players");
        ResultSet results = stmt3.executeQuery();
        while(results.next()){
            int id = results.getInt("id");
            String name2 = results.getString("name");
            Boolean is_alive2 = results.getBoolean("is_alive");
            System.out.println(id +" "+ name2+ " " + is_alive2);
        }

    }
}
