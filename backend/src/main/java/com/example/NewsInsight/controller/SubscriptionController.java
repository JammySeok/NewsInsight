package com.example.NewsInsight.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/subscription")
public class SubscriptionController {

    @GetMapping("/pay")
    public String subscriptionPage() {
        return "/subscription/subscriptionPage";
    }

    @GetMapping("/hotIssue")
    public String hotIssuePage() {
        return "subscription/hotIssue";
    }

    @GetMapping("/domainInsight")
    public String domainInsightPage() {
        return "subscription/domainInsight";
    }
}
