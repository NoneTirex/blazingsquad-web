package pl.edu.tirex.blazingsquad.web.groups;

import pl.edu.tirex.blazingsquad.web.Name;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group
{
    @Id
    @GeneratedValue
    private long id;

    @Embedded
    private Name displayedName;

    @Enumerated(EnumType.ORDINAL)
    @ElementCollection(targetClass = GroupFlag.class)
    private List<GroupFlag> flags = new ArrayList<>();

    public Group()
    {
    }

    public Group(Name displayedName)
    {
        this.displayedName = displayedName;
    }

    public Name getDisplayedName()
    {
        return displayedName;
    }

    public List<GroupFlag> getFlags()
    {
        return flags;
    }
}
