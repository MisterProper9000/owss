package openway.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "lesser")
public class Lesser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    private String first_name;
    private String last_name;
    private String company_name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String bank_account;
    private int sum_moto;

    public Lesser() {
    }

    public Lesser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Lesser(String type, String first_name, String last_name,
                  String company_name, String email, String password,
                  String phone, String address, String bank_account, int sum_moto) {
        this.type = type;
        this.first_name = first_name;
        this.last_name = last_name;
        this.company_name = company_name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.bank_account = bank_account;
        this.sum_moto = sum_moto;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}