package openway.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "motoroller")
public class Motoroller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String auto_number;
    private String model;
    private int id_owner;

    public Motoroller() {
    }

    public Motoroller(String auto_number, String model, int id_owner) {
        this.auto_number = auto_number;
        this.model = model;
        this.id_owner = id_owner;
    }
}