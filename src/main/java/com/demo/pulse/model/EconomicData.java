package com.demo.pulse.model;
// @author Leu A. Manuel
// @see https://github.com/Leupesquisa

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "economic_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EconomicData {

    @Id
    private String id;
    private String dataflow;
    private String key;
    private long timestamp;
    private double value;
}



