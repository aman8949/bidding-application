package com.intuit.craft.service;

import com.intuit.craft.excpetion.*;
import com.intuit.craft.model.Product;
import com.intuit.craft.request.ProductRequestDto;

import java.util.List;

public interface ProductService {
    Product getProduct(Long productId) throws ProductNotFoundException;
    List<Product> getAllProductsByMerchant(Long userId) throws UserNotFoundException, ProductNotFoundException, OperationNotAllowedException;
    Product addProduct(ProductRequestDto product) throws InvalidInputException, EntityNotCreatedException, OperationNotAllowedException;

    List<Product> getAllProductsByCategory(String category) throws InvalidInputException, ProductNotFoundException;
}
