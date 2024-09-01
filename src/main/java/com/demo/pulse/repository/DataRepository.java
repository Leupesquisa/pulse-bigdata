package com.demo.pulse.repository;
// @author Leu A. Manuel
// @see https://github.com/Leupesquisa

import com.demo.pulse.model.EconomicData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends MongoRepository<EconomicData, String> {
    List<EconomicData> findByDataflowAndKey(String dataflow, String key);
}

