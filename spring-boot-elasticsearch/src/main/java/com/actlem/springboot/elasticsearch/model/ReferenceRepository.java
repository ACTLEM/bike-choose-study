package com.actlem.springboot.elasticsearch.model;

/**
 * Must be implemented by a enum which represents a repository with referential values.
 */
public interface ReferenceRepository {

    String getLabel();
}
