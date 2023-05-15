package rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ErrorCode;

import java.io.IOException;
import java.sql.Connection;

/**
 * This class represents the abstract REST resource
 */
public abstract class RestResource {

    // The request
    protected final HttpServletRequest req;
    // The response
    protected final HttpServletResponse res;
    // The logger
    protected final Logger logger;
    // The connection to the database
    protected final Connection con;

    /**
     * Constructor
     * @param req The request
     * @param res The response
     * @param con The connection to the database
     */
    public RestResource(HttpServletRequest req, HttpServletResponse res, Connection con){
        this.req = req;
        this.res = res;
        this.con = con;
        this.logger = LogManager.getLogger(this.getClass());
    }

    /**
     * Prototype for the respond method
     * @throws IOException Error in IO operations
     */
    private void respond() throws IOException{}

    /**
     * Prototype for the initError method
     * @param ec The error code
     */
    private void initError(ErrorCode ec){}

}
