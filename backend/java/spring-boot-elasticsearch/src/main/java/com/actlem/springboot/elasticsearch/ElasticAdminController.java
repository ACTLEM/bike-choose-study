package com.actlem.springboot.elasticsearch;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class ElasticAdminController {

    private final ElasticAdminService adminService;

    /**
     * Endpoint to check that Solr is responding
     */
    @GetMapping("health")
    public ResponseEntity<String> elasticHealthCheck() {
        return adminService.elasticHealthCheck();
    }
}
