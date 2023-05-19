package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO class responsible for inserting a template to the database
 */
public class InsertTemplateDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT= "INSERT INTO workflix.template(template_name, image_url) VALUES (?, ?) RETURNING *;";
    /**
     * The connection to the database
     */
    private final Connection connection;
    Template template;

    /**
     * Initialize the DAO object with a connection to the database and the object to be inserted
     *
     * @param connection the connection to the database
     * @param template   the template to be inserted
     */
    public InsertTemplateDatabase(final Connection connection, final Template template){
        this.connection=connection;
        this.template=template;
    }

    /**
     * Inserts the template to the database
     *
     * @return the created template
     * @throws SQLException if an error occurred during the database operation
     */
    public Template insertTemplate ()throws SQLException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        Template newTemplate=null;
        try{
            ps=connection.prepareStatement(STATEMENT);
            ps.setString(1,template.getTemplateName());
            ps.setString(2,template.getImageUrl());
            rs=ps.executeQuery();
            if (rs.next()){
                newTemplate=new Template();
                newTemplate.setTemplateName(rs.getString(Template.TEMPLATE_NAME));
                newTemplate.setImageUrl(rs.getString(Template.IMAGE_URL));
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
        return newTemplate;
    }
}
