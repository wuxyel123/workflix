package filter;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is the login filter. It checks if the user is logged in.
 */
public class LoginFilter extends AbstractFilter {

    /**
     * The logger for this class
     */
    final static Logger logger = LogManager.getLogger(LoginFilter.class);

    /**
     * This method is called to apply the filter. It checks if the user is logged in.
     */
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        String loginURI = req.getContextPath() + "/jsp/login.jsp";

        boolean loggedIn = session != null && session.getAttribute("email") != null;

        if (loggedIn) {
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            chain.doFilter(req, res); // User is logged in, just continue request.
        } else {
            res.sendRedirect(loginURI); // Not logged in, show login page.
        }

    }

}
