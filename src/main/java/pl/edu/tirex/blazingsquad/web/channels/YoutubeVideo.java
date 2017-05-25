package pl.edu.tirex.blazingsquad.web.channels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;
import java.util.Date;

@Entity
public class YoutubeVideo
{
    @Id
    @Column(nullable = false, length = 32)
    private String id;

    @Column(length = 100)
    private String name;

    @Lob
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar published;

    @ManyToOne
    private YoutubeChannel channel;

    public YoutubeVideo()
    {
    }

    public YoutubeVideo(String id, YoutubeChannel channel)
    {
        this.id = id;
        this.channel = channel;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Calendar getPublished()
    {
        return published;
    }

    public void setPublished(Calendar published)
    {
        this.published = published;
    }

    public YoutubeChannel getChannel()
    {
        return channel;
    }
}
