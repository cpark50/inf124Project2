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



//when to close the db? 
//add information to and query table
@WebServlet(name = "home", value = "/home") //@WebServlet(name="JDBC Demo", urlPatterns="/link")
public class mainpage extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{ //slow. connection. 
            Class.forName("com.mysql.jdbc.Driver"); //load library
            Connection con = DriverManager.getConnection("jdbc:mysql:// localhost:3306/" + "JustPlantsProducts", "root", "aliceqiu367");
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM products";
            ResultSet rs = stmt.executeQuery(sql);

            int count = 1;

            PrintWriter writer = resp.getWriter();
            writer.println("<html> <head> <link rel=\"stylesheet\" href=\"styles/mainpage.css\"> <title>Just Plants</title> </head>");
            writer.println("<body> <div class=\"title\"><h1><a href=\"home\">JustPlants</a></h1></div>");
            writer.println("<div class=\"nav_bar\"><ul><li><a class=\"active\" href=\"home\">Home</a></li><li><a href=\"aboutcompany.html\">About Company</a></li><li><a href=\"orderInfo\">Make Order</a></li></ul></div>");
            while(rs.next()){

                if (count == 5)
                    count = 1;

                String name = rs.getString("p_name");
                String image = rs.getString("imagename");
                Integer price = rs.getInt("p_price");
                //boolean: getBlob
                //send to product page with id number for the database
                writer.println("<div class=\"col-" + count + "\"><a href=\"./product\"><img src=\"images/" + image +"\" alt=\"" + name + "\">");
                writer.println("<p class=\"pname\">" + name + "</p>");
                writer.println("<p class=\"price\"> $" + price + ".00</p></a></div>");
                count++;             
            }
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
