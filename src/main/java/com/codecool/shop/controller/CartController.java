package com.codecool.shop.controller;

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
import java.util.List;

import org.json.JSONObject;

@WebServlet(urlPatterns = {"/cart"})
public class CartController extends HttpServlet {

    CartDao cartDao = CartDaoMem.getInstance();
    ProductDao productDataStore = ProductDaoMem.getInstance();
    CartService cartService = new CartService(cartDao, productDataStore);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        String userId = req.getParameter("user-id");
        List<JSONObject> cartJson = cartService.getCartContent(userId);
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
            cartService.addToCart(String.valueOf(payload.get("product_id")), null);
        } catch (JSONException e) {
            System.out.println("Error parsing JSON request string");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String productId = req.getParameter("product-id");
        String userId = req.getParameter("user-id");
        String deleteType = req.getParameter("type");
        cartService.handleDelete(userId, productId, deleteType);
    }
}
