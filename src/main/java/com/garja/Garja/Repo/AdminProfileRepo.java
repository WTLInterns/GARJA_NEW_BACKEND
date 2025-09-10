package com.garja.Garja.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.garja.Garja.Model.AdminProfile;

@Repository
public interface AdminProfileRepo extends JpaRepository<AdminProfile, Integer>{
    
}
