package com.garja.Garja.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garja.Garja.DTO.requests.AddressRequest;
import com.garja.Garja.DTO.response.AddressResponse;
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


    public AddressResponse addAddress(String email, AddressRequest request){
        User user = this.userRepo.findByEmail(email);
        Address address = new Address();
        address.setSteet(request.getSteet());
        address.setCity(request.getCity());
        address.setLandmark(request.getLandmark());
        address.setPincode(request.getPincode());
        address.setAddress(request.getAddress());
        address.setUser(user);
        addressRepo.save(address);
        return new AddressResponse(address.getId(),address.getSteet(),address.getCity(),address.getLandmark(),address.getPincode(),address.getAddress());

       
    }

    public void deleteAddress(int id, String email){
        User user = this.userRepo.findByEmail(email);
        this.addressRepo.deleteById(id);
    }

    public List<AddressResponse> getAllAddressByUser(String email) {
    User user = this.userRepo.findByEmail(email);
    if (user == null) {
        throw new RuntimeException("User not found with email: " + email);
    }

    return addressRepo.findAll().stream()
            .filter(a -> a.getUser().getId()==user.getId())
            .map(a -> new AddressResponse(
                   a.getId(),a.getSteet(),a.getCity(),a.getLandmark(),a.getPincode(),a.getAddress()
                
            ))
            .toList();
}







    
}
