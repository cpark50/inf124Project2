package com.example.justplants;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "orderInfo", value = "/orderInfo")
public class orderInfo extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{ //slow. connection. 
            Class.forName("com.mysql.jdbc.Driver"); //load library
            Connection con = DriverManager.getConnection("jdbc:mysql:// localhost:3306/" + credentials.schemaName,"root", credentials.passwd);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM "+tables.order;
            ResultSet rs = stmt.executeQuery(sql);
            Integer total = 0;
            PrintWriter writer = resp.getWriter();
            writer.println("<html> <head>");
            writer.println("<script type=\"text/javaScript\" src=\"js/checkValidation.js\"></script>");
            writer.println("<link rel=\"stylesheet\" href=\"styles/orderInfo.css\"> <title>Just Plants</title> </head>");
            writer.println("<body> <div class=\"title\"><h1><a href=\"\">JustPlants</a></h1></div>");
            writer.println("<div class=\"nav_bar\"><ul><li><a class=\"active\" href=\"./\">Home</a></li><li><a href=\"aboutcompany.html\">About Company</a></li><li><a href=\"orderInfo\">Make Order</a></li></ul></div>");
            writer.println("<fieldset><legend>Cart</legend>");
            while(rs.next()){
                String name = rs.getString("p_name");
                String image = rs.getString("imagename");
                Integer price = rs.getInt("p_price");
                total += price;
                //boolean: getBlob
                //send to product page with id number for the database
                writer.println("<div><a href=\"./product\"><img src=\"images/" + image +"\" alt=\"" + name + "\">");
                writer.println("<p class=\"pname\">" + name + "</p>");
                writer.println("<p class=\"price\"> $" + price + ".00</p></a></div>");             
            }
            writer.println("<p class=\"totalPrice\">Total: "+total +"</p>");
            writer.println("</fieldset>");

            writer.println("<form action=\"checkOut\" name=\"orderForm\" method=\"post\" enctype=\"text/plain\" onsubmit=\"return (CheckValidation(this))\">");
            writer.println("<center><fieldset><legend>Shipping Information</legend>");
            writer.println("Country/Region:<br><input type=\"text\" name=\"country\"><br>");
            writer.println("First Name:<br><input type=\"text\" name=\"fname\"><br>");
            writer.println("Last Name:<br><input type=\"text\" name=\"lname\"><br>");
            writer.println("Phone Number:<br><input type=\"text\" name=\"phone\" placeholder=\"(xxx)-xxx-xxxx\"><br>");
            writer.println(":<br><input type=\"text\" name=\"address1\"><br><input type=\"text\" name=\"address2\"><br>");
            writer.println("City:<br><input type=\"text\" name=\"city\"><br>");
            writer.println(":<br><input type=\"text\" name=\"state\"><br>");
            writer.println("Zip Code:<br><input type=\"text\" name=\"zip\"><br><br>");
            writer.println("Shipping Speed:");
            writer.println("<select name=\"shipping\">");
            writer.println("<option value=\"Overnight\" selected=\"selected\">Overnight</option>");
            writer.println("<option value=\"Within 3 days\">Within 3 days</option>");
            writer.println("<option value=\"Within 7 days\">Within 7 days</option>");
            writer.println("</select></fieldset><br><br>");
            writer.println("<fieldset><legend>Payment Information</legend><br>");
            writer.println("Payment Method:");
            writer.println("<select name=\"payment\">");
            writer.println("<option value=\"None\" selected=\"selected\">None</option>");
            writer.println("<option value=\"VISA\">VISA</option>");
            writer.println("<option value=\"Mastercard\">Mastercard</option>");
            writer.println("<option value=\"Discover\">Discover</option>");
            writer.println("<option value=\"American Express\">American Express</option>");
            writer.println("<option value=\"UnionPay\">UnionPay</option>");
            writer.println("</select><br><br>");
            writer.println("Card Number:<br><input type=\"text\" name=\"card\"><br>");
            writer.println("Name on Card:<br><input type=\"text\" name=\"fullname\"><br>");
            writer.println("Expiration Date:<br><input type=\"text\" name=\"expDate\" placeholder=\"mm/yyyy\">");
            writer.println("</fieldset>");
            writer.println("<input type=\"submit\" value=\"Check Out\">");
            writer.println("<input type=\"reset\" value=\"Reset\"></center></form>");

            writer.println("</body> </html>");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
