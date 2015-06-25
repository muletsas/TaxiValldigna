package enguix.mulet.taxivalldigna.Entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 17/05/15.
 */
public class UserEntity {
    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private String user;
    private String contact_name;
    private String phone;
    private String password;
    private String code_gcm;


    public UserEntity() {
    }



    public UserEntity(String user, String password) {
        this.user = user;
        this.password = password;
    }




    public  boolean validateEmail() {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(this.user);
        return matcher.matches();

    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode_gcm() {
        return code_gcm;
    }

    public void setCode_gcm(String code_gcm) {
        this.code_gcm = code_gcm;
    }


}
