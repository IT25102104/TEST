package com.medicalstore.controller;

import com.medicalstore.entity.Medicine;
import com.medicalstore.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    // Show all medicines
    @GetMapping
    public String listMedicines(Model model, @RequestParam(required = false) String search) {
        List<Medicine> medicines;
        if (search != null && !search.isEmpty()) {
            medicines = medicineService.searchMedicines(search);
            model.addAttribute("searchKeyword", search);
        } else {
            medicines = medicineService.getAllMedicines();
        }
        model.addAttribute("medicines", medicines);
        return "medicine-list";
    }

    // Show single medicine details
    @GetMapping("/{id}")
    public String viewMedicine(@PathVariable Long id, Model model) {
        Medicine medicine = medicineService.getMedicineById(id);
        model.addAttribute("medicine", medicine);
        return "medicine-detail";
    }

    // Show form to add new medicine (Admin only)
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("medicine", new Medicine());
        return "medicine-form";
    }

    // Save new medicine (Admin only)
    @PostMapping("/add")
    public String addMedicine(@ModelAttribute Medicine medicine) {
        medicineService.saveMedicine(medicine);
        return "redirect:/medicines";
    }

    // Show form to edit medicine (Admin only)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Medicine medicine = medicineService.getMedicineById(id);
        model.addAttribute("medicine", medicine);
        return "medicine-form";
    }

    // Update medicine (Admin only)
    @PostMapping("/edit/{id}")
    public String updateMedicine(@PathVariable Long id, @ModelAttribute Medicine medicine) {
        medicine.setId(id);
        medicineService.saveMedicine(medicine);
        return "redirect:/medicines";
    }

    // Delete medicine (Admin only)
    @GetMapping("/delete/{id}")
    public String deleteMedicine(@PathVariable Long id) {
        medicineService.deleteMedicine(id);
        return "redirect:/medicines";
    }
}