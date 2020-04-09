package com.actlem.springboot.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableSolrRepositories
public class SolrConfig {

    @Value("${solr.host}")
    private String solrHost;

    @Bean
    public SolrClient solrClient() {
        return new Builder()
                .withBaseSolrUrl(solrHost)
                .build();
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient client) {
        return new SolrTemplate(client);
    }
}
