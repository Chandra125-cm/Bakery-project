import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/FeedbackServlet")
public class FeedbackServlet extends HttpServlet {

    // Updated database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bakery_project1?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Chandra@123";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Get form parameters
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zip = request.getParameter("zip");
        String message = request.getParameter("message");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to database
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Prepare SQL insert statement
            String sql = "INSERT INTO feedback (firstName, lastName, email, phone, city, state, zip, message) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            // Set parameters
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, city);
            pstmt.setString(6, state);
            pstmt.setString(7, zip);
            pstmt.setString(8, message);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                out.println("<html><body>");
                out.println("<h2>Thank you for your feedback!</h2>");
                out.println("<p>Your feedback has been saved successfully saved.</p>");
                out.println("</body></html>");
            } else {
                out.println("<html><body>");
                out.println("<h2>Error: Failed to save your feedback.</h2>");
                out.println("</body></html>");
            }

        } catch (ClassNotFoundException e) {
            out.println("<p>Error: MySQL JDBC Driver not found.</p>");
            e.printStackTrace(out);
        } catch (SQLException e) {
            out.println("<p>Database error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ignored) { }
            try {
                if (conn != null) conn.close();
            } catch (SQLException ignored) { }
        }
    }
}
