package servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ErrorCode;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Abstract servlet class
 * */
public class AbstractServlet extends HttpServlet {

    // logger
    Logger logger;
    // data source
    private static DataSource ds = null;


    /**
     * Init method
     * @param config ServletConfig contains the servlet's configuration and initialization parameters.
     * @throws ServletException Exception thrown when an error occurs during the servlet's initialization
     * */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        logger = LogManager.getLogger(this.getClass());
    }


    /**
     * Destroy method
     * @throws ServletException Exception thrown when an error occurs during the servlet's destruction
     * */
    @Override
    public void destroy(){
        super.destroy();
    }

    /**
     * Init the error
     * @param res Response
     * @param ec Error code
     * @throws IOException Error in IO operations
     * */
    public void writeError(HttpServletResponse res, ErrorCode ec) throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(ec.toJSON().toString());
    }

    /**
     * Init the data source
     * @throws IOException Error in IO operations
     * */
    public DataSource getDataSource() throws NamingException {

        // we don't want to initialize a new datasoruce everytime, so, we check first that ds is null
        if (ds == null) {

            Context initialContext = new InitialContext();
            Context envContext = (Context) initialContext.lookup("java:comp/env");
            ds = (DataSource) envContext.lookup("jdbc/workflix");
        }
        return ds;
    }
}
