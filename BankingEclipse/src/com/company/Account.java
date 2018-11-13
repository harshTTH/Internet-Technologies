package com.company;

import java.sql.*;

class Account {
    private String name,mobile;
    private float amount;
    private int age,accNo;

    Account(int accNo, String name,float amount,int age,String mobile){
        this.accNo = accNo;
        this.name = name;
        this.amount = amount;
        this.age = age;
        this.mobile = mobile;
    }

    void showDetails(){
        System.out.println("        ----Account Details----         ");
        System.out.println("Name   - "+this.name);
        System.out.println("Age    - "+this.age);
        System.out.println("Mobile - "+this.mobile);
        System.out.println("Amount - "+this.amount);
    }

    boolean updatePassbook(String op,Connection conn,double past,double current,double money){
        try{
            PreparedStatement ps = conn.prepareStatement("INSERT INTO "+this.name+this.accNo+" (BEF,OP,AMOUNT,AFT) VALUES (?,?,?,?)");
            ps.setDouble(1,past);
            ps.setString(2,op);
            ps.setDouble(3,money);
            ps.setDouble(4,current);
            ps.executeUpdate();
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    boolean withdrawMoney(Connection conn,double money) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet set = st.executeQuery("SELECT AMOUNT FROM CUST WHERE ACC_NO="+this.accNo);
        if(set.next() == false)return false;
        double past = set.getDouble("AMOUNT");
        if(past > money) {
            double current = past - money;
            st.execute("UPDATE CUST SET AMOUNT=" + current + " WHERE ACC_NO=" + this.accNo);
            if(updatePassbook("DB",conn,past,current,money)){
                return true;
            }else{
                st.execute("UPDATE CUST SET AMOUNT=" + past + " WHERE ACC_NO=" + this.accNo);
                return false;
            }
        }else return false;
    }

    boolean depositMoney(Connection conn,double money) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet set = st.executeQuery("SELECT AMOUNT FROM CUST WHERE ACC_NO="+this.accNo);
        if(set.next() == false)return false;
        double past = set.getDouble("AMOUNT");
        double current = past + money;
        st.execute("UPDATE CUST SET AMOUNT=" + current + " WHERE ACC_NO=" + this.accNo);
        if(updatePassbook("CR",conn,past,current,money)){
            return true;
        }else{
            st.execute("UPDATE CUST SET AMOUNT=" + past + " WHERE ACC_NO=" + this.accNo);
            return false;
        }
    }

    boolean showPassbook(Connection conn){
        try {
            Statement st = conn.createStatement();
            ResultSet set = st.executeQuery("SELECT * FROM "+this.name+this.accNo);

            System.out.println("\n\n");
            this.showDetails();
            System.out.println("\n\n");

            System.out.println("PREVIOUS BALANCE        OPERATION       AMOUNT      CURRENT BALANCE     TIMESTAMP");
            while(set.next() != false){
                String op;
                if(set.getString("OP").equals("CR"))op="CREDIT";
                else op="DEBIT";
                System.out.println("    "+set.getDouble("BEF")+"        "+"           "+op+"       "+set.getDouble("AMOUNT")+"            "+set.getDouble("AFT")+"             "+set.getTimestamp("ONTIME"));
            }
            System.out.println("\n\n");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
