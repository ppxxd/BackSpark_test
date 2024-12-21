package com.ppxxd.backspark_test.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.ppxxd.backspark_test.exception.FileHandlerException;
import com.ppxxd.backspark_test.socks.dto.NewSocksDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVBatchReader {

    public static List<NewSocksDto> processCsvFile(MultipartFile file) {
        List<NewSocksDto> socksDtoList = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));
            List<String[]> rows = reader.readAll();

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                NewSocksDto socksDto = new NewSocksDto(row[0], Integer.parseInt(row[1]), Integer.parseInt(row[2]));
                socksDtoList.add(socksDto);
            }
            reader.close();
        } catch (IOException | CsvException e) {
            throw new FileHandlerException(String.format("Ошибка при чтении файла %s", file.getName()));
        }
        return socksDtoList;
    }
}
