package com.intuit.craft;

import com.intuit.craft.enums.Category;
import com.intuit.craft.enums.Role;
import com.intuit.craft.excpetion.OperationNotAllowedException;
import com.intuit.craft.model.Auction;
import com.intuit.craft.model.Product;
import com.intuit.craft.model.User;
import com.intuit.craft.request.AuctionRequestDto;
import com.intuit.craft.request.BidRequestDto;
import com.intuit.craft.request.ProductRequestDto;
import com.intuit.craft.request.UserRequestDto;
import com.intuit.craft.service.AuctionService;
import com.intuit.craft.service.BiddingServices.GameService;
import com.intuit.craft.service.ProductService;
import com.intuit.craft.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BiddingSystemApplication.class)
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AuctionService auctionService;

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void invalidAuctionBidTest(){
        UserRequestDto merchantRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("test2@gmail.com").role(Role.MERCHANT.toString()).build();
        User merchant = userService.addUser(merchantRequestDto);
        UserRequestDto bidderRequestDto = UserRequestDto.builder().firstName("testfn").lastName("testln").emailId("test@gmail.com").role(Role.BIDDER.toString()).build();
        User bidder = userService.addUser(bidderRequestDto);
        ProductRequestDto productRequestDto = ProductRequestDto.builder().title("product1").description("product1desc").category(Category.MOBILE_PHONES.toString()).basePrice(50000.0).userId(merchant.getId()).build();
        Product savedProduct = productService.addProduct(productRequestDto);
        AuctionRequestDto auctionRequestDto = AuctionRequestDto.builder().endTime(formatter.format(LocalDateTime.now().plusHours(12))).startTime(formatter.format(LocalDateTime.now().plusHours(1))).productId(savedProduct.getId()).userId(merchant.getId()).build();
        Auction savedAuction = auctionService.createAuction(auctionRequestDto);

        BidRequestDto bidRequestDto = new BidRequestDto();
        bidRequestDto.setBidValue(51000.0);
        bidRequestDto.setUserId(bidder.getId());
        bidRequestDto.setAuctionId(savedAuction.getId());
        assertThrows(OperationNotAllowedException.class, ()-> gameService.processBidRequest(bidRequestDto), "Auction not active");
    }
}
