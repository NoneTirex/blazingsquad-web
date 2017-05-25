package pl.edu.tirex.blazingsquad.web.channels;

import pl.edu.tirex.blazingsquad.web.channels.youtube.YoutubeChannelInformation;
import pl.edu.tirex.blazingsquad.web.channels.youtube.YoutubeChannelStatistics;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class YoutubeChannel
{
    @Id
    @Column(nullable = false, length = 32)
    private String id;

    @Embedded
    private YoutubeChannelInformation information;

    @Embedded
    private YoutubeChannelStatistics statistics;

    private String refreshToken;

    private String uploadsId;

    public YoutubeChannel()
    {
    }

    public YoutubeChannel(String id, String refreshToken)
    {
        this.id = id;
        this.refreshToken = refreshToken;
    }

    public String getId()
    {
        return id;
    }

    public YoutubeChannelInformation getInformation()
    {
        return information;
    }

    public void setInformation(YoutubeChannelInformation information)
    {
        this.information = information;
    }

    public YoutubeChannelStatistics getStatistics()
    {
        return statistics;
    }

    public void setStatistics(YoutubeChannelStatistics statistics)
    {
        this.statistics = statistics;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken)
    {
        this.refreshToken = refreshToken;
    }

    public String getUploadsId()
    {
        return uploadsId;
    }

    public void setUploadsId(String uploadsId)
    {
        this.uploadsId = uploadsId;
    }
}

