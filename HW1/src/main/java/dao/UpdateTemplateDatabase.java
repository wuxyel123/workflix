package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateTemplateDatabase {
    private static final String STATEMENT= "UPDATE workflix.template SET image_url=?,image_name=? WHERE template_id=? RETURNING *;";
    private final Connection connection;
    Template template;

    public UpdateTemplateDatabase(Connection connection) {
        this.connection = connection;
        this.template= template;
    }

    public Template updateTemplate ()throws SQLException {
        PreparedStatement ps=null;
        ResultSet rs=null;

        Template updatedTemplate=null;
        try{
            ps=connection.prepareStatement(STATEMENT);
            ps.setString(1,template.getImageName());
            ps.setString(2,template.getImageUrl());
            rs=ps.executeQuery();
            if (rs.next()){
                updatedTemplate=new Template();
                updatedTemplate.setImageName(rs.getString(Template.IMAGE_NAME));
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
