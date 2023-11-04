package com.intuit.craft.service;

import com.intuit.craft.enums.Category;
import com.intuit.craft.enums.Role;
import com.intuit.craft.excpetion.*;
import com.intuit.craft.model.Product;
import com.intuit.craft.model.User;
import com.intuit.craft.repository.ProductRepository;
import com.intuit.craft.request.ProductRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    private final UserService userService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserService userService) {this.productRepository = productRepository;this.userService = userService;}

    @Override
    public Product getProduct(final Long productId) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(productId);
        if(product.isEmpty())
            throw new ProductNotFoundException("Product with requested id does not exists");
        return product.get();
    }

    @Override
    public List<Product> getAllProductsByMerchant(final Long userId) throws UserNotFoundException, ProductNotFoundException, OperationNotAllowedException {
        User user = userService.getUser(userId);

        if(!user.getRole().equals(Role.MERCHANT))
            throw new OperationNotAllowedException("Operation Forbidden for the roletype");

        List<Product> productList = productRepository.findByUser(user);
        if(productList.isEmpty())
            throw new ProductNotFoundException("No Products found for the merchant");
        return productList;
    }

    @Override
    public Product addProduct(final ProductRequestDto productObj) throws InvalidInputException, EntityNotCreatedException, OperationNotAllowedException {
        log.info(productObj.toString());
        User user = userService.getUser(productObj.getUserId());
        if(!user.getRole().equals(Role.MERCHANT))
            throw new OperationNotAllowedException("Operation Forbidden for the roletype");
        try {
            Product product = Product.builder().title(productObj.getTitle()).basePrice(productObj.getBasePrice()).description(productObj.getDescription()).category(Category.valueOf(productObj.getCategory())).user(user).build();
            return productRepository.saveAndFlush(product);
        }
        catch(IllegalArgumentException e){
            log.error("Invalid Category Provided");
            throw new InvalidInputException("Invalid Category Provided");
        }
        catch (DataAccessException e){
            log.error("Error while saving into database :{}", e.getMessage());
            throw new EntityNotCreatedException("Error while creating the product");
        }
    }

    @Override
    public List<Product> getAllProductsByCategory(final String category) throws ProductNotFoundException {
        try{
            List<Product> productList = productRepository.findByCategory(Category.valueOf(category));
            if(productList.isEmpty())
                throw new ProductNotFoundException("No Product Found for specific category");
            return productList;
        }
        catch (IllegalArgumentException e){
            log.error("Invalid Category Provided");
            throw new ProductNotFoundException("Invalid Category Provided");
        }
    }
}
