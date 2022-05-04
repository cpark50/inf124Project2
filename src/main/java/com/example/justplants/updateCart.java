package com.example.justplants;

import java.io.IOException;
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


@WebServlet(name = "updateCart")
public class updateCart extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }
    // }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        try{ //slow. connection. 
            HttpSession session = req.getSession(true);
            int[] currentCart = (int[]) session.getAttribute("cart");
            int currQt = 0;
            int totalPlants = 0;
            for (int i =1; i < 12; i++){
                if(null != req.getParameter("plant"+i)){
                    currQt = Integer.parseInt(req.getParameter("plant"+i));
                    currentCart[i] = currQt;
                    totalPlants += currQt;
                }
            }
            
            session.setAttribute("cart", currentCart);
            
            session.setAttribute("totalPlants", totalPlants);
            resp.sendRedirect("http://localhost:8080/ecommerce/viewCart");


            Class.forName("com.mysql.jdbc.Driver"); //load library
            Connection con = DriverManager.getConnection("jdbc:mysql:// localhost:3306/" + credentials.schemaName, "root", credentials.passwd);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM "+tables.product;
            
            con.close();
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}