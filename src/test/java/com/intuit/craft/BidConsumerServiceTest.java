package com.intuit.craft;

import com.intuit.craft.enums.Category;
import com.intuit.craft.enums.Role;
import com.intuit.craft.model.Auction;
import com.intuit.craft.model.Product;
import com.intuit.craft.model.User;
import com.intuit.craft.request.BidRequestDto;
import com.intuit.craft.service.AuctionService;
import com.intuit.craft.service.BiddingServices.BidConsumerService;
import com.intuit.craft.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BiddingSystemApplication.class)
public class BidConsumerServiceTest {
    @MockBean
    private AuctionService auctionService;

    @MockBean
    private UserService userService;

    @Autowired
    private BidConsumerService bidConsumerService;

    @Test
    public void playBidTest_thenOK() {
        User merchant = new User(1, "merchant", "merchant", "merchant@gmail.com", Role.MERCHANT);
        User bidder = new User(2, "bidder", "bidder", "bidder@gmail.com", Role.BIDDER);
        Product product = new Product(1, "iphone", 80000.0D, Category.MOBILE_PHONES, "iphone15", merchant);
        Auction auction = new Auction(1, false, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(12), 80000.0, product, merchant);
        when(auctionService.getAuction(any())).thenReturn(auction);
        when(userService.getUser(any())).thenReturn(bidder);

        BidRequestDto bidRequestDto = new BidRequestDto();
        bidRequestDto.setBidValue(90000.0);
        bidRequestDto.setUserId(2L);
        bidRequestDto.setAuctionId(1L);
        when(auctionService.updateAuction(any())).thenReturn(null);
        bidConsumerService.consumeMessage(bidRequestDto);
        verify(auctionService,times(1)).updateAuction(any());
    }
}
