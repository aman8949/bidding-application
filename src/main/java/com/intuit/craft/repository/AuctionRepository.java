package com.intuit.craft.repository;

import com.intuit.craft.enums.Category;
import com.intuit.craft.model.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Page<Auction> findByProductCategoryAndEndTimeAfter(Category category, LocalDateTime t, Pageable p);
}
