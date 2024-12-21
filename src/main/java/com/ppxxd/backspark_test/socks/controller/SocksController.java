package com.ppxxd.backspark_test.socks.controller;

import com.ppxxd.backspark_test.socks.dto.SocksDto;
import com.ppxxd.backspark_test.socks.dto.UpdateSocksDto;
import com.ppxxd.backspark_test.socks.enums.FilterOperations;
import com.ppxxd.backspark_test.socks.enums.Sort;
import com.ppxxd.backspark_test.socks.service.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/socks")
@RequiredArgsConstructor
public class SocksController {
    private final SocksService socksService;

    @Operation(summary = "Регистрация прихода носков",
            description = "Регистрирует поступление носков по цвету, проценту хлопка и количеству.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Носки успешно зарегистрированы"),
            @ApiResponse(responseCode = "400", description = "Неверные данные ввода")
    })
    @PostMapping("/income")
    public ResponseEntity<SocksDto> registerIncome(@RequestParam String color,
                                                         @RequestParam int cottonPart,
                                                         @RequestParam int quantity) {
        log.info("Получен запрос POST /api/socks/income с параметрами: color={}, cottonPart={}, quantity={}",
                color, cottonPart, quantity);
        return new ResponseEntity<>(socksService.registerIncome(color, cottonPart, quantity), HttpStatus.CREATED);
    }

    @Operation(summary = "Регистрация отпуска носков",
            description = "Регистрирует отпуска носков по цвету, проценту хлопка и количеству.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Носки успешно зарегистрированы"),
            @ApiResponse(responseCode = "400", description = "Неверные данные ввода")
    })
    @PostMapping("/outcome")
    public ResponseEntity<SocksDto> registerOutcome(@RequestParam String color,
                                                  @RequestParam int cottonPart,
                                                  @RequestParam int quantity) {
        log.info("Получен запрос POST /api/socks/outcome с параметрами: color={}, cottonPart={}, quantity={}",
                color, cottonPart, quantity);
        return new ResponseEntity<>(socksService.registerOutcome(color, cottonPart, quantity), HttpStatus.CREATED);
    }

    @Operation(summary = "Получение общего количества носков с фильтрацией",
            description = "Получает общее количество носков с возможностью фильтрации по цвету," +
                    " оператор сравнения (moreThan, lessThan, equal)," +
                    " диапазону процентного содержания хлопка (например, от 30 до 70%).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Количество успешно получено"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса")
    })
    @GetMapping
    public ResponseEntity<Integer> getSocksQuantity(@RequestParam(required = false) String color,
                                                    @RequestParam(required = false) FilterOperations operation,
                                                    @RequestParam(required = false, defaultValue = "0") int minCottonPart,
                                                    @RequestParam(required = false, defaultValue = "100") int maxCottonPart,
                                                    @RequestParam(required = false) int quantity) {
        log.info("Получен запрос GET /api/socks/ с параметрами: color={}, operation={}, minCottonPart={}," +
                        " maxCottonPart={} quantity={}", color, operation, minCottonPart, maxCottonPart, quantity);
        return new ResponseEntity<>(socksService.getSocksQuantity(color, operation, minCottonPart, maxCottonPart, quantity), HttpStatus.OK);
    }


    /*
    Добавил данный эндпоинт ещё, т. к. в тз было необходимо добавить:
        "Возможность сортировки результата по цвету или проценту хлопка."
    Но при получении количества, сортировать то нечего.
    */
    @Operation(summary = "Получить все носки",
            description = "Получает все носки с возможностью фильтрации по цвету," +
                    " оператор сравнения (moreThan, lessThan, equal)," +
                    " диапазону процентного содержания хлопка (например, от 30 до 70%), сортировкой.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Носки успешно получены"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры запроса")
    })
    @GetMapping("/all")
    public ResponseEntity<List<SocksDto>> getSocks(@RequestParam(required = false) String color,
                                                   @RequestParam(required = false) FilterOperations operation,
                                                   @RequestParam(required = false, defaultValue = "0") int minCottonPart,
                                                   @RequestParam(required = false, defaultValue = "100") int maxCottonPart,
                                                   @RequestParam(required = false) int quantity,
                                                   @RequestParam(required = false) Sort sort) {
        log.info("Получен запрос GET /api/socks/all с параметрами: color={}, operation={}, minCottonPart={}," +
                " maxCottonPart={} quantity={}, sort ={}", color, operation, minCottonPart, maxCottonPart, quantity, sort);
        return new ResponseEntity<>(socksService.getSocks(color, operation, minCottonPart,
                maxCottonPart, quantity, sort), HttpStatus.OK);
    }

    @Operation(summary = "Обновить информацию о носках",
            description = "Обновляет информацию о носках по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Носок успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Носок не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SocksDto> updateSock(@PathVariable Long id,
                                             @RequestBody UpdateSocksDto updatedSocks) {
        log.info("Получен запрос PUT /api/socks/id с параметрами: id={}, updatedSocks={}",
                id, updatedSocks);
        return new ResponseEntity<>(socksService.updateSock(id, updatedSocks), HttpStatus.OK);
    }

    @Operation(summary = "Загрузить партию носков",
            description = "Загружает партию носков из файла формата CSV.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Партия успешно обработана"),
            @ApiResponse(responseCode = "400", description = "Неверный формат файла")
    })
    @PostMapping("/batch")
    public ResponseEntity<List<SocksDto>> uploadBatch(@RequestParam("file") MultipartFile file) {
        log.info("Получен запрос POST /api/socks/batch с параметрами: file.name={}", file.getName());
        return new ResponseEntity<>(socksService.processBatch(file), HttpStatus.CREATED);
    }
}
