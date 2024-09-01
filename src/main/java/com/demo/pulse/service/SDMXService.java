package com.demo.pulse.service;

// @author Leu A. Manuel
// @see https://github.com/Leupesquisa

import com.demo.pulse.dto.EconomicDataDTO;
import com.demo.pulse.model.EconomicData;
import com.demo.pulse.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class SDMXService {

    @Autowired
    private DataRepository economicDataRepository;

    @Autowired
    private DataService economicDataService;

    @Autowired
    private RestTemplate restTemplate;

    private static final String SDMX_API_URL = "https://sdw-wsrest.ecb.europa.eu/service/data/{flowRef}/{key}?startPeriod={startPeriod}&endPeriod={endPeriod}";

    public void fetchAndStoreEconomicData(String dataflow, String key) {
        String url = SDMX_API_URL.replace("{flowRef}", dataflow).replace("{key}", key);
        EconomicDataDTO[] response = restTemplate.getForObject(url, EconomicDataDTO[].class);

        if (response != null) {
            for (EconomicDataDTO dto : response) {
                EconomicData economicData = new EconomicData(null, dto.dataflow(), dto.key(), dto.timestamp(), dto.value());
                economicDataRepository.save(economicData);
            }
        }
    }

    public List<EconomicDataDTO> getRealTimeData() {
        return economicDataService.getRealTimeData();
    }

    public List<EconomicDataDTO> getHistoricalData(String dataflow, String key) {
        return economicDataService.getHistoricalData(dataflow, key);
    }

    public List<EconomicDataDTO> getEconomicData(String dataflow, String key, String startPeriod, String endPeriod) {
        String url = SDMX_API_URL
                .replace("{flowRef}", dataflow)
                .replace("{key}", key)
                .replace("{startPeriod}", startPeriod)
                .replace("{endPeriod}", endPeriod);

        EconomicDataDTO[] response = restTemplate.getForObject(url, EconomicDataDTO[].class);

        if (response != null) {
            return Arrays.asList(response);
        }
        return List.of();
    }
}
