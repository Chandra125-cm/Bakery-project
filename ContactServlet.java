import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ContactServlet")
public class ContactServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // DB details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bakery_project1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Chandra@123";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "INSERT INTO contact (name, email, message) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, message);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                out.println("<h2 style='color:green;'>Thank you, " + name + "! Your message has been sent.</h2>");
            } else {
                out.println("<h2 style='color:red;'>Message failed. Please try again.</h2>");
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            out.println("<h2 style='color:red;'>Error: " + e.getMessage() + "</h2>");
        }
    }
}
