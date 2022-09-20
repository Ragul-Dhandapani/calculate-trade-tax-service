package com.morganstanley.calculatetradetaxservice.repositories;

import com.morganstanley.calculatetradetaxservice.dao.TradeInformationDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeInformationRepository extends JpaRepository<TradeInformationDto, Long> {
}
