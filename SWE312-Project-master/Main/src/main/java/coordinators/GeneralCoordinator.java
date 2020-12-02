/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coordinators;
import java.util.*;

import components.DBConnection;
import gui.HomePage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class GeneralCoordinator {
	private int AccountNum=0;
	private double balance=0.0;

    public boolean isAlphabetic(String input) {
        return input.matches("[a-zA-Z\\s]+");// includes white space
    }

    public String charArrayToString(char[] charArray) {
        String result = "";
        for (int i = 0; i < charArray.length; i++) {
            result += charArray[i];
        }
        return result;
    }

    public boolean isNumeric(String text) {
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }

    public boolean userSignIn(int accountNum, String password) {
        Connection connection = DBConnection.connectDB();
        if (connection != null) {
            try {
                PreparedStatement statment = (PreparedStatement) connection.prepareStatement("Select * from accounts WHERE accountnum = ? AND password = ?");
                statment.setInt(1, accountNum);
                statment.setString(2, password);
                ResultSet result = statment.executeQuery();
                if (result.next()) {
                	this.AccountNum=accountNum;
                	this.balance=Double.parseDouble(result.getString("balance"));
                	return true;
                } else {
                	return false;
                }
            } catch (SQLException exception) {
                Logger.getLogger(gui.SignInPage.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return false;
    }
    
    public boolean depositMoney(int accountNum, int money) {
    	Connection connection = DBConnection.connectDB();
    		double balance = money;
    		
            if (connection != null) {
                try {
                    PreparedStatement statment = (PreparedStatement) connection.prepareStatement("UPDATE accounts SET balance = ? WHERE accountnum = ? ");
                    statment.setDouble(1, balance);
                    statment.setInt(2, accountNum);
                    int result = statment.executeUpdate();
                    return true;
                } catch (SQLException exception) {
                    Logger.getLogger(gui.SignUpPage.class.getName()).log(Level.SEVERE, null, exception);
                }
            }	
		return false;
    }
    
    public List<String> getAccountInfo(int accountNum) {
    	Connection connection = DBConnection.connectDB();
    	List<String> list = new ArrayList<String>();
        if (connection != null) {
            try {
                PreparedStatement statment = (PreparedStatement) connection.prepareStatement("Select * from accounts WHERE accountnum = ?");
                statment.setInt(1, accountNum);
                ResultSet result = statment.executeQuery();
                if (result.next()) {
                	list.add(result.getString("accountnum"));
                	list.add(result.getString("name"));
                	list.add(result.getString("balance"));
                	return  list;
                } else {
                	return null;
                }
            } catch (SQLException exception) {
                Logger.getLogger(gui.SignInPage.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return null;
    }
    
    public boolean transferMoney(int fromAccountNum,int toAccountNum,int money,int amount) {
    	Connection connection = DBConnection.connectDB();
    	double balance=0;
        if (connection != null) {
            try {
                PreparedStatement checkAccountStatment = (PreparedStatement) connection.prepareStatement("Select * from accounts WHERE accountnum = ?");
                checkAccountStatment.setInt(1, toAccountNum);
                ResultSet result = checkAccountStatment.executeQuery();
                if (result.next()) {
                	balance=Double.parseDouble(result.getString("balance"));
                	PreparedStatement transferStatment = (PreparedStatement) connection.prepareStatement("UPDATE accounts SET balance = ? WHERE accountnum = ? ");
                	transferStatment.setDouble(1, balance+amount);
                	transferStatment.setInt(2, toAccountNum);
                	int result2 = transferStatment.executeUpdate();
               
                    	PreparedStatement depositStatment = (PreparedStatement) connection.prepareStatement("UPDATE accounts SET balance = ? WHERE accountnum = ? ");
                    	depositStatment.setDouble(1, money);
                    	depositStatment.setInt(2, fromAccountNum);
                    	int result3 = depositStatment.executeUpdate();
                    	return  true;
                    
                } else {
                	return false;
                }
            } catch (SQLException exception) {
                Logger.getLogger(gui.SignInPage.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
        return false;
    }
    

}
