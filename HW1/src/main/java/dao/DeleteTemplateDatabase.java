package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteTemplateDatabase {
    private static final String STATEMENT= "DELETE FROM workflix.template WHERE template_id=?;";
    private final Connection connection;
    Template template;
    public DeleteTemplateDatabase (final Connection connection, final Template template){
        this.connection=connection;
        this.template=template;
    }
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
