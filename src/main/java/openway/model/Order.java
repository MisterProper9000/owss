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
    private int tariff = 1;
    private float cost;

    public Order() {
    }

    public Order(String begin_time, int id_moto, int id_client, int tariff) {
        this.begin_time = begin_time;
        this.id_moto = id_moto;
        this.id_client = id_client;
        this.tariff = tariff;
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

    public int getTariff() {
        return tariff;
    }

    public int getId_moto() {
        return id_moto;
    }

    public String getEnd_time() {
        return end_time;
    }
}