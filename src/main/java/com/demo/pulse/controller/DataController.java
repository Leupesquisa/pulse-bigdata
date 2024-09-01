package com.demo.pulse.controller;

import com.demo.pulse.dto.EconomicDataDTO;
import com.demo.pulse.service.DataService;
import com.demo.pulse.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataController {

    private final DataService economicDataService;

    @GetMapping("/real-time")
    public List<EconomicDataDTO> getRealTimeData() {
        return economicDataService.getRealTimeData();
    }

    @GetMapping("/historical")
    public List<EconomicDataDTO> getHistoricalData(
            @RequestParam String dataflow,
            @RequestParam String key) {
        return economicDataService.getHistoricalData(dataflow, key);
    }
}
