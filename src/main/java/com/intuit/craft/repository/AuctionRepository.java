package com.intuit.craft.repository;

import com.intuit.craft.enums.Category;
import com.intuit.craft.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByProductCategory(Category category);
}
