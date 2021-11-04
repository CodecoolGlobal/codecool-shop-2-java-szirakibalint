package com.codecool.shop.service;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ProductServiceTest {

    ProductCategoryDao productCategoryDao ;
    ProductDao productDao ;
    SupplierDao supplierDao ;
    ProductService productService;


    @BeforeEach
    public void setup(){
        productCategoryDao = Mockito.mock(ProductCategoryDao.class);
        productDao = Mockito.mock(ProductDao.class);
        supplierDao = Mockito.mock(SupplierDao.class);
        productService = new ProductService(productDao, productCategoryDao, supplierDao);
    }

    @Test
    public void getProductCategory_withValidInput_returnsCategory(){
        int categoryId = 1;
        productService.getProductCategory(categoryId);
        Mockito.verify(productCategoryDao, times(1)).find(categoryId);
    }

    @Test
    public void getProductSupplier_withValidInput_returnsSupplier(){
        int supplierId = 1;
        productService.getProductSupplier(supplierId);
        Mockito.verify(supplierDao, times(1)).find(supplierId);
    }

    @Test
    public void getProductsForSupplier_withValidInput_returnsProducts(){
        int supplierId = 1;
        Supplier mockSupplier = Mockito.mock(Supplier.class);
        Mockito.when(supplierDao.find(supplierId)).thenReturn(mockSupplier);
        productService.getProductsForSupplier(supplierId);

        Mockito.verify(productDao, times(1)).getBy(mockSupplier);
    }

    @Test
    public void getProductsForCategory_withValidInput_returnsProducts(){
        int categoryId = 1;
        ProductCategory mockCategory = Mockito.mock(ProductCategory.class);
        Mockito.when(productCategoryDao.find(categoryId)).thenReturn(mockCategory);
        productService.getProductsForCategory(categoryId);

        Mockito.verify(productDao, times(1)).getBy(mockCategory);
    }
}