package openway.model;

import lombok.Data;

@Data
public class Login {
    private String email;
    private String password;

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}
