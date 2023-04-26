package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListTemplatesDatabase {
    private static final String STATEMENT= "SELECT * FROM workflix.template;";
    private final Connection connection;

    public ListTemplatesDatabase(final Connection connection) {
        this.connection = connection;
    }

    public List<Template> getTemplates ()throws SQLException {
        PreparedStatement ps=null;
        ResultSet rs=null;
        List<Template> templates=new ArrayList<Template>();

        try{
            ps=connection.prepareStatement(STATEMENT);
            rs=ps.executeQuery();

            while (rs.next()){
                Template template = new Template();
                template.setImageUrl(rs.getString(Template.IMAGE_URL));
                template.setTemplateName(rs.getString(Template.TEMPLATE_NAME));
                templates.add(template);
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
        return templates;
    }
}
