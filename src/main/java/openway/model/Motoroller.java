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
    private boolean insurance;
    private boolean status = false;

    public Motoroller() {
    }

    public Motoroller(String auto_number, String model, int id_owner, boolean insurance, boolean status) {
        this.auto_number = auto_number;
        this.model = model;
        this.id_owner = id_owner;
        this.insurance = insurance;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getId_owner() {
        return id_owner;
    }
}