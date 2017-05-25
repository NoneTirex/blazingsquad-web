package pl.edu.tirex.blazingsquad.web.channels.youtube;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class YoutubeChannelInformation
{
    private String name;

    @Lob
    private String description;

    public YoutubeChannelInformation()
    {
    }

    public YoutubeChannelInformation(String name, String description)
    {
        this.name = name;
        this.description = description;
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

    @Override
    public String toString()
    {
        return "ChannelInformation{" + "name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }
}
