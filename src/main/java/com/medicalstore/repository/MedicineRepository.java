package com.medicalstore.repository;

import com.medicalstore.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    // Search medicines by name containing keyword (case insensitive)
    List<Medicine> findByNameContainingIgnoreCase(String keyword);

    // Find medicines by category
    List<Medicine> findByCategory(String category);

    // Find medicines that require prescription
    List<Medicine> findByRequiresPrescription(boolean requiresPrescription);
}