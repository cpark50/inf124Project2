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

import com.mysql.cj.xdevapi.Result;

import java.util.ArrayList;
import java.util.Map;


@WebServlet(name = "recentorder", value = "/recentorder")
public class recentorder extends HttpServlet{

    private final static String[] style = {
        "<style>",
        ".star-red, .star-gray {",
        "    clip-path: polygon(50% 0, 65% 40%, 100% 40%, 72% 60%, 85% 100%, 50% 75%, 15% 100%, 28% 60%, 0 40%, 35% 40%);",
        "}",
        ".star-gray { background: rgb(218, 213, 213); }",
        ".star-red { background: red; }",
        "</style>"
    };

    private final static String[] script = {
        "<script>",
        "function onMouseOver(elem) {",
        "  var star_prefix = elem.id.slice(0,-1);",
        "  var rate = parseInt(elem.id.slice(-1));",
        "  for(star = 1; star<=5; star++) {",
        "    var div = document.getElementById(star_prefix + star);",
        "    if(div == null)",
        "        break;",
        "    if(star <= rate)",
        "        div.setAttribute('class', 'star-red');",
        "    else",
        "        div.setAttribute('class', 'star-gray');",
        "  }",
        "  var xhr = new XMLHttpRequest();",
        "  var url = './recentorder?action=rate&product=' + star_prefix + '&rate=' + rate;",
        "  xhr.open('get', url, true);",
        "  xhr.send(null);",
        "}",
        "</script>"
    };

    Connection conn;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql:// localhost:3306/" + credentials.schemaName, "root", credentials.passwd);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            //System.out.println("recentorder -> get");
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //System.out.println("recentorder -> connected");
            HttpSession session = req.getSession(true);
            //String uid = "1";
            String uid = String.valueOf(session.getAttribute("visitorId"));
            //System.out.println(uid);

            Map<String, String[]> params = req.getParameterMap();
            if(params.get("action") != null){
                String p1 = params.get("product")[0];
                String pindex = p1.substring(uid.length(), p1.indexOf('_'));
                int rate = Integer.valueOf(params.get("rate")[0]);
                System.out.println("uid: " + uid);
                System.out.println("p1: " + p1);
                System.out.println("pindex: " + pindex);
                String storeRatingsql = "REPLACE INTO rating VALUES (" + "'" + uid + "_" + pindex + "'" + "," + uid + "," + pindex + "," + rate + ")";
                stmt.executeUpdate(storeRatingsql);
                //System.out.println(pindex+ ": " +rate);
                return;
            }

            String sql = "SELECT * FROM order_info WHERE u_id = " + uid;
            //String sql = "SELECT * FROM order_info WHERE u_id = 1";
            ResultSet rs = stmt.executeQuery(sql);

            
            String pid = "";
            ArrayList<Integer> recentOrders = new ArrayList<Integer>();

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
            String selectProductsSql = "SELECT * FROM " + tables.product;
            ResultSet prod_result = stmt.executeQuery(selectProductsSql);

            PrintWriter writer = resp.getWriter();
            writer.println("<html><body>");

            for (String s : style){
                writer.println(s);
            }
            for (String s : script){
                writer.println(s);
            }

            writer.println("<br>");
            writer.println("<br>");
            writer.println("<h3>My Recent Orders</h3>");
            int count = 1;
            while(prod_result.next()){
                if (count == 6)
                    count = 1;

                int id = prod_result.getInt("id");
                // if it is a recent order
                if (recentOrders.contains(id)){
                    // product info
                    String name = prod_result.getString("p_name");
                    String image = prod_result.getString("imagename");
                    writer.println("<div class=\"col-" + count + "\"><img src=\"images/" + image +"\" alt=\"" + name + "\">");
                    writer.println("<p class=\"pname\">" + name + "</p>");

                    //Star stars = new Star("1" + id, 5, 0, 0, 4, 16, false);
                    Star stars = new Star(uid + id, 5, 0, 0, 4, 16, false);
                    stars.appendStarHTML(resp);
                    writer.println("</div>");
                    count++; 
                }
            }
            writer.println("</body> </html>");

            prod_result.close();
            rs.close();   
        }catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("recent order exception");
        }   
    }
}