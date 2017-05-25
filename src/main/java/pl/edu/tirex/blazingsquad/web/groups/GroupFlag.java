package pl.edu.tirex.blazingsquad.web.groups;

import java.util.HashMap;
import java.util.Map;

public enum GroupFlag
{
    USER("flaga uzytkownika");

    private static final Map<String, GroupFlag> flagByName = new HashMap<>();

    private String displayName;

    GroupFlag(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public static GroupFlag getFlagByName(String name)
    {
        return flagByName.get(name.toLowerCase());
    }

    static
    {
        for (GroupFlag groupFlag : values())
        {
            flagByName.put(groupFlag.name().toLowerCase(), groupFlag);
        }
    }
}
