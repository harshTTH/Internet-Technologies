package com.company;

import java.sql.*;
import java.util.Scanner;

public class BankAdmin {
    Connection conn;
    public BankAdmin(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String mysqlConnUrl = "jdbc:mysql://localhost:3307/bank";
        String userName = "bank";
        String pass = "1234";
        try {
            conn = DriverManager.getConnection(mysqlConnUrl,userName,pass);
            System.out.println("Database Connection Successful");
        } catch (SQLException e) {
            System.out.println("Database Connection Failed");
            e.printStackTrace();
        }
    }

    private int showMenu(){
        Scanner in = new Scanner(System.in);
        System.out.println("    ---Select option---");
        System.out.println("1. Create Account");
        System.out.println("2. Show Account");
        System.out.println("3. Exit");
        return in.nextInt();
    }

    private void createAccount(){
        String name,mobile,pass;
        int age;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Customer Name");
        name = in.next();
        System.out.println("Enter Age");
        age = in.nextInt();
        System.out.println("Enter Mobile Number");
        mobile = in.next();
        System.out.println("Enter new Password");
        pass = in.next();
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST (NAME,PASS,AGE,MOBILE) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,name);
            ps.setString(2,pass);
            ps.setInt(3,age);
            ps.setString(4,mobile);

            ps.executeUpdate();
            ResultSet set = ps.getGeneratedKeys();

            if(set.next()){
                int accNo = set.getInt(1);
                PreparedStatement psBook = conn.prepareStatement("CREATE TABLE "+name+accNo+" (BEF DOUBLE(10,2),OP CHAR(2),AMOUNT DOUBLE(10,2),AFT DOUBLE(10,2),ONTIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP  )");
                psBook.execute();
                System.out.println("Account created Successfully\nAccount number is "+accNo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private Account fetchAccount(String accNo) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet set = st.executeQuery("SELECT * FROM CUST WHERE ACC_NO=\""+accNo+"\"");
        if(set.next() == false){
            return null;
        }else {
            int acc = set.getInt("ACC_NO");
            String accName = set.getString("NAME");
            float amount = set.getFloat("AMOUNT");
            int age = set.getInt("AGE");
            String mobile = set.getString("MOBILE");
            return new Account(acc, accName, amount, age, mobile);
        }
    };

    public static void main(String[] args){
        BankAdmin admin = new BankAdmin();
        outer: while(true){
            switch(admin.showMenu()){
                case 1:
                    admin.createAccount();
                    break;
                case 2:
                    String accNo;
                    Scanner in = new Scanner(System.in);
                    System.out.println("Enter Account number");
                    accNo = in.nextLine();
                    try {
                        Account acc = admin.fetchAccount(accNo);
                        if(acc == null){
                            System.out.println("No Account with this Account number!");
                        }else{
                            acc.showDetails();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        admin.conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally {
                        break outer;
                    }
            }
        }
    }
}
