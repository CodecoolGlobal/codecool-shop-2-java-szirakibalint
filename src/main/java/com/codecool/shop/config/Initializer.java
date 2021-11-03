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
        Supplier ea = new Supplier("EA", "At the intersection of arts and electronics.");
        supplierDataStore.add(ea);
        Supplier se = new Supplier("Square Enix", "They have a cool name.");
        supplierDataStore.add(se);
        Supplier other = new Supplier("Other", "Everything else.");
        supplierDataStore.add(other);
        Supplier cc = new Supplier("Codecool", "Exciting games from our best & brightest.");
        supplierDataStore.add(cc);


        ProductCategory adventure = new ProductCategory("Action, adventure", "Games", "Pack your bag, say goodbye to your friends in the Shire.");
        productCategoryDataStore.add(adventure);

        ProductCategory sandbox = new ProductCategory("Sandbox, simulation", "Games", "Unleash your creativity.");
        productCategoryDataStore.add(sandbox);

        ProductCategory strategy = new ProductCategory("Strategy", "Games", "Strategy games requier thinking. In advance.");
        productCategoryDataStore.add(strategy);


        productDataStore.add(new Product("Tomb Raider", new BigDecimal("5.99"), "USD", "The scariest one.", adventure, se));
        productDataStore.add(new Product("Tomb Raider 2", new BigDecimal("5.99"), "USD", "Make Lara do cool jumps.", adventure, se));
        productDataStore.add(new Product("Tomb Raider 3", new BigDecimal("5.99"), "USD", "Lots of swimming.", adventure, se));
        productDataStore.add(new Product("Tomb Raider: The Last Revelation", new BigDecimal("5.99"), "USD", "Does she even raid tombs?", adventure, se));
        productDataStore.add(new Product("Minecraft", new BigDecimal("19.99"), "USD", "Build a house made of dirt.", sandbox, mojang));
        productDataStore.add(new Product("The Battle for Wesnoth", new BigDecimal("0.00"), "USD", "Wizards. Orcs. Trolls. Mermaids. Open-source turn-based strategy game.", strategy, other));
        productDataStore.add(new Product("Journey to the Center of the Earth", new BigDecimal("1.99"), "USD", ".", adventure, other));
        productDataStore.add(new Product("Private Static Final Fantasy 2", new BigDecimal("4.20"), "USD", "Fantasy_final_v2_finalversion_2.exe", adventure, cc));
        productDataStore.add(new Product("Terraria", new BigDecimal("9.20"), "USD", "Farming, I guess?", sandbox, other));
        productDataStore.add(new Product("Yu-Gi-Oh! Power of Chaos: Yugi the Destiny (2003)", new BigDecimal("1.20"), "USD", "Try to play your cards right.", strategy, other));
        productDataStore.add(new Product("The Sims 2", new BigDecimal("19.99"), "USD", "Throw a pool party.", sandbox, ea));
        productDataStore.add(new Product("Minecraft Dungeons", new BigDecimal("9.99"), "USD", "", adventure, mojang));
        productDataStore.add(new Product("Fifa 2004", new BigDecimal("9.99"), "USD", "Great music.", sandbox, ea));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
