package com.medicalstore.service;

import com.medicalstore.entity.Medicine;
import com.medicalstore.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    // Get all medicines
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    // Get medicine by ID (returns Medicine directly, throws if not found)
    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with ID: " + id));
    }

    // Save or update medicine
    public Medicine saveMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    // Delete medicine
    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }

    // Search medicines by name
    public List<Medicine> searchMedicines(String keyword) {
        return medicineRepository.findByNameContainingIgnoreCase(keyword);
    }

    // Get medicines by category
    public List<Medicine> getMedicinesByCategory(String category) {
        return medicineRepository.findByCategory(category);
    }

    // Count all medicines
    public long countMedicines() {
        return medicineRepository.count();
    }

    // Increase stock (admin adds inventory)
    public void updateStock(Long medicineId, int quantityChange) {
        Medicine medicine = getMedicineById(medicineId);
        medicine.setStockQuantity(medicine.getStockQuantity() + quantityChange);
        medicineRepository.save(medicine);
    }

    // Reduce stock when a customer places an order
    public void reduceStock(Long medicineId, int quantity) {
        Medicine medicine = getMedicineById(medicineId);
        if (medicine.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock for " + medicine.getName());
        }
        medicine.setStockQuantity(medicine.getStockQuantity() - quantity);
        medicineRepository.save(medicine);
    }

    // Check if medicine is in stock
    public boolean isInStock(Long medicineId) {
        Medicine medicine = getMedicineById(medicineId);
        return medicine.getStockQuantity() > 0;
    }
}