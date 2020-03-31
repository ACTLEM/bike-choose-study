package com.actlem.springboot.elasticsearch;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

/**
 * Mock the {@link Terms.Bucket} for tests
 */
public class BucketMock implements Terms.Bucket {

    private String key;
    private long documentCount;

    BucketMock(String key, long documentCount){
        this.key = key;
        this.documentCount = documentCount;
    }

    @Override
    public Number getKeyAsNumber() {
        throw(new UnsupportedOperationException("getKeyAsNumber of BucketMock must not to be called"));
    }

    @Override
    public long getDocCountError() {
        throw(new UnsupportedOperationException("getDocCountError of BucketMock must not to be called"));
    }

    @Override
    public Object getKey() {
        throw(new UnsupportedOperationException("getKey of BucketMock must not to be called"));
    }

    @Override
    public String getKeyAsString() {
        return key;
    }

    @Override
    public long getDocCount() {
        return documentCount;
    }

    @Override
    public Aggregations getAggregations() {
        throw(new UnsupportedOperationException("getAggregations of BucketMock must not to be called"));
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) {
        throw(new UnsupportedOperationException("toXContent of BucketMock must not to be called"));
    }
}
