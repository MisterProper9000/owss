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
    private boolean status;
    private Date begin_time;
    private Date end_time;
    private int id_motor;
    private int id_client;
    private float cost;


    public Order() {
    }

    public Order(boolean status, Date begin_time, Date end_time, int id_motor, int id_client, float cost) {
        this.status = status;
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.id_motor = id_motor;
        this.id_client = id_client;
        this.cost = cost;
    }
}
