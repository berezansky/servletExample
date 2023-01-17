package com.berezanskiy.servletExample.db;

import java.sql.*;

public class SqlDatabase {
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:postgresql://localhost:5436/hexlet";
        String login = "postgres";
        String password = "postgres";

        Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users;");

        while (resultSet.next()) {
            System.out.println(resultSet.getString("login"));
        }

        resultSet.close();
        statement.close();

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
        preparedStatement.setInt(1, 1);
        ResultSet resultSet1 = preparedStatement.executeQuery();

        while (resultSet1.next()) {
            System.out.println("username with id = 1: " + resultSet1.getString(2));
        }

        connection.close();
    }
}
