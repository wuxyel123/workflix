package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddTemplateDatabase {
    private static final String STATEMENT= "INSERT INTO workflix.template (image_url,image_name) VALUES (image_url,image_name) RETURNING *;";
    private final Connection connection;
    Template template;
    public AddTemplateDatabase (final Connection connection, final Template template){
        this.connection=connection;
        this.template=template;
    }
    public Template addTemplate ()throws SQLException {
        PreparedStatement ps=null;
        ResultSet rs=null;

        Template newTemplate=null;
        try{
            ps=connection.prepareStatement(STATEMENT);
            ps.setString(1,template.getImageName());
            rs=ps.executeQuery();
            if (rs.next()){
                newTemplate=new Template();
                newTemplate.setImageName(rs.getString(Template.IMAGE_NAME));
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
