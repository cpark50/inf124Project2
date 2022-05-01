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
import javax.servlet.http.HttpSession;

import java.util.ArrayList;


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
            Connection con = DriverManager.getConnection("jdbc:mysql:// localhost:3306/" + "JustPlantsProducts", "root", "aliceqiu367");
            Statement stmt = con.createStatement();

            // selects the user's row
            HttpSession session = req.getSession(true);
            String uid = (String) session.getAttribute("uid");
            
            String sql = "SELECT * FROM order_info WHERE uid = '" + uid + "'";
            ResultSet rs = stmt.executeQuery(sql);

            String pid = "";
            ArrayList<String> recentOrders = new ArrayList<String>();

            // obtains 5 products id
            // if it has record
            if (rs.last()){   
                while(rs.previous()){   
                    if (recentOrders.size() == 5)
                        break;

                    // it should be dynamic
                    for (int i = 1; i <= 10; i++){   
                        if (recentOrders.size() == 5)
                            break;

                        pid = "p_" + i;
                        if (rs.getString(pid) != null)
                            recentOrders.add(pid);
                    }
                }
            }           
            
            //show 5 recent items
            PrintWriter writer = resp.getWriter();

            writer.println("<html><body>");

            int count = 1;
            while(rs.next()){

                if (count == 5)
                    count = 1;

                String name = rs.getString("p_name");
                String image = rs.getString("imagename");
                int price = rs.getInt("p_price");

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