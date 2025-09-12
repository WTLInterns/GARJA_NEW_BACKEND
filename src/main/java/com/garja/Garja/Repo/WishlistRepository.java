package com.garja.Garja.Repo;

import com.garja.Garja.Model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    List<Wishlist> findByUserId(int userId);
    Optional<Wishlist> findByUserIdAndProductId(int userId, int productId);
}
