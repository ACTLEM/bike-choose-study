package com.actlem.springboot.solr;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class SolrAdminController {

    private final SolrAdminService adminService;

    /**
     * Endpoint to check that Solr is responding
     */
    @GetMapping("ping")
    public ResponseEntity<String> pingSolr() {
        return new ResponseEntity<>(adminService.pingSolr(), HttpStatus.OK);
    }
}
