package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDao = SupplierDaoMem.getInstance();
        ProductService productService = new ProductService(productDataStore,productCategoryDataStore,supplierDao);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("category", productService.getProductCategory(1));
        context.setVariable("products", productService.getProductsForCategory(1));


        //get category id from url if it exists
        String categoryIdString = req.getParameter("category_id");
        if (categoryIdString != null){
            int categoryId = 1;
            try {
                categoryId = Integer.parseInt(categoryIdString);
            } catch (NumberFormatException e) {
                resp.sendRedirect("/");
            }
            ProductCategory category = productService.getProductCategory(categoryId);
            if (category != null) {
                context.setVariable("category", category);
                context.setVariable("products", productService.getProductsForCategory(categoryId));
            } else {
                resp.sendRedirect("/");
            }
        }


        //get supplier id from url if it exists
        String supplierIdString = req.getParameter("supplier_id");
        if (supplierIdString != null){
            int supplierId = 1;
            try {
                supplierId = Integer.parseInt(supplierIdString);
            } catch (NumberFormatException e) {
                resp.sendRedirect("/");
            }
            Supplier supplier = productService.getProductSupplier(supplierId);
            if (supplier != null) {
                context.setVariable("supplier", supplier);
                context.setVariable("products", productService.getProductsForSupplier(supplierId));
            } else {
                resp.sendRedirect("/");
            }
        }

        context.setVariable("categories", productCategoryDataStore.getAll());
        context.setVariable("suppliers", supplierDao.getAll());
        engine.process("product/index.html", context, resp.getWriter());
    }

}
