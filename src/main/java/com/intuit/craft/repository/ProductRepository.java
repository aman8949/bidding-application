package com.intuit.craft.repository;

import com.intuit.craft.enums.Category;
import com.intuit.craft.model.Product;
import com.intuit.craft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUser(User user);

    List<Product> findByCategory(Category category);
}
