package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("title", "Manage Product");
        model.addAttribute("products", productDtoList);
        model.addAttribute("size", productDtoList.size());

        return "products";
    }

    @GetMapping("/add-product")
    public String addProductForm(Model model) {
        List<Category> categories = categoryService.findAllByActivated();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductDto());
        return "add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("product") ProductDto productDto,
                              @RequestParam("imageProduct") MultipartFile imageProduct,
                              RedirectAttributes redirectAttributes) throws IOException {
        try {
            productService.save(imageProduct, productDto);
            redirectAttributes.addFlashAttribute("success", "Add successfully!");
        } catch (UncheckedIOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add!");
        } finally {
            imageProduct.getInputStream().close();
        }
        return "redirect:/products";
    }

}
