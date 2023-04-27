package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertTemplateDatabase {

    /**
     * The SQL statement to be executed
     */
    private static final String STATEMENT= "INSERT INTO workflix.template(template_name, image_url) VALUES (?, ?) RETURNING *;";
    private final Connection connection;
    Template template;
    public InsertTemplateDatabase(final Connection connection, final Template template){
        this.connection=connection;
        this.template=template;
    }
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
