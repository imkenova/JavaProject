package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GoodsRepository extends JpaRepository <Goods, Long> {
    @Query("SELECT p FROM Goods p WHERE CONCAT(p.name, ' ', p.content, ' ', p.dep_city, ' ', p.dep_date, ' ', p.arr_city, ' ', p.arr_date) LIKE %?1%")
    List<Goods> search(String keyword);
}
