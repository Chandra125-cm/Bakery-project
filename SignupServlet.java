import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/index")
public class SignupServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bakery_project1";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Chandra@123";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            out.println("<script>alert('Please fill all fields');window.location='index.html';</script>");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String sql = "INSERT INTO users(username, password) VALUES (?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                int result = ps.executeUpdate();

                if (result > 0) {
                    out.println("<script>alert('Signup successful! Please login now.');window.location='login.html';</script>");
                } else {
                    out.println("<script>alert('Signup failed. Try again.');window.location='index.html';</script>");
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // duplicate username
                out.println("<script>alert('Username already exists. Choose another.');window.location='index.html';</script>");
            } else {
                out.println("<script>alert('Error: " + e.getMessage().replace("'", "\\'") + "');window.location='index.html';</script>");
            }
        } catch (ClassNotFoundException e) {
            out.println("<script>alert('Driver error: " + e.getMessage().replace("'", "\\'") + "');window.location='index.html';</script>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}

