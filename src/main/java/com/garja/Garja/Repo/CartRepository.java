package com.garja.Garja.Repo;

import com.garja.Garja.Model.Cart;
import com.garja.Garja.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
