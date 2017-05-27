package pl.edu.tirex.blazingsquad.web;

import org.thymeleaf.util.Validate;
import pl.edu.tirex.blazingsquad.web.channels.YoutubeChannel;
import pl.edu.tirex.blazingsquad.web.security.HashedPassword;
import pl.edu.tirex.blazingsquad.web.groups.Group;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;
import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account
{
    @Id
    @GeneratedValue
    private long id;

    private Name name;

    @Column(nullable = false)
    private HashedPassword password;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne
    private YoutubeChannel youtubeChannel;

    @OneToOne
    private Group group;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar created = Calendar.getInstance();

    public Account()
    {
    }

    public Account(Name name, HashedPassword password, String email)
    {
        Validate.notNull(name, "Name can not be a null!");
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public long getId()
    {
        return id;
    }

    public Name getName()
    {
        return name;
    }

    public HashedPassword getPassword()
    {
        return password;
    }

    public void setPassword(HashedPassword password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public YoutubeChannel getYoutubeChannel()
    {
        return youtubeChannel;
    }

    public void setYoutubeChannel(YoutubeChannel youtubeChannel)
    {
        this.youtubeChannel = youtubeChannel;
    }

    public Group getGroup()
    {
        return group;
    }

    public void setGroup(Group group)
    {
        this.group = group;
    }

    public Calendar getCreated()
    {
        return created;
    }

    public void setCreated(Calendar created)
    {
        this.created = created;
    }

    public boolean isActive()
    {
//        return this.confirmation != null && this.confirmation.getCode() == null && this.confirmation.getAccepted() != null;
        return true;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Account))
        {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(name, account.name) && Objects.equals(email, account.email);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, email);
    }

    @Override
    public String toString()
    {
        return "Account{" + "id=" + id + ", name=" + name + ", password='" + password + '\'' + ", email='" + email + '\'' + ", youtubeChannel=" + youtubeChannel + ", group=" + group + '}';
    }
}
