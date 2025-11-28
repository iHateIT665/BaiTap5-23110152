package vn.iotstar.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import vn.iotstar.entity.Category;
import vn.iotstar.model.CategoryModel;
import vn.iotstar.service.CategoryService;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    // Hien thi danh sach category
    @RequestMapping("")
    public String list(ModelMap model) {

        List<Category> list = categoryService.findAll();
        model.addAttribute("categories", list);
        return "admin/categories/list";

    }

    // Hien thi form ADD
    @GetMapping("add")
    public String add(ModelMap model) {
        CategoryModel cateModel = new CategoryModel();
        cateModel.setIsEdit(false);

        model.addAttribute("category", cateModel);
        return "admin/categories/addOrEdit";
    }

    // Save and Update
    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryModel cateModel,
            BindingResult result) {

        if (result.hasErrors()) {
            return new ModelAndView("admin/categories/addOrEdit");

        }
        // Tao Entity save
        Category entity = new Category();
        BeanUtils.copyProperties(cateModel, entity);
        categoryService.save(entity);

        // 4. Gửi thông báo cho người dùng
        String message = cateModel.getIsEdit() ? "Category is Edited!!!!!!!!" : "Category is saved!!!!!!!!";

        model.addAttribute("message", message);

        // 5. Điều hướng về danh sách
        return new ModelAndView("forward:/admin/categories", model);
    }

    // Hien thi form edit
    @GetMapping("edit/{id}")
    public ModelAndView edit(ModelMap model, @PathVariable("id") Long id) {

        Optional<Category> optCategory = categoryService.findById(id);

        CategoryModel cateModel = new CategoryModel();

        if (optCategory.isPresent()) {

            Category entity = optCategory.get();

            BeanUtils.copyProperties(entity, cateModel);

            cateModel.setIsEdit(true);

            model.addAttribute("category", cateModel);

            return new ModelAndView("admin/categories/addOrEdit", model);
        }

        model.addAttribute("message", "Category is not existed!!!!");

        return new ModelAndView("forward:/admin/categories", model);
    }

    // Xoa category
    @GetMapping("delete/{id}")
    public ModelAndView delete(ModelMap model, @PathVariable("id") Long id) {

        categoryService.deleteById(id);

        model.addAttribute("message", "Category is deleted!!!!");
        return new ModelAndView("redirect:/admin/categories", model);
    }

}
