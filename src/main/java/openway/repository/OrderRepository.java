package openway.repository;

import openway.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderById (int id_order);
    Order deleteById(int id_order);
    List<Order> findOrderByBegin_timeIsNull();
}
