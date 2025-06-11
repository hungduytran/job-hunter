package com.duyhung.jobhunter.repository;

import com.duyhung.jobhunter.domain.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Integer>, JpaSpecificationExecutor<Subscriber> {
    boolean existsByEmail(String email);
    Subscriber findById(long id);
    Subscriber findByEmail(String email);
}
