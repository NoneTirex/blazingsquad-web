package pl.edu.tirex.blazingsquad.web.security;

import javax.persistence.Column;

public class SaltedPassword
{
    private String hash;
    private String salt;

    public SaltedPassword()
    {
    }

    public SaltedPassword(String hash, String salt)
    {
        this.hash = hash;
        this.salt = salt;
    }

    public String getHash()
    {
        return hash;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }
}
