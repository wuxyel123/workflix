package resource;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Template {

    static final String TEMPLATE_ID = "template_id";
    static final String IMAGE_URL="image_url;";
    static final String IMAGE_NAME="image_name";

    private int TemplateId;
    private String ImageUrl;
    private String ImageName;

    public int getTemplateId() {
        return TemplateId;
    }
    public String getImageUrl(){
        return ImageUrl;
    }
    public String getImageName(){
        return ImageName;
    }

    public void setTemplateId(int TemplateId) {
        this.TemplateId = TemplateId;
    }
    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }
    public void setImageName(String ImageName) {
        this.ImageName = ImageName;
    }


    public static List<Template> fromJSONlist(InputStream inputStream) throws IOException, JSONException {
        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        JSONObject jobj = new JSONObject(dataString);
        List<Template> templates = new ArrayList<>();
        JSONArray templatesJSONList = jobj.getJSONArray("templates-list");

        for(int i=0; i<templatesJSONList.length(); i++){
            templates.add(fromJSON(templatesJSONList.getJSONObject(i)));
        }

        return templates;
    }
    public static Template fromJSON(InputStream inputStream) throws IOException, JSONException {

        String dataString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        return fromJSON(new JSONObject(dataString));
    }

    public static Template fromJSON(JSONObject jobj) throws JSONException {
        int templateId = jobj.getInt(TEMPLATE_ID);
        String imageUrl = jobj.getString(IMAGE_URL);
        String imageName = jobj.getString(IMAGE_NAME);
        Template template=new Template();
        template.setTemplateId(templateId);
        template.setImageUrl(imageUrl);
        template.setImageName(imageName);
        return template;
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject templateJSON = new JSONObject();

        templateJSON.put(TEMPLATE_ID, TemplateId);
        templateJSON.put(IMAGE_URL, ImageUrl);
        templateJSON.put(IMAGE_NAME, ImageName);
        return templateJSON;
    }
}
