package com.example.javafx.models;


import java.io.*;
import java.sql.*;

public class SqliteModel {
    /**
     * Connect to a sample database
     */
    public static String url = "jdbc:sqlite:java-sqlite.db";
    public static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void connect() throws SQLException {

        System.out.println("Connection to SQLite has been established.");
        Statement st = null;
        st = conn.createStatement();
        String sql = "create table user(username text primary key ,first_name text, last_name text,passwd text,pic blob)";
        st.execute(sql);

    }

    static public void insertNewUser(String username, String firname, String passwd, String lastname, File pic) throws SQLException, IOException {
        String sql = " INSERT INTO user (username ,first_name,last_name,passwd,pic) VALUES (?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, firname);
        pstmt.setString(3, lastname);
        pstmt.setString(4, passwd);
        pstmt.setBytes(5, new FileInputStream(pic).readAllBytes());
        pstmt.executeUpdate();    // 执行更新
        pstmt.close();
    }

    static public boolean login(String username, String passwd) throws SQLException {
        Statement statement = conn.createStatement();

        String sql = "select *from user where user.username=?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet set1 = pstmt.executeQuery();
        while (set1.next()) {
            String name = set1.getString(1);
            String password = set1.getString(4);
            if (password.equals(passwd)) return true;
            else return false;
        }
        return false;
    }

    static public ByteArrayInputStream getUserPic(String username) throws SQLException {
        Statement statement = conn.createStatement();

        String sql = "select *from user where user.username=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet set1 = pstmt.executeQuery();
        while (set1.next()) {
            String name = set1.getString(1);
            byte[] byteArr = set1.getBytes(5);
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArr);
            return bais;
        }
        return null;
    }

    static public String getFirstName(String username) throws SQLException {
        Statement statement = conn.createStatement();

        String sql = "select *from user where user.username=?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet set1 = pstmt.executeQuery();
        String firstname = null;
        while (set1.next()) {
            String name = set1.getString(1);
            firstname = set1.getString(2);
        }
        return firstname;
    }

    static public String getLastName(String username) throws SQLException {
        Statement statement = conn.createStatement();

        String sql = "select *from user where user.username=?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet set1 = pstmt.executeQuery();
        String lastname = null;
        while (set1.next()) {
            String name = set1.getString(1);
            lastname = set1.getString(3);
        }
        return lastname;
    }

    static public void changeUserProfile(String username, String firname, String lastname, File pic) throws SQLException, IOException {
        String sql = "UPDATE user SET first_name=?, last_name=?, pic=? WHERE username=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, firname);
        pstmt.setString(2, lastname);
        pstmt.setBytes(3, new FileInputStream(pic).readAllBytes());
        pstmt.setString(4, username);
        pstmt.executeUpdate();    // 执行更新
        pstmt.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
////        connect();
//        File file = new File("src/main/resources/login.jpeg");
////            insertNewUser("dsaf", "dsaf", "dasf", "dafasd", file);
//        getUserPic("dsaf");
//        System.out.println(login("dsaf", "dasf"));
//        conn.close();
        String c = "23123";
        System.out.println(c);

    }

}
