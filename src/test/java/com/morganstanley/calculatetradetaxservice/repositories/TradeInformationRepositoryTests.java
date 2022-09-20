package com.morganstanley.calculatetradetaxservice.repositories;

import com.morganstanley.calculatetradetaxservice.CalculateTradeTaxServiceApplication;
import com.morganstanley.calculatetradetaxservice.dao.TradeInformationDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CalculateTradeTaxServiceApplication.class})
public class TradeInformationRepositoryTests {

    public static final String BUSINESS_NAME_1 = "BusinessName_1";

    @Autowired
    private TradeInformationRepository tradeInformationRepository;

    List<TradeInformationDto>  tradeInformationDtoList = new ArrayList<>();

    @Before
    public void setUp() {
        tradeInformationDtoList.add(TradeInformationDto.builder().businessName(BUSINESS_NAME_1)
                .totalNoOfSharesPurchased(2L).sharePriceEach(20.0).taxRate(2.5).localDateTime(LocalDateTime.now()).build());

        tradeInformationDtoList.add(TradeInformationDto.builder().businessName("BusinessName_2")
                .totalNoOfSharesPurchased(4L).sharePriceEach(15.0).taxRate(2.5).localDateTime(LocalDateTime.now()).build());
    }

    @Test
    public void testSaveAllTradeDetails() {
        tradeInformationRepository.saveAll(tradeInformationDtoList);
        Optional<TradeInformationDto> resultDto = tradeInformationRepository.findById(1L);
        assertEquals(BUSINESS_NAME_1, resultDto.get().getBusinessName());
    }
}
