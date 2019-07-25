package openway.model.dto;

import lombok.Data;

@Data
public class LesserVO {
    private String type;
    private String first_name;
    private String last_name;
    private String company_name;
    private String email;
    private String phone;
    private String address;
    private String bank_account;
    private int sum_moto;

    public LesserVO(String type, String first_name, String last_name,
                    String company_name, String email, String phone,
                    String address, String bank_account, int sum_moto) {
        this.type = type;
        this.first_name = first_name;
        this.last_name = last_name;
        this.company_name = company_name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.bank_account = bank_account;
        this.sum_moto = sum_moto;
    }
}