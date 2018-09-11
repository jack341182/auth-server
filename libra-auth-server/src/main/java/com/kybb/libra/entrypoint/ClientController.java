package com.kybb.libra.entrypoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    JdbcClientDetailsService jdbcClientDetailsService;
    @PostMapping
    public ResponseEntity addClient(@RequestBody ClientDetails clientDetails) {
        jdbcClientDetailsService.addClientDetails(clientDetails);
        return ResponseEntity.ok(clientDetails);
    }

    @GetMapping
    public ResponseEntity listClients(){
        List<ClientDetails> clientDetails = jdbcClientDetailsService.listClientDetails();
        return ResponseEntity.ok(clientDetails);
    }
}
