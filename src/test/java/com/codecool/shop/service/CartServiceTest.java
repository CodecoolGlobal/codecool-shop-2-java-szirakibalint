package com.codecool.shop.service;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CartServiceTest {

    CartService cartService;
    ProductDao productDao ;
    CartDao cartDao ;
    Product mockProduct;

    @BeforeEach
    public void setup(){
        mockProduct = Mockito.mock(Product.class);
        productDao = Mockito.mock(ProductDao.class);
        cartDao = Mockito.mock(CartDao.class);
        cartService = new CartService(cartDao, productDao);
        Mockito.when(productDao.find(1)).thenReturn(mockProduct);

    }

    @Test
    public void handlePost_withValidProductIdAndNullCartId_addsToDefaultCart(){
        cartService.handlePost("1",null);
        Mockito.verify(cartDao).addToCart(0, mockProduct);
    }

    @Test
    public void handlePost_withValidArguments_addsToCorrectCart(){
        cartService.handlePost("1","1");
        Mockito.verify(cartDao).addToCart(1, mockProduct);
    }

    @Test
    public void handlePost_withInvalidCartId_throwsException(){
        assertThrows(NumberFormatException.class, ()->cartService.handlePost("1","a1"));
    }

    @Test
    public void handlePost_withInvalidProductId_throwsException(){
        assertThrows(NumberFormatException.class, ()->cartService.handlePost("a","1"));
    }

    @Test
    public void handleGet_withNullArguments_returnsEmptyJSONObject(){
        assertEquals(cartService.handleGet(null, null).toString(), new JSONObject().toString());
    }

    @Test
    public void handleGet_withNoProductId_returnsEmptyCart(){
        assertEquals(cartService.handleGet("0", null).toString(), new JSONObject().toString());
    }


    @Test
    public void handleGet_withValidArguments_returnsValidJson(){
        Mockito.when(cartDao.getCartById(2)).thenReturn(new Cart());
        assertEquals("{\"quantity\":0,\"total_price\":0}", cartService.handleGet("2", "1").toString());

    }

}
