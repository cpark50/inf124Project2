package com.example.justplants;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


//when to close the db? 
//add information to and query table
@WebServlet(name = "viewCart") //@WebServlet(name="JDBC Demo", urlPatterns="/link")
public class viewShoppingCart extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{ //slow. connection. 
            HttpSession session = req.getSession(true);
            if (session.isNew()){
                int userId = ThreadLocalRandom.current().nextInt();
                session.setAttribute("visitorId", userId);
            }
            Class.forName("com.mysql.jdbc.Driver"); //load library
            Connection con = DriverManager.getConnection("jdbc:mysql:// localhost:3306/" + credentials.schemaName, "root", credentials.passwd);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM "+tables.product;
            ResultSet rs = stmt.executeQuery(sql);

            int count = 1;
            int totalPlants = 0;
            int totalPrice = 0;

            if(null == session.getAttribute("totalPlants")) {
                session.setAttribute("totalPlants", totalPlants);
            }
            else {
                totalPlants = (int) session.getAttribute("totalPlants");
            }

            PrintWriter writer = resp.getWriter();
            writer.println("<html> <head> <link rel=\"stylesheet\" href=\"styles/viewcart.css\"> <title>Just Plants</title> </head>");
            writer.println("<body> <div class=\"title\"><h1><a href=\"./\">JustPlants</a></h1></div>");
            writer.println("<div class=\"nav_bar\"><ul><li><a class=\"active\" href=\"./\">Shop</a></li><li><a href=\"aboutcompany.html\">About Company</a></li><li><a href=\"viewCart\">View Shopping Cart("+ totalPlants+ ")</a></li></ul></div>");
            
            //The cart is empty
            if (null == session.getAttribute("cart")){
                writer.println("<p id=\"emptycart\"><i>YOUR CART IS EMTPTY</i></p>");
            }
            else{
                writer.println("<div id=\"wrapper\">");
                writer.println("<div id=\"cart-items\" class=\"cart-items\">");
                writer.println("<form action=\"./updateCart\" method=\"get\">");
                while(rs.next()){
                    int[] userCart = (int[]) session.getAttribute("cart");
                    String name = rs.getString("p_name");
                    String image = rs.getString("imagename");
                    int price = rs.getInt("p_price");
                    int p_id = rs.getInt("id");
                    int quantity = 0;
    
                    if (userCart[p_id] > 0){
                        quantity = userCart[p_id];
                        writer.println("<div class=\"col-" + count + "\" id=\""+ p_id +"\"><a href=\"./product/"+p_id+"\"><img src=\"images/" + image +"\" alt=\"" + name + "\">");
                        writer.println("<p class=\"pname\">" + name + "</p>");
                        writer.println("<p class=\"price\"> $" + price + ".00</p></a>");
                        writer.println("<input type=\"number\" name=\"plant"+p_id+"\" step=\"1\" min=\"1\" max=\"30\" value=\""+ quantity +"\" title=\"Qty\" class=\"input-text qty text\" size=\"2\" pattern=\"\" inputmode=\"\">");
                        //writer.println("<p class=\"total price\"> Price: $"+ price*quantity+".00 </p></a>");
                        writer.println("</div>");
                        count++;
                        if (count == 6) { count = 1;}
                        totalPrice += price*quantity;
                    }
                    //send to product page with p_id number for the database
                }
                writer.println("</div>"); //div cart-items
                writer.println("<div id= \"final-order\" class='edit-or-order'><span> Total Price: $"+ totalPrice + ".00 </span>");
                writer.println("<input type=\"submit\" value=\"Update Cart\"/></form>");
                writer.println("<button onclick=\"location.href='./orderInfo'\" type=\"button\">Order Now</button></div>");
                writer.println("</div>"); //div wrapper
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
