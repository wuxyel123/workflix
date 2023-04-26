package servlet;

import dao.GetAnalyticsDatabaseByWorkspaceId;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import resource.Analytics;
import resource.Message;
import utils.ErrorCode;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AnalyticsServlet extends AbstractServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("analytics") + 5);

        switch (op) {
            case "get/":
                String workspace_id = req.getParameter("workspace_id");
                if (workspace_id == null || workspace_id.equals("")) {
                    ErrorCode ec = ErrorCode.WORKSPACE_NOT_FOUND;
                }
                try {
                    Analytics analytics = new Analytics();
                    analytics = new GetAnalyticsDatabaseByWorkspaceId(getDataSource().getConnection(), analytics).getAnalytics();
                    if (analytics == null) {
                        ErrorCode ec = ErrorCode.ANALYTICS_NOT_FOUND;
                        Message m = new Message(true, "analytics not found");
                        res.setStatus(ec.getHTTPCode());
                    } else {
                        req.setAttribute("analytics", analytics);
                        res.setStatus(200);
//                        req.getRequestDispatcher("/jsp/builder-area/edit-model.jsp").forward(req, res);
                    }
                } catch (NamingException | SQLException e) {
                    writeError(res, ErrorCode.INTERNAL_ERROR);
                }
                break;
            default:
                writeError(res, ErrorCode.INTERNAL_ERROR);
                logger.warn("requested op " + op);
        }
    }
}



