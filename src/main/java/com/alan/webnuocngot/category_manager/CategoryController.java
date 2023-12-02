package com.alan.webnuocngot.category_manager;

import com.alan.webnuocngot.admin_manager.AdminController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @Autowired private CategoryService categoryService;

    @Autowired private AdminController adminController;

    @GetMapping("/category-manager")
    public String categoryManager(Model model) {
        if (adminController.getAdminLogin() == null)
            return "redirect:/admin/login";
        model.addAttribute("accMe", adminController.getAdminLogin());

        List<Category> listCategory = categoryService.findAllCategory();
        model.addAttribute("listCategory", listCategory);

        return "category_manager/category_manager";
    }

    @GetMapping("/category-manager/new")
    public String newCategory(Model model) {
        model.addAttribute("category", new Category());

        model.addAttribute("pageTitle", "Add new category");

        return "category_manager/category_form";
    }

    @GetMapping("/category-manager/edit/{id}")
    public String editCategory(@PathVariable("id") String id, Model model) {
        Category category = categoryService.findByidCategory(id);
        model.addAttribute("category", category);

        model.addAttribute("pageTitle", "Edit category {"+ category.getId() +"}");

        return "category_manager/category_form";
    }

    @PostMapping("/category-manager/save")
    public String saveCategory(Category category, RedirectAttributes ra) {
        List<Category> allCategory = categoryService.findAllCategory();
        for (Category categoryTemp: allCategory) {
            if (categoryTemp.getId().equals(category.getId())) {
                ra.addFlashAttribute("message", "Saved is not successfully.\n id "+ category.getId() +"is exist");
                return "redirect:/admin/category-manager/new";
            }
        }
        categoryService.saveCategory(category);

        ra.addFlashAttribute("message", "Saved is successfully.");

        return "redirect:/admin/category-manager";
    }

    @GetMapping("/category-manager/delete/{id}")
    public String deleteCategory(@PathVariable("id") String id, RedirectAttributes ra) {
        categoryService.deleteCategoryByID(id);
        ra.addFlashAttribute("message", "Delete is successfully.");
        return "redirect:/admin/category-manager";
    }
}
