package com.JB.couponsproject.repositories;

import com.JB.couponsproject.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,Long> {
    boolean existsByEmail(String email);

    List<CustomerEntity> findByEmail(String email);
}
