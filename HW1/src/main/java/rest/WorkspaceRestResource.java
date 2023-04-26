package rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ErrorCode;

import dao.WorkspaceDeleteByIdDatabase;
import dao.*;

import resource.WorkSpace;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class WorkspaceRestResource extends RestResource {

    protected final String op;
    protected ErrorCode ec = null;
    protected String response = null;
    protected final String[] tokens;

    public WorkspaceRestResource(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(req, res, con);
        op = req.getRequestURI();
        tokens = op.split("/");
    }

    public void DeleteWorkSpace() throws IOException {
        try {
            WorkSpace workSpace = new WorkSpace();

            workSpace.setWorkspaceId(Integer.parseInt(tokens[5]));
            if (new WorkspaceDeleteByIdDatabase(con, workSpace).workspaceDelete() == null) {
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

    public void CreateWorkSpace() throws IOException {
        try {
            WorkSpace workSpace = WorkSpace.fromJSON(req.getInputStream());
            WorkSpace newWorkSpace = new WorkspaceCreate(con, workSpace).insertWorkspace();

            if (newWorkSpace == null) {
                initError(ErrorCode.INTERNAL_ERROR);
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



    public void GetWorkSpace() throws IOException {
        try {
            WorkSpace workSpace = new WorkSpace();

            workSpace.setWorkspaceId(Integer.parseInt(tokens[4]));

            if (new GetWorkspaceByIdDatabase(con, workSpace).getWorkspaceById() == null) {
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


    public void UpdateWorkSpace() throws IOException {
        try {
            WorkSpace workSpace = WorkSpace.fromJSON(req.getInputStream());
            WorkSpace newWorkSpace = new WorkspaceUpdateDatabase(con, workSpace).updateWorkspace();

            if (newWorkSpace == null) {
                initError(ErrorCode.INTERNAL_ERROR);
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

    private void respond() throws IOException {
        res.setStatus(ec.getHTTPCode());
        res.getWriter().write(response);
    }

    private void initError(ErrorCode ec) {
        this.ec = ec;
        response = ec.toJSON().toString();
    }
}
