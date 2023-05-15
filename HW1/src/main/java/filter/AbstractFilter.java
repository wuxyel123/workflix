package filter;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ErrorCode;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * This class is the base class for all filters. It contains the logger and the datasource.
 * It also contains a method to write an error to the response.
 */
public class AbstractFilter extends HttpFilter {

    /**
     * The logger for this class
     */
    Logger logger;
    private static DataSource ds = null;

    /**
     * This method is called when the filter is initialized. It initializes the logger and the superclass.
     * @param config the filter configuration
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        // If you have any <init-param> in web.xml, then you could get them
        // here by config.getInitParameter("name") and assign it as field.
        super.init(config);
        logger = LogManager.getLogger(this.getClass());

    }


    /**
     * This method is called when the filter is destroyed. It destroys the superclass.
     */
    @Override
    public void destroy() {
        super.destroy();
        // If you have assigned any expensive resources as field of
        // this Filter class, then you could clean/close them here.
    }

    /**
     * This method writes an error to the response.
     * @param res the response
     * @param ec the error code
     * @throws IOException
     */
    public void writeError(HttpServletResponse res, ErrorCode ec) throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(ec.toJSON().toString());
    }

    /**
     * This method returns the datasource.
     * @return the datasource
     * @throws NamingException
     */
    public DataSource getDataSource() throws NamingException {

        // we don't want to initialize a new datasource everytime, so, we check first that ds is null
        if (ds == null) {

            //we get the context
            InitialContext ctx = new InitialContext();

            //and use the proper resource to initialize the datasource
            ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/workflix");
        }
        return ds;
    }
}
