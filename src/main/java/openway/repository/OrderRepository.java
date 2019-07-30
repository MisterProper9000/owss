package openway.repository;

import openway.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    //Order findOrderByBegin_time(Date begin_time);
    //Order findOrderById (int id_order);
}
