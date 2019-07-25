package openway.model.dto;

import lombok.Data;

@Data
public class ClientVO {
    private String first_name;
    private String last_name;
    private String email;
    private String phone;

    public ClientVO(String first_name, String last_name,
                    String email, String phone) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
    }
}
