package com.intuit.craft.controller;

import com.intuit.craft.model.Product;
import com.intuit.craft.request.ProductRequestDto;
import com.intuit.craft.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("product")
public class ProductController {

    private final ProductService productService;
    ProductController(ProductService productService)
    {
        this.productService = productService;
    }

    @Operation(summary = "Get Product by ProductId", description = "ProductId should be valid")
    @GetMapping("/{id}")
    public ResponseEntity<Product> productDetails(@PathVariable("id") Long productId)
    {
        // fetch details from user table and return
        return new ResponseEntity<>(productService.getProduct(productId), HttpStatus.OK);
    }

    @Operation(summary = "Get all products for a merchant")
    @GetMapping("/merchant")
    public ResponseEntity<List<Product>> getAllProduct(@RequestParam(name = "id") Long merchantId)
    {
        return new ResponseEntity<>(productService.getAllProductsByMerchant(merchantId), HttpStatus.OK);
    }

    @Operation(summary = "Create new product")
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDto productRequestDto)
    {
        return new ResponseEntity<>(productService.addProduct(productRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "get all products for a category")
    @GetMapping("/category")
    public ResponseEntity<List<Product>> getAllProductForACategory(@RequestParam(name = "category") String category)
    {
        return new ResponseEntity<>(productService.getAllProductsByCategory(category), HttpStatus.OK);
    }

    @Operation(summary = "Delete a product by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long productId){
        // remove post from table with given id
        productService.deleteProduct(productId);
        return new ResponseEntity<String>("Deleted", HttpStatus.OK);
    }
}
