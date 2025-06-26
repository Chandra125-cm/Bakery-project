import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/bakery_project1";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Chandra@123";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            out.println("<script type='text/javascript'>");
            out.println("alert('Please fill all fields');");
            out.println("location='login.html';");
            out.println("</script>");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String sql = "SELECT * FROM users WHERE username=? AND password=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    response.sendRedirect("home.html");
                } else {
                    out.println("<script type='text/javascript'>");
                    out.println("alert('Invalid username or password');");
                    out.println("location='login.html';");
                    out.println("</script>");
                }
            }
        } catch (SQLException e) {
            out.println("<script type='text/javascript'>");
            out.println("alert('Database error: " + e.getMessage().replace("'", "") + "');");
            out.println("location='login.html';");
            out.println("</script>");
        } catch (ClassNotFoundException e) {
            out.println("<script type='text/javascript'>");
            out.println("alert('JDBC Driver error: " + e.getMessage().replace("'", "") + "');");
            out.println("location='login.html';");
            out.println("</script>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
