package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateTemplateDatabase {
    private static final String STATEMENT= "UPDATE workflix.template SET image_url=?,template_name=? WHERE template_id=? RETURNING *;";
    private final Connection connection;
    Template template;

    public UpdateTemplateDatabase( final Connection connection, final Template template) {
        this.connection = connection;
        this.template= template;
    }

    public Template updateTemplate ()throws SQLException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        Template updatedTemplate=null;
        try{
            ps=connection.prepareStatement(STATEMENT);
            ps.setString(1,template.getImageUrl());
            ps.setString(2,template.getTemplateName());

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
