package com.zakneer.backend.controller;

import com.zakneer.backend.service.AmistadService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/amistad")
public class AmistadController {
    private AmistadService amistadService;
}
