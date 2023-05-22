package rest;

import dao.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import resource.User;
import utils.ErrorCode;

import resource.WorkSpace;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * This class represents the REST resource "/workspace"
 */
public class WorkspaceRestResource extends RestResource {

    // The following strings represent the different operations that the REST
    protected final String op;
    // The error code
    protected ErrorCode ec = ErrorCode.OK;
    // The response
    protected String response = null;
    // The tokens of the request
    protected final String[] tokens;

    /**
     * Constructor
     * @param req The request
     * @param res The response
     * @param con The connection to the database
     */
    public WorkspaceRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }

    /**
     * DeleteWorkSpace by id
     * @throws IOException
     * */
    public void DeleteWorkSpace() throws IOException {
        try {
            WorkSpace workSpace = new WorkSpace();

            workSpace.setWorkspaceId(Integer.parseInt(tokens[5]));
            if (new DeleteWorkspaceByIdDatabase(con, workSpace).workspaceDelete() == null) {
                initError(ErrorCode.INTERNAL_ERROR);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }

    }

    /**
     * CreateWorkSpace
     * @throws IOException
     * */
    public void CreateWorkSpace() throws IOException {
        try {
            WorkSpace workSpace = WorkSpace.fromJSON(req.getInputStream());
            WorkSpace newWorkSpace = new InsertWorkspaceDatabase(con, workSpace).insertWorkspace();

            if (newWorkSpace == null) {
                initError(ErrorCode.WORKSPACE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                res.setContentType("application/json");
                response = newWorkSpace.toJSON().toString();
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }

    }


    /**
     * GetWorkSpace by id
     * @throws IOException
     * */
    public void GetWorkSpace() throws IOException {
        try {
            WorkSpace workSpace = new WorkSpace();

            workSpace.setWorkspaceId(Integer.parseInt(tokens[4]));

            if (new GetWorkspaceByIdDatabase(con, workSpace).getWorkspaceById() == null) {
                initError(ErrorCode.WORKSPACE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }

    }

    /**
     * Get workspaces by user id
     * @throws IOException
     */
    public void GetWorkSpacesByUserId() throws IOException {
        try {
            User user = User.fromJSON(req.getInputStream());
            user.setUserId(Integer.parseInt(tokens[4]));
            List<WorkSpace> workSpaces = new GetWorkspaceByUserIdDatabase(con, user).getWorkspaceByUserId();

            if (workSpaces == null || workSpaces.isEmpty()) {
                initError(ErrorCode.WORKSPACE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                response = WorkSpace.toJSONlist(workSpaces).toString();

            }
        } catch (SQLException e) {
            initError(ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        } finally {
            respond();
        }
    }

    /**
     * UpdateWorkSpace by id
     * @throws IOException
     * */
    public void UpdateWorkSpace() throws IOException {
        try {
            WorkSpace workSpace = WorkSpace.fromJSON(req.getInputStream());
            workSpace.setWorkspaceId(Integer.parseInt(tokens[5]));
            WorkSpace newWorkSpace = new UpdateWorkspaceDatabase(con, workSpace).updateWorkspace();

            if (newWorkSpace == null) {
                initError(ErrorCode.WORKSPACE_NOT_FOUND);
            } else {
                ec = ErrorCode.OK;
                response = newWorkSpace.toJSON().toString();

            }
        } catch (SQLException e) {
            ec = ErrorCode.OK;
            res.setContentType("application/json");
        } finally {
            respond();
        }

    }

    /**
     * Respond to the request
     * @throws IOException
     */
    private void respond() throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(response);
    }

    /**
     * Initialize the error
     * @param ec The error code
     */
    private void initError(ErrorCode ec) {
        this.ec = ec;
        response = ec.toJSON().toString();
    }
}
