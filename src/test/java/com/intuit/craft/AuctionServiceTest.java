package com.intuit.craft;

import com.intuit.craft.enums.Category;
import com.intuit.craft.enums.Role;
import com.intuit.craft.excpetion.AuctionNotFoundException;
import com.intuit.craft.excpetion.InvalidInputException;
import com.intuit.craft.excpetion.OperationNotAllowedException;
import com.intuit.craft.model.Auction;
import com.intuit.craft.model.Product;
import com.intuit.craft.model.User;
import com.intuit.craft.request.AuctionRequestDto;
import com.intuit.craft.request.ProductRequestDto;
import com.intuit.craft.request.UserRequestDto;
import com.intuit.craft.service.AuctionService;
import com.intuit.craft.service.ProductService;
import com.intuit.craft.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BiddingSystemApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AuctionServiceTest {
    @Autowired
    private AuctionService auctionService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    final String testStartTime = formatter.format(LocalDateTime.now().plusHours(1));
    final String testEndTime = formatter.format(LocalDateTime.now().plusHours(6));

    @Test
    public void unauthorizedMerchantOrBidderNotAllowedToCreateAuctions(){
        UserRequestDto merchantRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("test@gmail.com").role(Role.MERCHANT.toString()).build();
        User merchant = userService.addUser(merchantRequestDto);
        ProductRequestDto productRequestDto = ProductRequestDto.builder().title("product1").description("product1desc").category(Category.MOBILE_PHONES.toString()).basePrice(50000.0).userId(merchant.getId()).build();
        Product savedProduct = productService.addProduct(productRequestDto);
        AuctionRequestDto auctionRequestDto = AuctionRequestDto.builder().endTime("").startTime("").productId(savedProduct.getId()).userId(merchant.getId()+1).build();
        assertThrows(OperationNotAllowedException.class, ()-> auctionService.createAuction(auctionRequestDto), "User is not allowed to create auctions");
    }

    @Test
    public void deleteAuction_Test(){
        UserRequestDto merchantRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("test3@gmail.com").role(Role.MERCHANT.toString()).build();
        User merchant = userService.addUser(merchantRequestDto);
        ProductRequestDto productRequestDto = ProductRequestDto.builder().title("product1").description("product1desc").category(Category.MOBILE_PHONES.toString()).basePrice(50000.0).userId(merchant.getId()).build();
        Product savedProduct = productService.addProduct(productRequestDto);
        AuctionRequestDto auctionRequestDto = AuctionRequestDto.builder().endTime(testEndTime).startTime(testStartTime).productId(savedProduct.getId()).userId(merchant.getId()).build();
        Auction savedAuction = auctionService.createAuction(auctionRequestDto);
        auctionService.deleteAuction(savedAuction.getId());
        assertThrows(AuctionNotFoundException.class, ()-> auctionService.getAuction(savedAuction.getId()), "Auction successfully deleted");
    }

    @Test
    void invalidDateFormat_Test(){
        UserRequestDto merchantRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("test2@gmail.com").role(Role.MERCHANT.toString()).build();
        User merchant = userService.addUser(merchantRequestDto);
        ProductRequestDto productRequestDto = ProductRequestDto.builder().title("product1").description("product1desc").category(Category.MOBILE_PHONES.toString()).basePrice(50000.0).userId(merchant.getId()).build();
        Product savedProduct = productService.addProduct(productRequestDto);
        AuctionRequestDto auctionRequestDto = AuctionRequestDto.builder().endTime("2024-10-10").startTime("2024-10-09 00").productId(savedProduct.getId()).userId(merchant.getId()).build();
        assertThrows(InvalidInputException.class, ()->auctionService.createAuction(auctionRequestDto), "Invalid Datetime format provided");
    }

    @Test
    public void saveAndGetAuctionsByCategoryTest_thenOK() {
        UserRequestDto merchantRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("test2@gmail.com").role(Role.MERCHANT.toString()).build();
        User merchant = userService.addUser(merchantRequestDto);
        ProductRequestDto productRequestDto = ProductRequestDto.builder().title("product1").description("product1desc").category(Category.MOBILE_PHONES.toString()).basePrice(50000.0).userId(merchant.getId()).build();
        Product savedProduct = productService.addProduct(productRequestDto);
        AuctionRequestDto auctionRequestDto = AuctionRequestDto.builder().endTime(testEndTime).startTime(testStartTime).productId(savedProduct.getId()).userId(merchant.getId()).build();
        auctionService.createAuction(auctionRequestDto);
        List<Auction> fetchedAuction = auctionService.getAllAuctionsByCategory("MOBILE_PHONES", 0, 10, "startTime", "asc");
        assertNotNull(fetchedAuction);
        assertEquals(fetchedAuction.get(0).getProduct().getCategory(), Category.MOBILE_PHONES);
    }

    @Test
    public void deleteProductAndCascadedAuctionsTest(){
        UserRequestDto merchantRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("test2@gmail.com").role(Role.MERCHANT.toString()).build();
        User merchant = userService.addUser(merchantRequestDto);
        ProductRequestDto productRequestDto = ProductRequestDto.builder().title("product1").description("product1desc").category(Category.MOBILE_PHONES.toString()).basePrice(50000.0).userId(merchant.getId()).build();
        Product savedProduct = productService.addProduct(productRequestDto);
        AuctionRequestDto auctionRequestDto = AuctionRequestDto.builder().endTime(testEndTime).startTime(testStartTime).productId(savedProduct.getId()).userId(merchant.getId()).build();
        Auction savedAuction = auctionService.createAuction(auctionRequestDto);
        productService.deleteProduct(savedProduct.getId());
        assertThrows(AuctionNotFoundException.class, ()-> auctionService.deleteAuction(savedAuction.getId()));
    }
}
