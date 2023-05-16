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
import java.util.*;

public class TemplateServlet extends AbstractServlet{
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("template") + 9);

        switch (op){
            case "get/":
                try{
                    List<Template> templates = new GetTemplatesDatabase(getDataSource().getConnection()).getTemplates();
                    JSONObject resJSON = new JSONObject();
                    resJSON.put("template-list", templates);
                    res.setStatus(HttpServletResponse.SC_OK);
                    res.setContentType("application/json");
                    res.getWriter().write((new JSONObject()).put("data", resJSON).toString());
                } catch (NamingException | SQLException e){
                    writeError(res, ErrorCode.INTERNAL_ERROR);
                }
                break;
            default:
                writeError(res, ErrorCode.INTERNAL_ERROR);
                logger.warn("requested op "+op);
        }

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("template")+9);

        switch (op){
            case "update/":
                updateOperations(req, res);
                break;
            case "create/":
                insertionOperations(req, res);
                break;
            default:
                writeError(res, ErrorCode.INTERNAL_ERROR);
                logger.warn("requested op "+op);
        }

    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        deleteOperations(req, res);
    }


    private void insertionOperations(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
        Message m = null;
        try {
            String image_url = req.getParameter("image_url");
            String template_name = req.getParameter("template_name");
            Template template;
            ErrorCode ec = null;
//            String dispatchPage = null;
            boolean addTemplate = true;
            if (template_name == null || template_name.equals("") || image_url == null || image_url.equals("")) {
                ec = ErrorCode.TEMPLATE_INFORMATION_MISSING;
                m = new Message(true, ec.getErrorMessage());
//                dispatchPage = "/jsp/builder-area/edit-park.jsp";
            } else {
                template=new Template();
                template.setImageUrl(image_url);
                template.setTemplateName(template_name);

                if (addTemplate) {
                    template = new InsertTemplateDatabase(getDataSource().getConnection(), template).insertTemplate();
                    if (template != null) {
                        m = new Message(false, ec.getErrorMessage());
                        ec = ErrorCode.TEMPLATE_CREATED;
                    } else if (new GetTemplateByNameDatabase(getDataSource().getConnection(), template).getTemplateByName()==null) {
                        ec = ErrorCode.TEMPLATE_NAME_ALREADY_EXIST;
                        req.setAttribute("message", new Message(true, ec.getErrorMessage()));
                        m = new Message(true, ec.getErrorMessage());
                    }  else {
                        writeError(res, ErrorCode.INTERNAL_ERROR);
                        logger.error("unknown error: " + req.getRequestURL());
                    }
                }
            }
            res.setStatus(ec.getHTTPCode());
            req.setAttribute("message", m);
//            req.getRequestDispatcher(dispatchPage).forward(req, res);

        } catch (NamingException | SQLException e){
            writeError(res, ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        }
    }

    private void updateOperations(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            String template_id = req.getParameter("template_id");
            Template template;
            ErrorCode ec = null;
            Message m = null;
            boolean updateTemplate = true;

            if (template_id == null || template_id.equals("")) {
                updateTemplate=false;
                ec = ErrorCode.BAD_REQUEST;
                m = new Message(true, ec.getErrorMessage());
            } else {
                template=new Template();
                int id=Integer.parseInt(template_id);
                template.setTemplateId(id);

                if (new GetTemplateByIdDatabase(getDataSource().getConnection(), template).getTemplateById()==null){
                    updateTemplate = false;
                    ec = ErrorCode.TEMPLATE_NOT_FOUND;
                    m = new Message(true, ec.getErrorMessage());
                }

                if (updateTemplate){
                    template = new UpdateTemplateDatabase(getDataSource().getConnection(), template).updateTemplate();
                    if (template!=null) {
                        ec = ErrorCode.OK;
                        m = new Message(true, "template updated correctly");
                    } else {
                        writeError(res, ErrorCode.INTERNAL_ERROR);
                        logger.error("problem when updating template: " + req.getRequestURL());
                    }
                }
            }

            req.setAttribute("message", m);
            res.setStatus(ec.getHTTPCode());
//            req.getRequestDispatcher(dispatchPage).forward(req, res);

        } catch (NamingException | SQLException e) {
            writeError(res, ErrorCode.INTERNAL_ERROR);
            logger.error("stacktrace:", e);
        }
    }

    private void deleteOperations(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        String template_id = req.getParameter("template_id");
        Template template;
        try {
            if (template_id == null || template_id.equals("")) {
                Message m = new Message(true, "template not found");
                ErrorCode ec = ErrorCode.TEMPLATE_NOT_FOUND;
                res.setStatus(ec.getHTTPCode());
                req.setAttribute("message", m);
//                req.getRequestDispatcher("/jsp/builder-area/edit-park.jsp").forward(req, res);
            } else {
                template=new Template();
                int id=Integer.parseInt(template_id);
                template.setTemplateId(id);

                template = new DeleteTemplateDatabase(getDataSource().getConnection(), template).deleteTemplate();
                if (template != null) {
                    logger.error("template deleted correctly");
                    Message m = new Message(true, "template deleted correctly");
                    res.setStatus(HttpServletResponse.SC_OK);

                    res.getWriter().write(m.toJSON().toString());
                } else {
                    ErrorCode ec = ErrorCode.INTERNAL_ERROR;
                    writeError(res, ec);
                    logger.error("problem when deleting template: " + req.getRequestURL());
                }
            }
        } catch (SQLException | NamingException e) {
            ErrorCode ec = ErrorCode.INTERNAL_ERROR;
            writeError(res, ec);
            logger.error("stacktrace:", e);
        }
    }

}
