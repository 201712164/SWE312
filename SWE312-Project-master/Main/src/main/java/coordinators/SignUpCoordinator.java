/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinators;

import components.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SignUpCoordinator extends GeneralCoordinator {

    private static final Random GENERATOR = new Random();
    private static int accountNum = 0;

    public SignUpCoordinator() {
        genAccountNum();
    }

    public boolean isPhoneNum(String phoneNum) { // 10-digits
        return phoneNum.matches("[0-9]{10}");
    }

    public boolean passMatches(char[] pass1Array, char[] pass2Array) { //During signingUp to check password & repeat pass
        String pass1 = charArrayToString(pass1Array);
        String pass2 = charArrayToString(pass2Array);
        return pass1.equals(pass2);
    }

    public boolean isEmail(String email) {
        return email.matches("[a-zA-Z0-9_%+-]+@[a-zA-Z.-]+\\.[a-zA-Z]{2,}");
    }

    private void genAccountNum() { //6-digits
        int min = 100000;
        int max = 900000;
        accountNum = min + GENERATOR.nextInt(max);
    }

    public int getAccountNum() {
        return accountNum;
    }

    public void userRegister(String name, String password, String phoneNum, String email) {
        Connection connection = DBConnection.connectDB();
        double balance = 0.00;

        if (connection != null) {
            try {
                PreparedStatement statment = (PreparedStatement) connection.prepareStatement("INSERT INTO accounts (accountnum,name,password,phonenum,email,balance) VALUES(?,?,?,?,?,?)");
                statment.setInt(1, accountNum);
                statment.setString(2, name);
                statment.setString(3, password);
                statment.setString(4, phoneNum);
                statment.setString(5, email);
                statment.setDouble(6, balance);
                int result = statment.executeUpdate();
            } catch (SQLException exception) {
                Logger.getLogger(gui.SignUpPage.class.getName()).log(Level.SEVERE, null, exception);
            }
        }

    }
}
