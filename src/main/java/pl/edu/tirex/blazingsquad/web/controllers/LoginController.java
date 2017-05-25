package pl.edu.tirex.blazingsquad.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.tirex.blazingsquad.web.Account;
import pl.edu.tirex.blazingsquad.web.AccountManager;
import pl.edu.tirex.blazingsquad.web.security.HashException;
import pl.edu.tirex.blazingsquad.web.forms.AccountLoginForm;
import pl.edu.tirex.blazingsquad.web.groups.RequiredUnauthorized;
import pl.edu.tirex.blazingsquad.web.repositories.AccountRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredUnauthorized
@RequestMapping("/login")
public class LoginController
{
    private final AccountRepository accountRepository;
    private final AccountManager accountManager;

    @Autowired
    public LoginController(AccountRepository accountRepository, AccountManager accountManager)
    {
        this.accountRepository = accountRepository;
        this.accountManager = accountManager;
    }

    @GetMapping
    public String login(AccountLoginForm accountLoginForm)
    {
        return "login";
    }

    @PostMapping
    public String loginWork(HttpServletRequest request, @Valid AccountLoginForm accountLoginForm, BindingResult bindingResult)
    {
        Account account = this.accountRepository.findByName(accountLoginForm.getUsername());
        if (account == null)
        {
            account = this.accountRepository.findByEmail(accountLoginForm.getUsername());
        }
        if (account == null)
        {
            bindingResult.rejectValue("username", "error.name.not-have", "Nie znaleziono użytkownika o takiej nazwie.");
        }
        try
        {
            if (account != null && !this.accountManager.comparePassword(account, accountLoginForm.getPassword()))
            {
                bindingResult.rejectValue("password", "error.password.failed", "Podano nieprawidłowe haslo.");
            }
        }
        catch (HashException e)
        {
            bindingResult.rejectValue("password", "error.password.exception", "Wyjebalo serwis");
        }
        if (bindingResult.hasErrors())
        {
            return "login";
        }
        this.accountManager.addAccountToSession(request, account);
        return "redirect:/user";
    }
}
