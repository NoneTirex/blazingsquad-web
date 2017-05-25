package pl.edu.tirex.blazingsquad.web.controllers;

import com.sparkpost.exception.SparkPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.tirex.blazingsquad.web.Account;
import pl.edu.tirex.blazingsquad.web.AccountManager;
import pl.edu.tirex.blazingsquad.web.security.HashException;
import pl.edu.tirex.blazingsquad.web.forms.AccountRegisterForm;
import pl.edu.tirex.blazingsquad.web.groups.RequiredUnauthorized;
import pl.edu.tirex.blazingsquad.web.repositories.AccountRepository;
import pl.edu.tirex.blazingsquad.web.utils.HttpRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequiredUnauthorized
@RequestMapping("/register")
public class RegisterController
{
    private final AccountRepository accountRepository;
    private final AccountManager accountManager;

    @Autowired
    public RegisterController(AccountRepository accountRepository, AccountManager accountManager)
    {
        this.accountRepository = accountRepository;
        this.accountManager = accountManager;
    }

    @GetMapping
    public String register(AccountRegisterForm accountRegisterForm)
    {
        return "register";
    }

    @PostMapping
    public String registerWork(HttpServletRequest request, @Valid AccountRegisterForm accountRegisterForm, BindingResult bindingResult)
    {
        if (this.accountRepository.findByName(accountRegisterForm.getUsername()) != null)
        {
            bindingResult.rejectValue("username", "error.name.already-have", "Jest juz uzytkownik o takiej nazwie.");
        }
        if (bindingResult.hasErrors())
        {
            return "register";
        }
        try
        {
            Account account = this.accountManager.createAccount(accountRegisterForm);
            this.accountManager.sendConfirmRegister(account, HttpRequestUtils.getHostURL(request));
            this.accountRepository.save(account);
            this.accountManager.addAccountToSession(request, account);
        }
        catch (SparkPostException | HashException e)
        {
            e.printStackTrace();
        } return "redirect:/user";
    }
}
