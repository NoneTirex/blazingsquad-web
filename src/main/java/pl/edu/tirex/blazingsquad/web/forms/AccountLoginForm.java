package pl.edu.tirex.blazingsquad.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountLoginForm
{
    @NotNull
    @Size(min = 3, max = 32)
    private String username;

    @NotNull
    @Size(min = 6)
    private String password;

    public AccountLoginForm()
    {
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
