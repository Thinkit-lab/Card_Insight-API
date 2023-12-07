package com.mintyn.cardInsight.repository;

import com.mintyn.cardInsight.entity.CardStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardStatsRepository extends JpaRepository<CardStats, Long> {
    Optional<CardStats> findCardStatsByBinNumber(String binNumber);
}
