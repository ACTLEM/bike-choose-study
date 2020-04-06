package com.actlem.springboot.elasticsearch;

import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;

import java.util.List;

/**
 * Mock the {@link ParsedFilter} for tests
 */
public class ParsedFilterMock extends ParsedFilter {

    private Aggregations aggregations;

    ParsedFilterMock(String name, ParsedStringTermsMock parsedStringTerms){
        setName(name);
        aggregations = new Aggregations(List.of(parsedStringTerms));
    }

    @Override
    public Aggregations getAggregations() {
        return aggregations;
    }
}
