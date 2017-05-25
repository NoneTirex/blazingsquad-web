package pl.edu.tirex.blazingsquad.web.channels.youtube;

import javax.persistence.Embeddable;
import java.math.BigInteger;

@Embeddable
public class YoutubeChannelStatistics
{
    private BigInteger subscribers;
    private BigInteger views;
    private BigInteger videos;

    public BigInteger getSubscribers()
    {
        return subscribers;
    }

    public void setSubscribers(BigInteger subscribers)
    {
        this.subscribers = subscribers;
    }

    public BigInteger getViews()
    {
        return views;
    }

    public void setViews(BigInteger views)
    {
        this.views = views;
    }

    public BigInteger getVideos()
    {
        return videos;
    }

    public void setVideos(BigInteger videos)
    {
        this.videos = videos;
    }

    @Override
    public String toString()
    {
        return "YoutubeChannelStatistics{" + "subscribers=" + subscribers + ", views=" + views + ", videos=" + videos + '}';
    }
}
