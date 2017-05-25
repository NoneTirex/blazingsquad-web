package pl.edu.tirex.blazingsquad.web.security;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HashedPassword
{
    @Column(nullable = false, length = 32)
    private String method;

    @Column(nullable = false)
    private String hash;

    public HashedPassword()
    {
    }

    public HashedPassword(String method, String password)
    {
        this.method = method;
        this.hash = password;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getHash()
    {
        return hash;
    }

    public void setHash(String password)
    {
        this.hash = password;
    }

    @Override
    public String toString()
    {
        return "HashedPassword{" + "method='" + method + '\'' + ", hash='" + hash + '\'' + '}';
    }
}
