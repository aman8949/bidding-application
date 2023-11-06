package com.intuit.craft;

import com.intuit.craft.enums.Category;
import com.intuit.craft.enums.Role;
import com.intuit.craft.excpetion.InvalidInputException;
import com.intuit.craft.excpetion.OperationNotAllowedException;
import com.intuit.craft.model.Product;
import com.intuit.craft.model.User;
import com.intuit.craft.request.ProductRequestDto;
import com.intuit.craft.request.UserRequestDto;
import com.intuit.craft.service.ProductService;
import com.intuit.craft.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BiddingSystemApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Test
    public void bidderNotAllowedToCreateProducts(){
        UserRequestDto bidderRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("bidder@gmail.com").role(Role.BIDDER.toString()).build();
        User bidder = userService.addUser(bidderRequestDto);
        ProductRequestDto productRequestDto = ProductRequestDto.builder().title("product1").description("product1desc").category(Category.MOBILE_PHONES.toString()).basePrice(50000.0).userId(bidder.getId()).build();
        assertThrows(OperationNotAllowedException.class, ()->productService.addProduct(productRequestDto), "Bidder is not allowed to create the products");
    }

    @Test
    public void saveAndRetrieveProduct_thenOK() {
        UserRequestDto merchantRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("test@gmail.com").role(Role.MERCHANT.toString()).build();
        User merchant = userService.addUser(merchantRequestDto);
        ProductRequestDto productRequestDto = ProductRequestDto.builder().title("product1").description("product1desc").category(Category.MOBILE_PHONES.toString()).basePrice(50000.0).userId(merchant.getId()).build();
        Product savedProduct = productService.addProduct(productRequestDto);
        Product fetchedProduct =  productService
                .getProduct(savedProduct.getId());
        System.out.println(savedProduct);
        System.out.println(fetchedProduct);

        assertNotNull(fetchedProduct);
        assertEquals(savedProduct, fetchedProduct);
    }

    @Test
    public void saveAndRetrieveProductByMerchant_thenOK() {
        UserRequestDto merchantRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("test@gmail.com").role(Role.MERCHANT.toString()).build();
        User merchant = userService.addUser(merchantRequestDto);
        ProductRequestDto productRequestDto = ProductRequestDto.builder().title("product1").description("product1desc").category(Category.MOBILE_PHONES.toString()).basePrice(50000.0).userId(merchant.getId()).build();
        productService.addProduct(productRequestDto);
        List<Product> fetchedProduct =  productService
                .getAllProductsByMerchant(merchant.getId());

        assertNotNull(fetchedProduct);
        assertEquals(fetchedProduct.size(), 1);
    }

    @Test
    public void saveAndRetrieveProductByCategory_thenOK() {
        UserRequestDto merchantRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("test@gmail.com").role(Role.MERCHANT.toString()).build();
        User merchant = userService.addUser(merchantRequestDto);
        ProductRequestDto productRequestDto = ProductRequestDto.builder().title("product1").description("product1desc").category(Category.MOBILE_PHONES.toString()).basePrice(50000.0).userId(merchant.getId()).build();
        productService.addProduct(productRequestDto);
        List<Product> fetchedProduct =  productService
                .getAllProductsByCategory("MOBILE_PHONES");

        assertNotNull(fetchedProduct);
    }

    @Test
    public void getProductByInvalidCategory_thenOK() {
        assertThrows(InvalidInputException.class, ()->productService
                .getAllProductsByCategory("Invalid"), "Invalid Category Provided");
    }

    @Test
    public void saveProductByMerchantWithInvalidCategory_thenOK() {
        UserRequestDto merchantRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("test@gmail.com").role(Role.MERCHANT.toString()).build();
        User merchant = userService.addUser(merchantRequestDto);
        ProductRequestDto productRequestDto = ProductRequestDto.builder().title("product1").description("product1desc").category("invalid").basePrice(50000.0).userId(merchant.getId()).build();
        assertThrows(InvalidInputException.class, ()->productService
                .addProduct(productRequestDto), "Invalid Category Provided");
    }
}
