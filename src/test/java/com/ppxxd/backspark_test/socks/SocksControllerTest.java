package com.ppxxd.backspark_test.socks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppxxd.backspark_test.socks.dto.SocksDto;
import com.ppxxd.backspark_test.socks.dto.UpdateSocksDto;
import com.ppxxd.backspark_test.socks.enums.FilterOperations;
import com.ppxxd.backspark_test.socks.enums.Sort;
import com.ppxxd.backspark_test.socks.service.SocksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SocksControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SocksService socksService;

    private SocksDto socksDtoResult;
    private SocksDto socksDtoResult2;
    private SocksDto socksDtoResult3;

    @BeforeEach
    void BeforeEach() {
        socksDtoResult = SocksDto.builder()
                            .id(1L)
                            .color("Red")
                            .cottonPart(80)
                            .quantity(10)
                            .build();

        socksDtoResult2 = SocksDto.builder()
                .id(1L)
                .color("Red")
                .cottonPart(80)
                .quantity(5)
                .build();

        socksDtoResult3 = SocksDto.builder()
                .id(1L)
                .color("Green")
                .cottonPart(70)
                .quantity(20)
                .build();
    }

    @Test
    public void registerIncomeTest() throws Exception {
        when(socksService.registerIncome("Red", 80, 10)).thenReturn(socksDtoResult);

        checkSocksProps(mockMvc.perform(post("/api/socks/income")
                .param("color", "Red")
                .param("cottonPart", "80")
                .param("quantity", "10")
        ));
    }

    @Test
    public void registerOutcomeTest() throws Exception {
        when(socksService.registerOutcome("Red", 80, 5)).thenReturn(socksDtoResult2);


        checkSocksProps(mockMvc.perform(post("/api/socks/outcome")
                .param("color", "Red")
                .param("cottonPart", "80")
                .param("quantity", "5")
        ));
    }

    @Test
    public void getSocksQuantityTest() throws Exception {
        when(socksService.getSocksQuantity("Red", FilterOperations.MORE_THAN,
                0, 100, 3)).thenReturn(5);

        mockMvc.perform(get("/api/socks")
                .param("color", "Red")
                .param("operation", "MORE_THAN")
                .param("minCottonPart", "0")
                .param("maxCottonPart", "100")
                .param("quantity", "3")
        ).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void getSocksTest() throws Exception {
        when(socksService.getSocks(null, FilterOperations.MORE_THAN, 0,
                100, 3, Sort.COTTON_PART))
                .thenReturn(List.of(socksDtoResult2, socksDtoResult3));

        checkSocksListProps(mockMvc.perform(get("/api/socks/all")
                .param("operation", "MORE_THAN")
                .param("minCottonPart", "0")
                .param("maxCottonPart", "100")
                .param("quantity", "3")
                .param("sort", "COTTON_PART")
        ));
    }

    @Test
    public void updateSocksTest() throws Exception {
        UpdateSocksDto updateSocksDto = new UpdateSocksDto("Blue", 80, 2);

        SocksDto expectedResponse = SocksDto.builder()
                .id(1L)
                .color("Blue")
                .cottonPart(80)
                .quantity(2)
                .build();

        String requestJson = new ObjectMapper().writeValueAsString(updateSocksDto);

        when(socksService.updateSock(1L, updateSocksDto))
                .thenReturn(expectedResponse);


        mockMvc.perform(put("/api/socks/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.color").isNotEmpty())
                .andExpect(jsonPath("$.cottonPart").isNumber())
                .andExpect(jsonPath("$.quantity").isNumber())
                .andDo(print())
                .andReturn();;
    }

    @Test
    public void registerIncomeBatch() throws Exception {
        String csvContent = "color,cottonPart,quantity\nRed,80,10\nBlue,70,5";
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "socks.csv",
                "text/csv",
                csvContent.getBytes()
        );

        mockMvc.perform(multipart("/api/socks/batch")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    private static void checkSocksProps(ResultActions result) throws Exception {
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.color").isNotEmpty())
                .andExpect(jsonPath("$.cottonPart").isNumber())
                .andExpect(jsonPath("$.quantity").isNumber())
                .andDo(print())
                .andReturn();
    }

    private static void checkSocksListProps(ResultActions result) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].color").isNotEmpty())
                .andExpect(jsonPath("$[0].cottonPart").isNumber())
                .andExpect(jsonPath("$[0].quantity").isNumber())
                .andDo(print())
                .andReturn();
    }
}
