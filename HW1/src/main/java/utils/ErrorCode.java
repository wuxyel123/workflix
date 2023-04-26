package utils;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
public enum ErrorCode {
    OK(0, HttpServletResponse.SC_OK,"OK."),
    USER_NOT_FOUND(-100, HttpServletResponse.SC_NOT_FOUND, "User not found."),
    USER_ALREADY_EXISTS(-101, HttpServletResponse.SC_CONFLICT, "User already exists."),
    USER_NOT_AUTHORIZED(-102, HttpServletResponse.SC_UNAUTHORIZED, "User not authorized."),
    TEMPLATE_NOT_FOUND(-100, HttpServletResponse.SC_NOT_FOUND, "Template not found."),
    TEMPLATE_INFORMATION_MISSING(-400, HttpServletResponse.SC_BAD_REQUEST, "Template information missing."),
    INTERNAL_ERROR(-999, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error.");

    private final int errorCode;
    private final int httpCode;
    private final String errorMessage;

    ErrorCode(int errorCode, int httpCode, String errorMessage) {
        this.errorCode = errorCode;
        this.httpCode = httpCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getHTTPCode() {
        return httpCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        data.put("code", errorCode);
        data.put("message", errorMessage);
        JSONObject info = new JSONObject();
        info.put("error", data);
        return info;
    }
}
