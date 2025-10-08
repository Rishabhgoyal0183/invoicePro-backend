package com.invoicePro.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aws")
public class AWSController {

    @GetMapping("/test")
    public String test() {

        return "AWS Controller is working!";
    }
}
