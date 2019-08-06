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
    private int idmoto;
    private int idclient;
    private double tariff = 1;
    private int tariff_time;
    private float cost;
    private String RRN;

    public Order() {
    }

    public Order(String begin_time, int idmoto, int idclient, double tariff) {
        this.begin_time = begin_time;
        this.idmoto = idmoto;
        this.idclient = idclient;
        this.tariff = tariff;
    }

    public Order(int id_moto, int idclient, String RRN){
        this.idmoto = id_moto;
        this.idclient = idclient;
        this.RRN = RRN;
    }


    public Order(String begin_time, int idmoto, int idclient, double tariff, int tariff_time, String RRN) {
        this.begin_time = begin_time;
        this.idmoto = idmoto;
        this.idclient = idclient;
        this.tariff = tariff;
        this.tariff_time = tariff_time;
        this.RRN = RRN;
    }

    public Order(String begin_time, String end_time, int id_moto, int idclient, double tariff, float cost, String RRN) {
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.idmoto = id_moto;
        this.idclient = idclient;
        this.tariff = tariff;
        this.cost = cost;
        this.RRN = RRN;
    }

    public Order(String begin_time, String end_time, int idmoto, int idclient, double tariff, int tariff_time, float cost, String RRN) {
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.idmoto = idmoto;
        this.idclient = idclient;
        this.tariff = tariff;
        this.tariff_time = tariff_time;
        this.cost = cost;
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
        return idmoto;
    }

    public int getIdclient() {
        return idclient;
    }

    public float getCost() {
        return cost;
    }

    public String getRRN() {
        return RRN;
    }

    public int getTariff_time() {
        return tariff_time;
    }

    public String getEnd_time() {
        return end_time;
    }
}