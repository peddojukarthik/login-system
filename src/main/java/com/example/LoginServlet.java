package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doget(HttpServletRequest request, HttpServletResponse response) throws IOException{
        response.setContentType("text/html");
        response.getWriter().println("this servlet only handles post request");
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String action = request.getParameter("action");

        

        try {
             Class.forName("com.mysql.cj.jdbc.Driver");

           Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/loguser","root","Mextscholar@2007"
    );
    
    System.out.println("action received:"+action);
            response.setContentType("text/html");
            if ("login".equals(action)) {

                
            String query = "select * from users where username=? and password=?"; 

             response.setContentType("text/html");
        response.getWriter().println("<html><body><h1>login QUERY</h1></body></html>");

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if( rs.next()){
                RequestDispatcher rd = request.getRequestDispatcher("loginsuccess.html");
               rd.forward(request, response);
            }
            else{
                RequestDispatcher rd = request.getRequestDispatcher("loginfail.html");
                 rd.forward(request, response);
            } 
        
            rs.close();
            pstmt.close();
        }
        else if ("SIGNIN".equals(action)) {

            String iquery ="INSERT INTO users VALUES (?,?)";
            PreparedStatement pstmt = conn.prepareStatement(iquery);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int result = pstmt.executeUpdate();
            if (result>0) {
                response.sendRedirect("signsuccess.html");
               
            }else{
                 response.sendRedirect("signfail.html");
               
              
            }
            pstmt.close();


        }
        conn.close();
           
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<h2>e.exception:"+e.getClass().getName());
             response.getWriter().println("<p>"+e.getMessage()+"</p>");
        }
    }
}