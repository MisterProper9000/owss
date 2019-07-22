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
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private int sum_moto;

    public Lesser() {
    }

    public Lesser(String type, String name, String email, String password, String phone, String address, int sum_moto) {
        this.type = type;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.sum_moto = sum_moto;
    }
}