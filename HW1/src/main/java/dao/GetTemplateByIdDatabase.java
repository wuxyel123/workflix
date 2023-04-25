package dao;

import resource.Template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetTemplateByIdDatabase {
    private static final String STATEMENT= "SELECT FROM workflix.template WHERE template_id*;";
    private final Connection connection;
    Template template;

    public GetTemplateByIdDatabase(Connection connection) {
        this.connection = connection;
        this.template=template;
    }

    public Template getTemplateById ()throws SQLException {
        PreparedStatement ps=null;
        ResultSet rs=null;

        Template foundTemplate=null;
        try{
            ps=connection.prepareStatement(STATEMENT);
            ps.setInt(1,template.getTemplateId());
            rs=ps.executeQuery();
            if (rs.next()){
                foundTemplate=new Template();
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
        return foundTemplate;
    }
}
