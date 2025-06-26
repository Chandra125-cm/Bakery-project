import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bakery_project1";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Chandra@123";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String payment = request.getParameter("payment");
        String cartData = request.getParameter("cartData");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String sql = "INSERT INTO ordersss (name, phone, address, payment_method, cart_data) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, address);
            stmt.setString(4, payment);
            stmt.setString(5, cartData);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                out.println("<h2 style='color:green;text-align:center'>Order Placed Successfully ✅</h2>");
                out.println("<div style='text-align:center;'><a href='home.html'>Back to Home</a></div>");
            } else {
                out.println("<h2 style='color:red;text-align:center'>Failed to Place Order ❌</h2>");
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2 style='color:red;text-align:center'>Error: " + e.getMessage() + "</h2>");
        }
    }
}
