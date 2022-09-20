package com.morganstanley.calculatetradetaxservice.controller;

import com.morganstanley.calculatetradetaxservice.dao.TradeInformationDto;
import com.morganstanley.calculatetradetaxservice.entities.AddedTradeDetailsResponse;
import com.morganstanley.calculatetradetaxservice.entities.CalculateTradeTaxResponse;
import com.morganstanley.calculatetradetaxservice.service.CalculateTradeTaxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.morganstanley.calculatetradetaxservice.constants.ApplicationConstants.*;

@RestController
@Validated
@Api(value = "Calculate Trade Tax Due MicroService")
public class CalculateTradeTaxController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateTradeTaxController.class);

    @Autowired
    private CalculateTradeTaxService tradeTaxService;

    @ApiOperation(value = "Create or onboard the business trade information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = SUCCESS_MSG) ,
            @ApiResponse(code = 400, message = DATA_EXCEPTION)
    })
    @PostMapping(path = "createTradeDetails", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddedTradeDetailsResponse> createTradeDetails(@Valid @RequestBody TradeInformationDto tradeInformationDto) {

        AddedTradeDetailsResponse tradeDetailsResponse = null;
        try {
            tradeDetailsResponse = tradeTaxService.addTradeInformation(tradeInformationDto);
            LOGGER.info("Trade information successfully Stored into Database");
        } catch (JpaSystemException jpaSystemException) {
            throw new JpaSystemException(jpaSystemException);
        }
        return new ResponseEntity<>(tradeDetailsResponse , HttpStatus.OK);
    }

    @ApiOperation(value = "Calculate Tax Due on the Trades")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = TRADE_CALCULATION_SUCCESS_MSG) ,
            @ApiResponse(code = 404, message = TRADE_ID_NOT_FOUND_MSG)
    })
    @GetMapping(path = "calculateTaxDue/{trade_id}")
    public ResponseEntity<CalculateTradeTaxResponse> calculateTaxDue(@PathVariable(name = "trade_id", required = true)
                                                                     final Long tradeId) {
        CalculateTradeTaxResponse tradeTaxResponse = tradeTaxService.calculateTradeTaxDue(tradeId);
        return new ResponseEntity<>(tradeTaxResponse ,
                tradeTaxResponse.getStatus().equals(SUCCESS) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }


}
