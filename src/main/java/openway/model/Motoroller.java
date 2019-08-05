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
    private boolean status_rent = false;
    private boolean status_reserv = false;
    private String time_res_st;

    public Motoroller() {
    }

    public Motoroller(String auto_number, String model, int id_owner, boolean insurance, boolean status_rent) {
        this.auto_number = auto_number;
        this.model = model;
        this.id_owner = id_owner;
        this.insurance = insurance;
        this.status_rent = status_rent;
    }

    public Motoroller(String auto_number, String model, int id_owner, boolean insurance, boolean status_rent,
                      String time_res_st) {
        this.auto_number = auto_number;
        this.model = model;
        this.id_owner = id_owner;
        this.insurance = insurance;
        this.status_rent = status_rent;
        this.time_res_st = time_res_st;
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

    public int getId_owner() {
        return id_owner;
    }

    public int getId() {
        return id;
    }

    public String getTime_res_st() {
        return time_res_st;
    }

    public void setTime_res_st(String time_res_st) {
        this.time_res_st = time_res_st;
    }
}