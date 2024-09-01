package com.demo.pulse.controller;

import com.demo.pulse.dto.EconomicDataDTO;
import com.demo.pulse.service.SDMXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sdmx")
public class SDMXController {

    @Autowired
    private SDMXService sdmxService;

    @GetMapping("/data")
    public List<EconomicDataDTO> getEconomicData(
            @RequestParam String dataflow,
            @RequestParam String key,
            @RequestParam String startPeriod,
            @RequestParam String endPeriod) {
        return sdmxService.getEconomicData(dataflow, key, startPeriod, endPeriod);
    }
}
