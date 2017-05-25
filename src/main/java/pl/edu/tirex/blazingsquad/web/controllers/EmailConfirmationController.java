package pl.edu.tirex.blazingsquad.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/u")
public class EmailConfirmationController
{
    @RequestMapping("/{action}/{code}")
    private String activateAccount(@PathVariable("action") String action, @PathVariable("code") String code)
    {
        System.out.println(code);
        return "index";
    }
}
