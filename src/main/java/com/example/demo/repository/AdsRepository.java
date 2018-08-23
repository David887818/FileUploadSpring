package com.example.demo.repository;

import com.example.demo.model.Advertisment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdsRepository extends JpaRepository<Advertisment,Integer> {
}
