package com.actlem.springboot.elasticsearch;

import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.List;

/**
 * Mock the {@link ParsedStringTerms} for tests
 */
public class ParsedStringTermsMock extends ParsedStringTerms {

    private List<Terms.Bucket> buckets;

    ParsedStringTermsMock(String name, List<Terms.Bucket> buckets){
        setName(name);
        this.buckets = buckets;
    }

    @Override
    public List<? extends Terms.Bucket> getBuckets() {
        return buckets;
    }

}
