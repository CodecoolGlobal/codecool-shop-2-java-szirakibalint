package com.codecool.shop.service;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.InvalidOrder;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

public class OrderServiceTest {
    OrderService orderService;
    OrderDao orderDao ;
    CartDao cartDao ;
    ProductDao productDao;

    @BeforeEach
    public void setup(){

        orderDao = Mockito.mock(OrderDao.class);
        cartDao = Mockito.mock(CartDao.class);
        productDao = Mockito.mock(ProductDao.class);

        Mockito.when(cartDao.getCartById(2)).thenReturn(new Cart());

        orderService = new OrderService(cartDao, orderDao, productDao);
    }

    @Test
    public void getFullPrice_withEmptyCart_returns0(){
        BigDecimal result = orderService.getFullPriceForPayment("2");
        BigDecimal expected = BigDecimal.ZERO;

        assertEquals(expected, result);
    }

    @Test
    public void addInvalidOrder_returnsId(){
        Cart mockCart = Mockito.mock(Cart.class);
        Mockito.when(mockCart.createJsonFromCart()).thenReturn(new JSONObject());
        int orderId = orderService.addNewInvalidOrder("message", mockCart);

        Mockito.verify(orderDao).add(any(InvalidOrder.class));
        assertAll(()->Integer.valueOf(orderId));
    }



}
