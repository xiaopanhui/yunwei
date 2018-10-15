package com.gzsf.operation.controller;

import com.gzsf.operation.service.ConnectionConfigService;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class ConnectionConfigController {

    @Autowired
    private ConnectionConfigService connectionConfigService;

}