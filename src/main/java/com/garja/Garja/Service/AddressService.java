package com.garja.Garja.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garja.Garja.Model.Address;
import com.garja.Garja.Model.User;
import com.garja.Garja.Repo.AddressRepo;
import com.garja.Garja.Repo.UserRepo;

@Service
public class AddressService {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;


    public Address addAddress(String email, Address address){
        User user = this.userRepo.findByEmail(email);
        address.setUser(user);
        return this.addressRepo.save(address);
    }

    // public void deleteAddress(int id){

    // }
}
