package com.garja.Garja.Repo;

import com.garja.Garja.Model.UserOrders;
import com.garja.Garja.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrdersRepo extends JpaRepository<UserOrders, Integer> {
    
    List<UserOrders> findByUser(User user);
    List<UserOrders> findByUserOrderByIdDesc(User user);
}
