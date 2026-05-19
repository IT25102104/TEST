package com.medicalstore.repository;

import com.medicalstore.entity.Prescription;
import com.medicalstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByUserOrderByUploadDateDesc(User user);
}