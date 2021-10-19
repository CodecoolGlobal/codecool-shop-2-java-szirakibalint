package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.service.ProductService;
import com.codecool.shop.config.TemplateEngineUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        ProductService productService = new ProductService(productDataStore,productCategoryDataStore);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        String categoryIdString = req.getParameter("category_id");
        int categoryId = 1;
        try {
            categoryId = Integer.parseInt(categoryIdString);
        } catch (NumberFormatException e){
            System.out.println("category id wrong format. showing data for category_id=1");
        }

        ProductCategory category = productService.getProductCategory(categoryId);

        if (category != null) {
            context.setVariable("category", category);
            context.setVariable("products", productService.getProductsForCategory(categoryId));
        } else {
            System.out.println("category with id " + categoryId + " does not exist. showing category_id=1");
            context.setVariable("category", productService.getProductCategory(1));
            context.setVariable("products", productService.getProductsForCategory(1));
        }

        // // Alternative setting of the template context
        // Map<String, Object> params = new HashMap<>();
        // params.put("category", productCategoryDataStore.find(1));
        // params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        // context.setVariables(params);

        engine.process("product/index.html", context, resp.getWriter());
    }

}
