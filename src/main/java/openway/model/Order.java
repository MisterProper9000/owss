package openway.model;

import lombok.Data;
import sun.util.calendar.BaseCalendar;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ord")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String begin_time;
    private String end_time;
    private int id_moto;
    private int id_client;
    private double tariff = 1;
    private float cost;
    private String RRN;

    public Order() {
    }

    public Order(String begin_time, int id_moto, int id_client, double tariff) {
        this.begin_time = begin_time;
        this.id_moto = id_moto;
        this.id_client = id_client;
        this.tariff = tariff;
    }

    public Order(String begin_time, int id_moto, int id_client, double tariff, String RRN) {
        this.begin_time = begin_time;
        this.id_moto = id_moto;
        this.id_client = id_client;
        this.tariff = tariff;
        this.RRN = RRN;
    }


    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public int getId() {
        return id;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public double getTariff() {
        return tariff;
    }

    public int getId_moto() {
        return id_moto;
    }

    public int getId_client() {
        return id_client;
    }

    public float getCost() {
        return cost;
    }

    public String getRRN() {
        return RRN;
    }

    public String getEnd_time() {
        return end_time;
    }
}