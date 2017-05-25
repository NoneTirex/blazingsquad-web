package pl.edu.tirex.blazingsquad.web;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

public class ConfirmationRegister
{
    private String code;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar accepted;

    public ConfirmationRegister()
    {
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Calendar getAccepted()
    {
        return accepted;
    }

    public void setAccepted(Calendar accepted)
    {
        this.accepted = accepted;
    }
}
