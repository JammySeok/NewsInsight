package com.example.NewsInsight.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/subscription")
public class SubscriptionController {

    @GetMapping("/hotIssue")
    public String hotIssuePage() {
        return "subscription/hotIssue";
    }

    @GetMapping("/domainInsight")
    public String domainInsightPage() {
        return "subscription/domainInsight";
    }
}
