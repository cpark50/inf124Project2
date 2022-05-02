package com.example.justplants;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "product")
public class product extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = "";
        String image = "";
        Integer price = 0;
        String size = "";
        String othername = "";
        String dscrptn = "";
        String water = "";
        String light = "";
        String friend = "best kept away from pets and children";        
        
        try{ //slow. connection. 
            PrintWriter writer = resp.getWriter();
            String plant_id = "";
            HttpSession session = req.getSession(true);
            
            if (session.isNew()){
                int userId = ThreadLocalRandom.current().nextInt();
                session.setAttribute("visitorId", userId);
            }

            String str_url = req.getRequestURL().toString();
            plant_id = str_url.substring(str_url.lastIndexOf('/')+1);
            session.setAttribute("currentPlant", plant_id);
        
            plant_id = (String) session.getAttribute("currentPlant");

            int totalPlants = 0;

            if(null == session.getAttribute("totalPlants")) {
                session.setAttribute("totalPlants", totalPlants);
            }
            else {
                totalPlants = (int) session.getAttribute("totalPlants");
            }
           

            Class.forName("com.mysql.jdbc.Driver"); //load library
            Connection con = DriverManager.getConnection("jdbc:mysql:// localhost:3306/" + credentials.schemaName, "root", credentials.passwd);
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM "+tables.product+" WHERE id=" +plant_id;
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                name = rs.getString("p_name");
                image = rs.getString("imagename");
                price = rs.getInt("p_price");
                othername = rs.getString("p_othername");
                dscrptn = rs.getString("p_desc")+rs.getString("p_desc2");
                size = rs.getString("p_size");
                water = rs.getString("p_water");
                light = rs.getString("p_light");
                friend = "best kept away from pets and children";
                if (rs.getBoolean("p_pet")){
                    friend = "pet and children friendly";
                }
            }
            writer.println("<html><head>");
            writer.println("<link rel=\"stylesheet\" href=\"../styles/productpage.css\"> <title>Just Plants</title> </head>");
            writer.println("<body> <div class=\"title\"><h1><a href=\"../\">JustPlants</a></h1></div>");
            writer.println("<div class=\"nav_bar\"><ul><li><a class=\"active\" href=\"../\">Home</a></li><li><a href=\"../aboutcompany.html\">About Company</a></li><li><a href=\"../viewCart\">View My Cart("+totalPlants+")</a></li></ul></div>");
            writer.println("<div class=\"item-title\">");
            writer.println("<span>Plants</span><h1>"+ name + "</h1></div>");
            writer.println("<main class = \"container\">");
            writer.println("<div class=\"left-column\"><img data-image=\"red\" class=\"active\" src=\"../images/"+image+"\"></div>");
            writer.println("<div class=\"right-column\">");

            writer.println("<div class=\"product-description\">");
            writer.println("<p> <i> othername: "+ othername + "</i></p>");
            writer.println("<p>"+dscrptn+"<br><br>");
            writer.println("<i>size (height × width × diameter): <br>");
            writer.println(size+"</i><br><br></i>");
            writer.println("<i>How to take care:</i><br>");
            writer.println("light: "+light+"<br>");
            writer.println("water: "+water+"<br>");
            writer.println("friendliness: "+friend+"<br></p></div>");

            writer.println("<div class=\"product-price\"><span> $"+ price + ".00 </span></div>");
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
