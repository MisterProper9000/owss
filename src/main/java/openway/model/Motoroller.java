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
    private int idowner;
    private boolean insurance;
    private boolean status = false;

    public Motoroller() {
    }

    public Motoroller(String auto_number, String model, int idowner, boolean insurance, boolean status) {
        this.auto_number = auto_number;
        this.model = model;
        this.idowner = idowner;
        this.insurance = insurance;
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public int getIdowner() {
        return idowner;
    }

    public int getId() {
        return id;
    }
}