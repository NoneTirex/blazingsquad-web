package pl.edu.tirex.blazingsquad.web.confirmations;

import pl.edu.tirex.blazingsquad.web.Account;

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
@Table(name = "confirmations")
public class Confirmation
{
    @Id
    @GeneratedValue
    private long id;

    @Column(length = 32, nullable = false)
    private String code;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar created = Calendar.getInstance();

    @OneToOne
    private Account account;

    public Confirmation()
    {
    }

    public Confirmation(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Confirmation))
        {
            return false;
        }
        Confirmation that = (Confirmation) o;
        return id == that.id && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, code);
    }

    @Override
    public String toString()
    {
        return "Confirmation{" + "id=" + id + ", code='" + code + '\'' + '}';
    }
}
