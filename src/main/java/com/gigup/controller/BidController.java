package com.gigup.controller;

import com.gigup.model.Bid;
import com.gigup.model.User;
import com.gigup.service.BidService;
import com.gigup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/bids")
public class BidController {

    @Autowired
    private BidService bidService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/my-bids")
    public String myBids(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Bid> bids = bidService.findByFreelancer(user);
        model.addAttribute("bids", bids);
        
        return "bids/my-bids";
    }
}

