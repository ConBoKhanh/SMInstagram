package com.example.sminstagram.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/live")
@RequiredArgsConstructor
@Tag(name = "Live", description = "15p check 1 lần để service sống")
public class LifeControler {

    @GetMapping()
    @Operation(summary = "Live service")
    public String liveService() {
        return "Chạy lại rồi nè";
    }

}
