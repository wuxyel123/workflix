package servlet;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import resource.*;
import utils.ErrorCode;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
/**
    Description: Servlet for activities
    Input: None
    Output: None
    Usage: Called by frontend
 */
public class ActivitiesServlet extends AbstractServlet{

    /**
     * @see HttpServlet.HttpServlet()
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("activity") + 9);
    }


}
