package com.demo.pulse.service;

import com.demo.pulse.dto.EconomicDataDTO;
import com.demo.pulse.model.EconomicData;
import com.demo.pulse.repository.DataRepository;
import com.demo.pulse.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataService {

    @Autowired
    private DataRepository economicDataRepository;

    public List<EconomicDataDTO> getRealTimeData() {
        return economicDataRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EconomicDataDTO> getHistoricalData(String dataflow, String key) {
        return economicDataRepository.findByDataflowAndKey(dataflow, key).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private EconomicDataDTO convertToDTO(EconomicData economicData) {
        return new EconomicDataDTO(
                economicData.getDataflow(),
                economicData.getKey(),
                economicData.getTimestamp(),
                economicData.getValue()
        );
    }
}
