package com.company;

import java.sql.*;
import java.util.Scanner;

public class Bank {
    Connection conn;

    public Bank(){
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

    public String[] getCred(){
        String cred[] = new String[2];
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Local Banking System");
        System.out.println("Enter Credentials :- ");
        System.out.println("1. Enter Account number.");
        cred[0] = in.next();
        System.out.println("2. Enter Password");
        cred[1] = in.next();

        return cred;
    }

    public int showFunctions(){
        Scanner in = new Scanner(System.in);
        System.out.println("-/-/-/-/-Local Banking System-/-/-/-/-");
        System.out.println("Enter <Choice no.> to perform Operation");
        System.out.println("1. Withdraw Amount");
        System.out.println("2. Deposit Amount");
        System.out.println("3. Show Passbook");
        System.out.println("4. Exit");
        return in.nextInt();
    }

    private Account openAccount(String[] cred) throws SQLException {
        Statement st = this.conn.createStatement();
        ResultSet set = st.executeQuery("SELECT * FROM CUST WHERE ACC_NO="+cred[0]+" AND PASS=\""+cred[1]+"\"");

        set.first();

        int accNo = set.getInt("ACC_NO");
        String accName = set.getString("NAME");
        float amount = set.getFloat("AMOUNT");
        int age = set.getInt("AGE");
        String mobile = set.getString("MOBILE");
        return new Account(accNo, accName, amount, age, mobile);
    }

    public static void main(String[] args) {
	    Bank bank = new Bank();
	    String[] cred = bank.getCred();
        try {
            Account acc = bank.openAccount(cred);
            if(acc == null){
                System.out.println("Access Denied!!");
            }else{
                acc.showDetails();
                Scanner in = new Scanner(System.in);
                double money;

                outer:while(true){
                    switch(bank.showFunctions()){
                        case 1:
                            System.out.println("Enter amount to withdraw");
                            money = in.nextDouble();
                            if(acc.withdrawMoney(bank.conn,money)){
                                System.out.println("Amount "+money+" Debited successfully from your account");
                            }else System.out.println("Insufficient money! ");
                            break;
                        case 2:
                            System.out.println("Enter Amount to deposit");
                            money = in.nextDouble();
                            if(acc.depositMoney(bank.conn,money)){
                                System.out.println("Amount "+money+" Credited successfully to your account");
                            }else System.out.println("Money cannot be credited");
                            break;
                        case 3:
                            if(!acc.showPassbook(bank.conn)){
                                System.out.println("Something went wrong please retry !");
                            }
                            break;
                        case 4:
                            try {
                                bank.conn.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }finally {
                                break outer;
                            }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
