package com.codecool.shop.config;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.math.BigDecimal;

@WebListener
public class Initializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();


        Supplier mojang = new Supplier("Mojang Studios", "Swedish video game company.");
        supplierDataStore.add(mojang);
        Supplier ea = new Supplier("EA", "At the intersection of arts and eletronics.");
        supplierDataStore.add(ea);
        Supplier se = new Supplier("Square Enix", "They have a cool name.");
        supplierDataStore.add(se);
        Supplier other = new Supplier("Square Enix", "Everything else.");
        supplierDataStore.add(other);


        ProductCategory adventure = new ProductCategory("Adventure", "Games", "Pack your bag, say goodbye to your friends in the Shire.");
        productCategoryDataStore.add(adventure);

        ProductCategory sandbox = new ProductCategory("Sandbox", "Games", "Unleash your creativity.");
        productCategoryDataStore.add(sandbox);

        ProductCategory strategy = new ProductCategory("Strategy", "Games", "Strategy games requier thinking. In advance.");
        productCategoryDataStore.add(strategy);


        productDataStore.add(new Product("Tomb Raider", new BigDecimal("5.99"), "USD", "The scariest one.", adventure, se));
        productDataStore.add(new Product("Tomb Raider 2", new BigDecimal("5.99"), "USD", "Make Lara do cool jumps.", adventure, se));
        productDataStore.add(new Product("Tomb Raider 3", new BigDecimal("5.99"), "USD", "Lots of swimming.", adventure, se));
        productDataStore.add(new Product("Tomb Raider: The Last Revelation", new BigDecimal("5.99"), "USD", "Does she even raid tombs?", adventure, se));
        productDataStore.add(new Product("Minecraft", new BigDecimal("19.99"), "USD", "Build a house made of dirt.", sandbox, mojang));
        productDataStore.add(new Product("Battle for Wesnoth", new BigDecimal("0.00"), "USD", "Wizards. Orcs. Trolls. Mermaids. Open-source turn-based strategy game.", strategy, other));
    }
}
