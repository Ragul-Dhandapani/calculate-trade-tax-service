package com.morganstanley.calculatetradetaxservice.service;

import com.morganstanley.calculatetradetaxservice.dao.TradeInformationDto;
import com.morganstanley.calculatetradetaxservice.entities.AddedTradeDetailsResponse;
import com.morganstanley.calculatetradetaxservice.entities.CalculateTradeTaxResponse;
import com.morganstanley.calculatetradetaxservice.repositories.TradeInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.morganstanley.calculatetradetaxservice.constants.ApplicationConstants.*;

@Service
public class CalculateTradeTaxService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateTradeTaxService.class);

    @Autowired
    private TradeInformationRepository tradeInformationRepository;

    /**
     *  Store the Trade information into Database
     *
     * @param tradeInformationDto
     * @return
     * @throws JpaSystemException
     */
    public AddedTradeDetailsResponse addTradeInformation(TradeInformationDto tradeInformationDto) throws JpaSystemException {

        tradeInformationRepository.save(tradeInformationDto);
        return AddedTradeDetailsResponse.builder().trade_id(tradeInformationDto.getTradeId()).status(SUCCESS)
                .message(SUCCESS_MSG).timeStamp(LocalDateTime.now()).build();
    }

    /**
     * If the Trade Tax is already calculated for the tradeId then returning from cache ,
     *  If not then it will get calculated
     * @param tradeId
     * @return
     */
    @Cacheable(cacheNames = "calculate_trade_tax_service_cache", key = "#tradeId")
    public CalculateTradeTaxResponse calculateTradeTaxDue(Long tradeId) {
        LOGGER.info("Trade Id to calculate the tax due {}" , tradeId);
        CalculateTradeTaxResponse calculateTradeTaxResponse = null;

        Optional<TradeInformationDto> tradeInformationDto = tradeInformationRepository.findById(tradeId);

        if (tradeInformationDto.isPresent()) {
            Double shareAmount = tradeInformationDto.get().getTotalNoOfSharesPurchased() * tradeInformationDto.get().getSharePriceEach();
            Double taxDueAmount = shareAmount * tradeInformationDto.get().getTaxRate();

            LOGGER.info("Calculated Trade Tax Due for the Trade id {} is {}" , tradeId , taxDueAmount);

            calculateTradeTaxResponse = CalculateTradeTaxResponse.builder().status(SUCCESS)
                                                                .message(TRADE_CALCULATION_SUCCESS_MSG)
                                                                .timeStamp(LocalDateTime.now()).TaxDueToBePaid(taxDueAmount).build();
        } else {
            LOGGER.info("Given Trade Id is not available in the database");
            calculateTradeTaxResponse = CalculateTradeTaxResponse.builder().status(FAILED)
                                                                 .message(TRADE_ID_NOT_FOUND_MSG)
                                                                 .timeStamp(LocalDateTime.now()).TaxDueToBePaid(null).build();
        }
        return calculateTradeTaxResponse;
    }

}
