package pl.edu.tirex.blazingsquad.web.forms;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

public class AccountRegisterForm extends AccountLoginForm
{
    @Email
    @NotNull
    private String email;

    public AccountRegisterForm()
    {
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
