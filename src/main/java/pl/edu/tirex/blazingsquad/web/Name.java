package pl.edu.tirex.blazingsquad.web;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name
{
    @Column(length = 32, nullable = false, unique = true)
    private String name;

    @Column(length = 32, nullable = true, unique = true)
    private String displayName;

    public Name()
    {
    }

    public Name(String name, String displayName)
    {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDisplayName()
    {
        if (this.displayName != null)
        {
            return this.displayName;
        }
        return this.name;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    @Override
    public String toString()
    {
        return "DisplayedName{" + "name='" + name + '\'' + ", displayName='" + displayName + '\'' + '}';
    }
}
