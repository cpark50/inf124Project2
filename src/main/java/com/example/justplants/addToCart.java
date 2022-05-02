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
@WebServlet(name = "addToCart") //@WebServlet(name="JDBC Demo", urlPatterns="/link")
public class addToCart extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }
    
    // @Override
    // protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //     resp.setContentType("text/html;charset=UTF-8");
    //     try(PrintWriter out = resp.getWriter()){ //slow. connection. 
    //         HttpSession session = req.getSession(true);
    //         int plant_id = Integer.parseInt(req.getParameter("plant_name"));
    //         int plant_qt = Integer.parseInt(req.getParameter("quantity"));
            
    //         int[] currentCart = (int[]) session.getAttribute("cart");
            
    //         if (currentCart == null){
    //             currentCart = new int[11];
    //             currentCart[plant_id] += plant_qt;
    //         }
    //         else {
    //             currentCart[plant_id] += plant_qt;
    //         }
    //         session.setAttribute("cart", currentCart);
    //         out.println("session created and added the item");
    //     }
        

    // }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        try{ //slow. connection. 
            HttpSession session = req.getSession(true);
            PrintWriter writer = resp.getWriter();
            int plant_id = Integer.parseInt(req.getParameter("plant_name"));
            int plant_qt = Integer.parseInt(req.getParameter("quantity"));
            int[] currentCart = (int[]) session.getAttribute("cart");
            
            if (currentCart == null){
                currentCart = new int[11];
                currentCart[plant_id] += plant_qt;
            }
            else {
                currentCart[plant_id] += plant_qt;
            }
            session.setAttribute("cart", currentCart);
            // RequestDispatcher dispatcher = req.getRequestDispatcher("product");
            // dispatcher.include(req, resp);
            int totalPlants = (int) session.getAttribute("totalPlants");
            session.setAttribute("totalPlants", totalPlants + plant_qt);
            resp.sendRedirect("http://localhost:8080/ecommerce/product/"+plant_id);


            Class.forName("com.mysql.jdbc.Driver"); //load library
            Connection con = DriverManager.getConnection("jdbc:mysql:// localhost:3306/" + credentials.schemaName, "root", credentials.passwd);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM "+tables.product+" WHERE id=" +plant_id;
            ResultSet rs = stmt.executeQuery(sql);
            
            writer.println("<div class=\"product-price\"><span> $" + ".00 </span></div>");
            writer.println("<div class=\"order-button\"><form action=\"../addToCart\" method=\"get\">");
            writer.println("<input type=\"number\" name=\"quantity\" step=\"1\" min=\"1\" max=\"\" value=\"1\" title=\"Qty\" class=\"input-text qty text\" size=\"2\" pattern=\"\" inputmode=\"\">");
            writer.println("<input type=\"hidden\" name=\"plant_name\" value=\""+plant_id+"\">");
            writer.println("<button type=\"submit\">Add to cart</button></form>");
            writer.println("</div></div></main>");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
