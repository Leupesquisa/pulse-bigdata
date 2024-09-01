package com.demo.pulse;

import com.demo.pulse.dto.EconomicDataDTO;
import com.demo.pulse.model.EconomicData;
import com.demo.pulse.repository.DataRepository;
import com.demo.pulse.service.DataService;
import com.demo.pulse.service.SDMXService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SDMXTest {

    @Mock
    private DataRepository economicDataRepository;

    @Mock
    private DataService economicDataService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SDMXService sdmxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchAndStoreEconomicData() {
        // Simula a resposta da API SDMX
        EconomicDataDTO[] mockResponse = new EconomicDataDTO[]{
                new EconomicDataDTO("EXR", "D.USD.EUR.SP00.A", 1627833600L, 1.18),
                new EconomicDataDTO("EXR", "D.USD.EUR.SP00.A", 1627916400L, 1.19)
        };

        when(restTemplate.getForObject(anyString(), eq(EconomicDataDTO[].class)))
                .thenReturn(mockResponse);

        // Executa o método a ser testado
        sdmxService.fetchAndStoreEconomicData("EXR", "D.USD.EUR.SP00.A");

        // Verifica se os dados foram armazenados corretamente no repositório
        verify(economicDataRepository, times(2)).save(any(EconomicData.class));
    }

    @Test
    void testGetRealTimeData() {
        // Simula o dado para os dados em tempo real
        List<EconomicDataDTO> mockRealTimeData = Arrays.asList(
                new EconomicDataDTO("EXR", "D.USD.EUR.SP00.A", 1627833600L, 1.18),
                new EconomicDataDTO("EXR", "D.USD.EUR.SP00.A", 1627916400L, 1.19)
        );

        when(economicDataService.getRealTimeData())
                .thenReturn(mockRealTimeData);

        // Executa o método a ser testado
        List<EconomicDataDTO> realTimeData = sdmxService.getRealTimeData();

        // Verifica se os dados retornados são os esperados
        assertEquals(2, realTimeData.size());
        assertEquals("EXR", realTimeData.get(0).dataflow());
    }

    @Test
    void testGetHistoricalData() {
        // Simula o dado para os dados históricos
        List<EconomicDataDTO> mockHistoricalData = Arrays.asList(
                new EconomicDataDTO("EXR", "D.USD.EUR.SP00.A", 1627833600L, 1.18),
                new EconomicDataDTO("EXR", "D.USD.EUR.SP00.A", 1627916400L, 1.19)
        );

        when(economicDataService.getHistoricalData("EXR", "D.USD.EUR.SP00.A"))
                .thenReturn(mockHistoricalData);

        // Executa o método a ser testado
        List<EconomicDataDTO> historicalData = sdmxService.getHistoricalData("EXR", "D.USD.EUR.SP00.A");

        // Verifica se os dados retornados são os esperados
        assertEquals(2, historicalData.size());
        assertEquals(1.18, historicalData.get(0).value());
    }

    @Test
    void testGetEconomicData() {
        // Simula a resposta da API SDMX com período específico
        EconomicDataDTO[] mockResponse = new EconomicDataDTO[]{
                new EconomicDataDTO("EXR", "D.USD.EUR.SP00.A", 1627833600L, 1.18),
                new EconomicDataDTO("EXR", "D.USD.EUR.SP00.A", 1627916400L, 1.19)
        };

        when(restTemplate.getForObject(anyString(), eq(EconomicDataDTO[].class)))
                .thenReturn(mockResponse);

        // Executa o método a ser testado
        List<EconomicDataDTO> economicData = sdmxService.getEconomicData("EXR", "D.USD.EUR.SP00.A", "2021-07-01", "2021-07-30");

        // Verifica se os dados retornados são os esperados
        assertEquals(2, economicData.size());
        assertEquals(1.18, economicData.get(0).value());
    }
}
