package com.garja.Garja.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.garja.Garja.Model.Address;


@Repository
public interface AddressRepo extends JpaRepository<Address, Integer>{
    
}
