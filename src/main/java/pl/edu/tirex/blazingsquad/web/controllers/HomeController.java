package pl.edu.tirex.blazingsquad.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.tirex.blazingsquad.web.Account;
import pl.edu.tirex.blazingsquad.web.AccountManager;
import pl.edu.tirex.blazingsquad.web.groups.Flag;
import pl.edu.tirex.blazingsquad.web.groups.GroupFlag;
import pl.edu.tirex.blazingsquad.web.groups.RequiredActivate;
import pl.edu.tirex.blazingsquad.web.groups.RequiredAuthorized;
import pl.edu.tirex.blazingsquad.web.repositories.AccountRepository;
import pl.edu.tirex.blazingsquad.web.repositories.YoutubeChannelRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class HomeController
{
    @RequestMapping("/")
    public String index(Model model)
    {
//        model.addAttribute("channels", this.youtubeChannelRepository.findAllByInformationIsNotNull());
        return "index";
    }

    @RequestMapping("/index")
    public String index()
    {
        return "redirect:/";
    }

    @RequiredAuthorized("/login")
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, Account account, Model model)
    {
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            session.invalidate();
        }
        return "redirect:/";
    }

    @RequestMapping("/user")
    @RequiredAuthorized
    @RequiredActivate("/not-activated")
    public String userIndex(HttpServletRequest request, Account account, Model model)
    {
        System.out.println("USER PAGE");
        model.addAttribute("title", "Witaj " + account.getName().getDisplayName() + "!");
        return "index";
    }
}
