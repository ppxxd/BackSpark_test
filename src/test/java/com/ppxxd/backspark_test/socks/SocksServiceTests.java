package com.ppxxd.backspark_test.socks;

import com.ppxxd.backspark_test.exception.ObjectNotFoundException;
import com.ppxxd.backspark_test.exception.SocksShortageException;
import com.ppxxd.backspark_test.socks.dto.SocksDto;
import com.ppxxd.backspark_test.socks.dto.UpdateSocksDto;
import com.ppxxd.backspark_test.socks.model.Socks;
import com.ppxxd.backspark_test.socks.repository.SocksRepository;
import com.ppxxd.backspark_test.socks.service.SocksServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SocksServiceTests {
    @Mock
    private SocksRepository socksRepository;
    @InjectMocks
    private SocksServiceImpl socksService;

    private Socks testSocks;

    @BeforeEach
    void setUp() {
        testSocks = Socks.builder()
                .id(1L)
                .color("Red")
                .cottonPart(80)
                .quantity(10)
                .build();
    }

    @Test
    void testRegisterIncome_newSocks() {
        when(socksRepository.save(any(Socks.class))).thenReturn(testSocks);

        SocksDto result = socksService.registerIncome("Red", 80, 10);

        assertNotNull(result);
        assertEquals(testSocks.getColor(), result.getColor());
        assertEquals(testSocks.getCottonPart(), result.getCottonPart());
        assertEquals(testSocks.getQuantity(), result.getQuantity());

        verify(socksRepository, times(1)).save(any(Socks.class));
    }

    @Test
    void testRegisterIncome_existingSocks() {
        when(socksRepository.findByColorAndCottonPart("Red", 80)).thenReturn(testSocks);
        when(socksRepository.save(any(Socks.class))).thenReturn(testSocks);

        SocksDto result = socksService.registerIncome("Red", 80, 40);

        assertNotNull(result);
        assertEquals(50, result.getQuantity());

        verify(socksRepository, times(1)).save(any(Socks.class));
    }

    @Test
    void testRegisterOutcome_socksShortage() {
        testSocks.setQuantity(10);
        when(socksRepository.findByColorAndCottonPart("Red", 80)).thenReturn(testSocks);
        SocksShortageException exception = assertThrows(SocksShortageException.class, () ->
                socksService.registerOutcome("Red", 80, 50)
        );

        assertEquals("Носков с параметрами Red, 80 на складе всего 10 шт., вместо необходимых 50 шт.",
                exception.getMessage());
    }

    @Test
    void testUpdateSock_success() {
        when(socksRepository.findById(any())).thenReturn(Optional.of(testSocks));
        when(socksRepository.save(any(Socks.class))).thenReturn(testSocks);

        UpdateSocksDto updateSocksDto = new UpdateSocksDto("Blue", 50, 200);
        SocksDto result = socksService.updateSock(1L, updateSocksDto);

        assertNotNull(result);
        assertEquals("Blue", result.getColor());
        assertEquals(50, result.getCottonPart());
        assertEquals(200, result.getQuantity());

        verify(socksRepository, times(1)).save(any(Socks.class));
    }

    @Test
    void testUpdateSock_notFound() {
        when(socksRepository.findById(any())).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () ->
                socksService.updateSock(1L, new UpdateSocksDto("Blue", 50, 200))
        );

        assertEquals("Носок c id 1 не найден", exception.getMessage());
    }
}
