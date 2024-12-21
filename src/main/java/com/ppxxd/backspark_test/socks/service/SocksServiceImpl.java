package com.ppxxd.backspark_test.socks.service;

import com.ppxxd.backspark_test.exception.ObjectNotFoundException;
import com.ppxxd.backspark_test.exception.SocksShortageException;
import com.ppxxd.backspark_test.socks.dto.NewSocksDto;
import com.ppxxd.backspark_test.socks.dto.SocksDto;
import com.ppxxd.backspark_test.socks.dto.UpdateSocksDto;
import com.ppxxd.backspark_test.socks.enums.FilterOperations;
import com.ppxxd.backspark_test.socks.enums.Sort;
import com.ppxxd.backspark_test.socks.mapper.SocksMapper;
import com.ppxxd.backspark_test.socks.model.Socks;
import com.ppxxd.backspark_test.socks.repository.SocksRepository;
import com.ppxxd.backspark_test.util.CSVBatchReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SocksServiceImpl implements SocksService {
    private final SocksRepository socksRepository;

    @Override
    public SocksDto registerIncome(String color, int cottonPart, int quantity) {
        Socks socks = socksRepository.findByColorAndCottonPart(color, cottonPart);
        if (socks == null) {
            socks = socksRepository.save(SocksMapper.fromNewDto(new NewSocksDto(color, cottonPart, quantity)));
        } else {
            socks.setQuantity(socks.getQuantity() + quantity);
            socksRepository.save(socks);
        }
        log.info("Поступление носков успешно зарегистрировано: {}", socks);
        return SocksMapper.toDto(socks);
    }

    @Override
    public SocksDto registerOutcome(String color, int cottonPart, int quantity) {
        Socks socks = socksRepository.findByColorAndCottonPart(color, cottonPart);
        if (socks == null || socks.getQuantity() < quantity) {
            int socksQuantityAtRepo = socks == null ? 0 : socks.getQuantity();
            throw new SocksShortageException(String.format("Носков с параметрами %s, %d на складе всего %d шт., " +
                    "вместо необходимых %d шт.", color, cottonPart, socksQuantityAtRepo, quantity));
        }
        socks.setQuantity(socks.getQuantity() - quantity);
        socksRepository.save(socks);
        log.info("Отступление носков успешно зарегистрировано: {}", socks);
        return new SocksDto(socks.getId(), color, cottonPart, quantity);
    }

    @Override
    public Integer getSocksQuantity(String color, FilterOperations operation, int minCottonPart,
                                    int maxCottonPart, int quantity) {
        Integer result = socksRepository.findSocksQuantityByParams(color, String.valueOf(operation),
                minCottonPart, maxCottonPart, quantity);
        log.info("Получение общего количества носков успешно зарегистрировано: {}", result);
        return result;
    }

    @Override
    public List<SocksDto> getSocks(String color, FilterOperations operation, int minCottonPart, int maxCottonPart,
                                   int quantity, Sort sort) {
        List<Socks> socksList = socksRepository.findSocksByParams(color, operation.name(), minCottonPart, maxCottonPart, quantity);

        if (sort != null) {
            switch (sort) {
                case COLOR -> socksList.sort((e1, e2) -> e2.getColor().compareTo(e1.getColor()));
                case COTTON_PART -> socksList.sort((e1, e2) -> e2.getCottonPart().compareTo(e1.getCottonPart()));
            }
        }

        log.info("Получение списка носков успешно зарегистрировано, размер полученного списка: {}", socksList.size());
        return socksList.stream().map(SocksMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public SocksDto updateSock(Long id, UpdateSocksDto updatedSocksDto) {
        Socks socks = socksRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                String.format("Носок c id %d не найден", id)));
        socks.setColor(updatedSocksDto.getColor());
        socks.setCottonPart(updatedSocksDto.getCottonPart());
        socks.setQuantity(updatedSocksDto.getQuantity());
        socksRepository.save(socks);

        log.info("Обновление данных носков успешно зарегистрировано: {}", socks);
        return SocksMapper.toDto(socks);
    }

    @Override
    public List<SocksDto> processBatch(MultipartFile file) {
        List<NewSocksDto> socksDtoList = CSVBatchReader.processCsvFile(file);
        List<Socks> socksList = socksDtoList.stream().map(SocksMapper::fromNewDto).toList();
        socksRepository.saveAll(socksList);
        log.info("Поступление носков из файла успешно зарегистрировано. " +
                "Добавлено следующее кол-во носков: {}", socksList.size());
        return new ArrayList<>(socksList).stream().map(SocksMapper::toDto).toList();
    }
}
