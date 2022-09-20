package com.morganstanley.calculatetradetaxservice.controller;

import com.morganstanley.calculatetradetaxservice.CalculateTradeTaxServiceApplication;
import com.morganstanley.calculatetradetaxservice.entities.AddedTradeDetailsResponse;
import com.morganstanley.calculatetradetaxservice.entities.CalculateTradeTaxResponse;
import com.morganstanley.calculatetradetaxservice.service.CalculateTradeTaxService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static com.morganstanley.calculatetradetaxservice.constants.ApplicationConstants.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CalculateTradeTaxServiceApplication.class})
@WebMvcTest(controllers = CalculateTradeTaxController.class)
public class CalculateTradeTaxControllerTests {

    private static final String CREATE_TRADE_DETAILS = "/" + BASE_API_URL + "createTradeDetails";
    private static final String GET_TAX_DUE_API = "/" + BASE_API_URL + "calculateTaxDue/";
    private static final Long TRADE_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculateTradeTaxService calculateTradeTaxService;

    @Test
    public void testCreateTradeDetailsSuccessful() throws Exception {
        AddedTradeDetailsResponse addedRecords = prepareEntityList();
        when(calculateTradeTaxService.addTradeInformation(Mockito.any())).thenReturn(addedRecords);

        String requestJson = "{\"business_name\": \"morganstanley\", \"total_no_of_shares_purchased\": 1, \"share_price_each\": 122, \"tax_rate\": 1.5 }";

        mockMvc.perform(post(CREATE_TRADE_DETAILS).content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.trade_id").value(TRADE_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SUCCESS_MSG));
    }

    @Test
    public void testCreateTradeDetailsApiBadRequest() throws Exception {
        String requestJson = "{\"business_name\": \"morganstanley\", \"total_no_of_shares_purchased\": 0, \"share_price_each\": 20, \"tax_rate\": 1.5 }";

        mockMvc.perform(post(CREATE_TRADE_DETAILS).content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetCalculateTaxDueSuccessful() throws Exception {
        when(calculateTradeTaxService.calculateTradeTaxDue(Mockito.anyLong()))
                .thenReturn(prepareTaxDueSuccessResponse());

        mockMvc.perform(get(GET_TAX_DUE_API + TRADE_ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(TRADE_CALCULATION_SUCCESS_MSG))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taxDueToBePaid").value(183.0));
    }

    @Test
    public void testGetCalculateTaxDueTradeNotFoundFailed() throws Exception{
        when(calculateTradeTaxService.calculateTradeTaxDue(Mockito.anyLong()))
                .thenReturn(prepareTaxDueFailedResponse());

        mockMvc.perform(get(GET_TAX_DUE_API + TRADE_ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(FAILED))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(TRADE_ID_NOT_FOUND_MSG));
    }

    @Test
    public void testGetCalculateTaxDueTradeBadRequest() throws Exception{
        mockMvc.perform(get(GET_TAX_DUE_API + null).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    private AddedTradeDetailsResponse prepareEntityList() {
        return AddedTradeDetailsResponse.builder().trade_id(TRADE_ID).status(SUCCESS)
                .message(SUCCESS_MSG).timeStamp(LocalDateTime.now()).build();
    }

    private CalculateTradeTaxResponse prepareTaxDueSuccessResponse() {
        return CalculateTradeTaxResponse.builder().status(SUCCESS)
                .message(TRADE_CALCULATION_SUCCESS_MSG)
                .timeStamp(LocalDateTime.now()).TaxDueToBePaid(183.0).build();
    }

    private CalculateTradeTaxResponse prepareTaxDueFailedResponse() {
        return CalculateTradeTaxResponse.builder().status(FAILED)
                .message(TRADE_ID_NOT_FOUND_MSG)
                .timeStamp(LocalDateTime.now()).TaxDueToBePaid(null).build();
    }
}
