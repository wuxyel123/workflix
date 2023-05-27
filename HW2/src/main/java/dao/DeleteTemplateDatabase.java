package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to delete a template from the database
 * It takes a connection to the database and the template object to be deleted as input
 * It returns the deleted template object
 */
public class DeleteTemplateDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT= "DELETE FROM workflix.template WHERE template_id=?;";
    /**
     * The connection to the database
     */
    private final Connection connection;
    /**
     * The template object to be deleted
     */
    Template template;

    /**
     * Initialize the DAO object with a connection to the database and the object to be deleted
     * @param connection
     * @param template
     */
    public DeleteTemplateDatabase (final Connection connection, final Template template){
        this.connection=connection;
        this.template=template;
    }

    /**
     * Delete the template from the database
     * @return the deleted template object
     * @throws SQLException
     */
    public Template deleteTemplate ()throws SQLException{
        PreparedStatement ps=null;
        ResultSet rs=null;

        Template deletedTemplate=null;
        try{
            ps=connection.prepareStatement(STATEMENT);
            ps.setInt(1,template.getTemplateId());
            rs=ps.executeQuery();
            if (rs.next()){
                deletedTemplate=new Template();
                deletedTemplate.setTemplateId(rs.getInt(Template.TEMPLATE_ID));
                deletedTemplate.setTemplateName(rs.getString(Template.TEMPLATE_NAME));
                deletedTemplate.setImageUrl(rs.getString(Template.IMAGE_URL));
            }
        }finally {
            if (rs!=null){
                rs.close();
            }
            if (ps!=null){
                ps.close();
            }
            connection.close();
        }
        return deletedTemplate;
    }
}
