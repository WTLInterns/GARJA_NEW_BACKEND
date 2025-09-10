package com.garja.Garja.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.garja.Garja.Model.UserProfile;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile, Integer>{
    
}
