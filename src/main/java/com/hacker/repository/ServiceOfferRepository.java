package com.hacker.repository;

import com.hacker.domain.ServiceOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOfferRepository extends JpaRepository<ServiceOffer, Integer> {
}