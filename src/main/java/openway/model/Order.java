package openway.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ord")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date begin_time;
    private Date end_time;
    private int id_motor;
    private int id_client;
    private int tariff = 1;
    private float cost;

    public Order() {
    }

    public Order(Date begin_time, int id_motor, int id_client) {
        this.begin_time = begin_time;
        this.id_motor = id_motor;
        this.id_client = id_client;
    }

    public Order(boolean status, Date end_time) {
        this.end_time = end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public Date getBegin_time() {
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
}