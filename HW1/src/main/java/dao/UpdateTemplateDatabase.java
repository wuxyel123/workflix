package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is responsible for updating a template in the database
 */
public class UpdateTemplateDatabase {
    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT= "UPDATE workflix.template SET image_url=?,template_name=? WHERE template_id=? RETURNING *;";

    /**
     * The connection to the database
     */
    private final Connection connection;

    /**
     * The template to be updated
     */
    Template template;

    /**
     * Initialize the DAO object with a connection to the database and the object to be updated
     *
     * @param connection the connection to the database
     * @param template   the template to be updated
     */
    public UpdateTemplateDatabase( final Connection connection, final Template template) {
        this.connection = connection;
        this.template= template;
    }

    /**
     * Update the template in the database
     *
     * @return the updated template
     * @throws SQLException if an error occurred while trying to update the template
     */
    public Template updateTemplate ()throws SQLException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        Template updatedTemplate=null;
        try{
            ps=connection.prepareStatement(STATEMENT);
            ps.setString(1,template.getImageUrl());
            ps.setString(2,template.getTemplateName());
            ps.setInt(3,template.getTemplateId());

            rs=ps.executeQuery();
            if (rs.next()){
                updatedTemplate=new Template();
                updatedTemplate.setTemplateName(rs.getString(Template.TEMPLATE_NAME));
                updatedTemplate.setImageUrl(rs.getString(Template.IMAGE_URL));
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
        return updatedTemplate;
    }

}
