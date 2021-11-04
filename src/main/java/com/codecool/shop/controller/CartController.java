package com.codecool.shop.controller;

import com.codecool.shop.config.DataBaseManager;
import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.CartDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.service.CartService;
import org.json.JSONException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(urlPatterns = {"/cart"})
public class CartController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    DataBaseManager dataBaseManager = DataBaseManager.getInstance();
    CartDao cartDao = dataBaseManager.getCurrentCartDao();
    ProductDao productDataStore = dataBaseManager.getCurrentProductDao();
    CartService cartService = new CartService(cartDao, productDataStore);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        String cartId = req.getParameter("cart-id");
        String productId = req.getParameter("product-id");
        JSONObject cartJson = cartService.handleGet(cartId, productId);
        out.println(cartJson);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder jb = new StringBuilder();
        String line;
        BufferedReader reader = req.getReader();
        while ((line = reader.readLine()) != null)
            jb.append(line);
        try {
            JSONObject payload = new JSONObject(jb.toString());
            cartService.handlePost(String.valueOf(payload.get("product_id")), String.valueOf(payload.get("cart_id")));
        } catch (JSONException e) {
            logger.warn("payload received in invalid format: '{}'", jb);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String productId = req.getParameter("product-id");
        String cartId = req.getParameter("cart-id");
        String deleteType = req.getParameter("type");
        cartService.handleDelete(cartId, productId, deleteType);
    }
}
