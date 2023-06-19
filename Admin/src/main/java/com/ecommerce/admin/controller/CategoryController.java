package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String categories(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("size", categories.size());
        model.addAttribute("categoryNew", new Category());
        model.addAttribute("title", "Category");
        return "categories";
    }

    @PostMapping("/add-category")
    public String add(@ModelAttribute("categoryNew") Category category, RedirectAttributes attributes) {
        try {
            categoryService.save(category);
            attributes.addFlashAttribute("success", "Add successfully!");
        } catch (DataIntegrityViolationException e1) {
            e1.getMessage();
            attributes.addFlashAttribute("error", "Duplicate name of category, please check again!");
        } catch (Exception e2) {
            e2.getLocalizedMessage();
            attributes.addFlashAttribute("error",
                    "Error server");
        }
        return "redirect:/categories";

    }

    @RequestMapping(value = "/findById", method = {RequestMethod.PUT, RequestMethod.GET})
    @ResponseBody
    public Category findById(Long id) {
        return categoryService.findById(id);
    }


    @GetMapping("/update-category")
    public String updateCategory(Category category, RedirectAttributes redirectAttributes){

        try {
            categoryService.update(category);
            redirectAttributes.addFlashAttribute("success", "Updated successfully");
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to update because duplicate name");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return "redirect:/categories";

    }

    @RequestMapping(value = "/delete-category", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deleteCategory(Long id, RedirectAttributes redirectAttributes){
        try {
            categoryService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to deleted");
        }


        return "redirect:/categories";

    }
    @RequestMapping(value = "/enable-category", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enableCategory(Long id, RedirectAttributes redirectAttributes){
        try {
            categoryService.enabledById(id);
            redirectAttributes.addFlashAttribute("success", "Enabled successfully");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to enabled");
        }


        return "redirect:/categories";

    }

}
