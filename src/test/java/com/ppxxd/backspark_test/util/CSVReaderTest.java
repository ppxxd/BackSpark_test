package com.ppxxd.backspark_test.util;

import com.ppxxd.backspark_test.socks.dto.NewSocksDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CSVReaderTest {
    private MultipartFile file;

    @BeforeEach
    void setUp() throws IOException {
        String csvContent = "color,cotton_part,quantity\nRed,80,100\nBlue,70,150\nGreen,60,200";
        file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        when(file.getName()).thenReturn("test.csv");
    }

    @Test
    void testProcessCsvFile_success() {
        List<NewSocksDto> result = CSVBatchReader.processCsvFile(file);

        assertNotNull(result);
        assertEquals(3, result.size());

        NewSocksDto firstSocksDto = result.get(0);
        assertEquals("Red", firstSocksDto.getColor());
        assertEquals(80, firstSocksDto.getCottonPart());
        assertEquals(100, firstSocksDto.getQuantity());
    }
}
