package com.example.justplants;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "checkOut", urlPatterns = "/checkOut")
public class checkOut extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        PrintWriter out = resp.getWriter();
        int[] currentCart = (int[]) session.getAttribute("cart");
        int userId = (int)session.getAttribute("visitorId");
        if(currentCart==null){
            out.println("<html><body>");
            out.println("Sorry user "+userId+ ", your cart is empty.");
            out.println("<br><button onclick=\"history.back()\">Go Back</button>");
            out.println("</body></html>");
        }
        else{
            RequestDispatcher rd = req.getRequestDispatcher("keep");
            rd.forward(req, resp);
        }      
        

    }
}
