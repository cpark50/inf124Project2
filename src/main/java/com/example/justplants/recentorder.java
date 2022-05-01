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
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // selects the user's row
            HttpSession session = req.getSession(true);
            //String uid = (String) session.getAttribute("uid");
            //System.out.println(uid);

            //String sql = "SELECT * FROM order_info WHERE u_id = '" + uid + "'";
            String sql = "SELECT * FROM order_info WHERE u_id = '" + 1 + "'";
            ResultSet rs = stmt.executeQuery(sql);

            String pid = "";
            
            ArrayList<Integer> recentOrders = new ArrayList<Integer>();

            // obtains 5 products id
            // if it has record
            rs.afterLast();
             
            while(rs.previous()){   
                if (recentOrders.size() == 5)
                    break;

                // it should be dynamic
                for (int i = 1; i <= 10; i++){   
                    if (recentOrders.size() == 5)
                        break;
                    pid = "p_" + i;
                    if (rs.getString(pid) != null)
                        recentOrders.add(i);
                }
            }
                      
            
            //show 5 recent items
            String selectProductsSql = "SELECT * FROM products";
            ResultSet prod_result = stmt.executeQuery(selectProductsSql);

            PrintWriter writer = resp.getWriter();

            writer.println("<html><body>");
            writer.println("<h3>My Recent Orders</h3>");

            int count = 1;
            while(prod_result.next()){
                if (count == 5)
                    count = 1;

                int id = prod_result.getInt("id");
                // if it is a recent order
                if (recentOrders.contains(id)){
                    // product info
                    String name = prod_result.getString("p_name");
                    String image = prod_result.getString("imagename");
                    writer.println("<div class=\"col-" + count + "\"><img src=\"images/" + image +"\" alt=\"" + name + "\">");
                    writer.println("<p class=\"pname\">" + name + "</p>");

                    // style
                    writer.println("<link href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\" rel=\"stylesheet\"/>");
                    writer.println("<style> .rating-list li {float: right;color: #ddd padding: 10px 5px;}");
                    writer.println(".rating-list li:hover, .rating-list li:hover~li { color: #ffd700;}");
                    writer.println(".rating-list {display: inline-block;list-style: none;}</style>");
                    
                    // rating stars
                    writer.println("<ul class=\"list-inline rating-list\">");
                    writer.println("<li><i class=\"fa fa-star\" title=\"1\"></i></li>");
                    writer.println("<li><i class=\"fa fa-star\" title=\"2\"></i></li>");
                    writer.println("<li><i class=\"fa fa-star\" title=\"3\"></i></li>");
                    writer.println("<li><i class=\"fa fa-star\" title=\"4\"></i></li>");
                    writer.println("<li><i class=\"fa fa-star\" title=\"5\"></i></li>");
                    writer.println("</ul>");

                    writer.println("</div>");
                    count++; 
                }
            }
            writer.println("</body> </html>");

            prod_result.close();
            rs.close();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}