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


@WebServlet(name = "recentorder", value = "/recentorder")
public class recentorder extends HttpServlet{

    @Override
    public void init() throws ServletException {
        super.init();
        
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            Class.forName("com.mysql.jdbc.Driver"); //load library
            Connection con = DriverManager.getConnection("jdbc:mysql:// localhost:3306/" + credentials.schemaName, "root", credentials.passwd);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM "+tables.order;
            ResultSet rs = stmt.executeQuery(sql);

            PrintWriter writer = resp.getWriter();

            int count = 1;

            writer.println("<html><body>");
            while(rs.next()){

                if (count == 5)
                    count = 1;

                String name = rs.getString("p_name");
                String image = rs.getString("imagename");
                Integer price = rs.getInt("p_price");

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