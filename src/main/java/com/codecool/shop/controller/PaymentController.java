package com.codecool.shop.controller;

import com.codecool.shop.config.DataBaseManager;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.logger.LoggerService;
import com.codecool.shop.service.CartService;
import com.codecool.shop.service.OrderService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet(urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String cartId = req.getParameter("cart_id");
        String orderId = req.getParameter("order_id");

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        CartDao cartDao = dataBaseManager.getCurrentCartDao();
        OrderDao orderDao = dataBaseManager.getCurrentOrderDao();
        ProductDao productDao = dataBaseManager.getCurrentProductDao();
        OrderService paymentService = new OrderService(cartDao, orderDao, productDao);

        BigDecimal totalPrice = paymentService.getFullPriceForPayment(cartId);

        context.setVariable("totalPrice", totalPrice);
        context.setVariable("order_id", orderId == null ? "0" : orderId);

        engine.process("payment/payment.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String orderId = req.getParameter("order_id");
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();
        CartDao cartDao = dataBaseManager.getCurrentCartDao();
        OrderDao orderDao = dataBaseManager.getCurrentOrderDao();
        ProductDao productDao = dataBaseManager.getCurrentProductDao();
        CartService cartService = new CartService(cartDao, productDao);
        OrderService paymentService = new OrderService(cartDao, orderDao, productDao);
        LoggerService loggerService = new LoggerService(orderDao, cartDao, productDao);
        paymentService.payForOrder(orderId);
        cartService.emptyCart();

        PrintWriter writer = resp.getWriter();
        loggerService.logOrder(Integer.parseInt(orderId));
        writer.println(String.format("order with ID %s paid\n", orderId));
    }
}
