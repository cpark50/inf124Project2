package com.example.justplants;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "keepOrder", urlPatterns = "/keep")
public class keepOrder extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            PrintWriter out = resp.getWriter();
            HttpSession session = req.getSession(true);
            int[] currentCart = (int[]) session.getAttribute("cart");
            int userId = (int)session.getAttribute("visitorId");
            Integer total = 0;

            Class.forName("com.mysql.jdbc.Driver"); //load library
            Connection con = DriverManager.getConnection("jdbc:mysql:// localhost:3306/" + credentials.schemaName, "root", credentials.passwd);
            Statement stmt = con.createStatement();
            String sql = "";
            String fLine = "INSERT INTO order_info(u_id, shipping";
            String sLine = " VALUES("+userId+",\""+req.getParameter("shipping")+"\"";
            String u_name="";
            String u_address="";
            String u_phone="";
            String u_card="";
            u_name = req.getParameter("fname")+" "+req.getParameter("lname");
            u_address = req.getParameter("address1")+" "+req.getParameter("address2")+" "+
                        req.getParameter("city")+" "+req.getParameter("state")+" "+req.getParameter("zip")+" "+
                        req.getParameter("country");
            u_phone = req.getParameter("phone");
            u_card = req.getParameter("payment")+" "+req.getParameter("card")+" "+
                     req.getParameter("fullname")+" "+req.getParameter("expDate");
            
            sql = "INSERT INTO user_info(u_id, u_name, u_address, u_phone, u_card)"+ 
                    " VALUES("+userId+",\""+u_name+"\",\""+u_address+"\",\""+u_phone+"\",\""+u_card+"\")";
            stmt.executeUpdate(sql);
            out.println("<html> <head>");
            out.println("<link rel=\"stylesheet\" href=\"styles/orderInfo.css\"> <title>Order Confirmed</title> </head>");
            out.println("<body> <div class=\"title\"><h1><a href=\"./\">JustPlants</a></h1></div>");
            out.println("<div class=\"nav_bar\"><ul><li><a class=\"active\" href=\"./\">Home</a></li><li><a href=\"aboutcompany.html\">About Company</a></li></ul></div>");
            out.println("<div class=\"orderconfirmed\"><h1>Order Confirmed</h1></div>");
            // out.println("<p>"+sql+"</p>");
            if(currentCart!=null){
                out.println("<div class=orderInfo>Hello User "+userId+", Your order is: </div>");
                for(int i=1; i<11; i++){
                    if(currentCart[i]>0){
                        fLine += ", p_"+i;
                        sLine += ","+currentCart[i];
                        out.println("<div class=orderInfo>"+currentCart[i]+" "+plants.PLANT_NAMES[i]+"</div>");
                        total += currentCart[i]*plants.PLANT_PRICES[i];
                    }
                }
                fLine+=")";
                sLine+=")";
                sql = fLine+sLine;
                // out.println("<p>"+sql+"</p>");
                stmt.executeUpdate(sql);
                out.println("<div class=orderInfo>Total is $"+total+".00 </div>");
                Arrays.fill(currentCart, 0);
                session.setAttribute("cart", currentCart);
                session.setAttribute("totalPlants", 0);
            }
            out.println("</body></html>");    
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
