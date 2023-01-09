package com.unibuc.fmi.mycinema.repository;

import com.unibuc.fmi.mycinema.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE '%' || ?1 || '%' OR LOWER(c.email) LIKE '%' || ?1 || '%'")
    List<Customer> searchByNameOrEmail(String searchParam);
}
