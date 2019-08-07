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
    private boolean status_rent = false;
    private boolean status_reserv = false;
    private String timeresst;

    public Motoroller() {
    }


    public Motoroller(String auto_number, String model, int idowner, boolean insurance, boolean status_rent) {
        this.auto_number = auto_number;
        this.model = model;
        this.idowner = idowner;
        this.insurance = insurance;
        this.status_rent = status_rent;

    }

    public boolean isRent() {
        return status_rent;
    }

    public boolean isRes() {return status_reserv; }

    public void setStatusRent(boolean statusRent) {
        this.status_rent = statusRent;
    }

    public void setStatusReserve(boolean statusRes){
        this.status_reserv = statusRes;
    }

    public int getIdowner() {
        return idowner;
    }

    public int getId() {
        return id;
    }

    public String gettimeresst() {
        return timeresst;
    }

    public void settimeresst(String timeresst) {
        this.timeresst = timeresst;
    }

    public boolean isInsurance() {
        return insurance;
    }

    public boolean isStatus_rent() {
        return status_rent;
    }

    public boolean isStatus_reserv() {
        return status_reserv;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

    public void setStatus_rent(boolean status_rent) {
        this.status_rent = status_rent;
    }

    public void setStatus_reserv(boolean status_reserv) {
        this.status_reserv = status_reserv;
    }
}