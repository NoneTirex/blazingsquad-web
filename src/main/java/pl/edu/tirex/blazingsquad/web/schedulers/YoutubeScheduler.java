package pl.edu.tirex.blazingsquad.web.schedulers;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ChannelSnippet;
import com.google.api.services.youtube.model.ChannelStatistics;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.edu.tirex.blazingsquad.web.configuration.ApplicationConfiguration;
import pl.edu.tirex.blazingsquad.web.channels.YoutubeChannel;
import pl.edu.tirex.blazingsquad.web.channels.YoutubeVideo;
import pl.edu.tirex.blazingsquad.web.channels.youtube.YoutubeChannelInformation;
import pl.edu.tirex.blazingsquad.web.channels.youtube.YoutubeChannelStatistics;
import pl.edu.tirex.blazingsquad.web.repositories.YoutubeChannelRepository;
import pl.edu.tirex.blazingsquad.web.repositories.YoutubeVideoRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class YoutubeScheduler
{
    private final ApplicationConfiguration configuration;
    private final YouTube youTube;
    private final YoutubeChannelRepository youtubeChannelRepository;
    private final YoutubeVideoRepository youtubeVideoRepository;

    @Autowired
    public YoutubeScheduler(ApplicationConfiguration configuration, YouTube youTube, YoutubeChannelRepository youtubeChannelRepository, YoutubeVideoRepository youtubeVideoRepository)
    {
        this.configuration = configuration;
        this.youTube = youTube;
        this.youtubeChannelRepository = youtubeChannelRepository;
        this.youtubeVideoRepository = youtubeVideoRepository;
    }

    @PostConstruct
    public void init() throws IOException
    {
        System.out.println("POST CONSTRUCT");
        this.execute();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void run() throws IOException
    {
        System.out.println("LAUNCH CRON EVERY 1 HOUR");
        this.execute();
    }

    public void execute() throws IOException
    {
        Map<String, YoutubeChannel> channelsMap = this.youtubeChannelRepository.findAll().stream().collect(Collectors.toMap(YoutubeChannel::getId, t -> t));
        if (channelsMap.size() <= 0)
        {
            return;
        }
        YouTube.Channels.List channelsRequest = this.youTube.channels().list("snippet,statistics,contentDetails");
        channelsRequest.setKey(this.configuration.getYoutube().getKey());
        channelsRequest.setMaxResults(50L);
        StringBuilder channelsIdString = new StringBuilder();
        int i = 0;
        for (YoutubeChannel channel : channelsMap.values())
        {
            channelsIdString.append(channel.getId());
            if (++i < channelsMap.size())
            {
                channelsIdString.append(",");
            }
        }
        channelsRequest.setId(channelsIdString.toString());
        List<Channel> youtubeChannels = new ArrayList<>();
        ChannelListResponse channelsResponse = null;
        while ((channelsResponse == null || (!Objects.equals(channelsResponse.getPageInfo().getResultsPerPage(), channelsResponse.getPageInfo().getTotalResults()) && channelsResponse.getNextPageToken() != null)) && (channelsResponse = channelsRequest.execute()) != null)
        {
            youtubeChannels.addAll(channelsResponse.getItems());
            channelsRequest.setPageToken(channelsResponse.getNextPageToken());
        }
        for (Channel youtubeChannel : youtubeChannels)
        {
            YoutubeChannel channel = channelsMap.get(youtubeChannel.getId());
            if (channel == null)
            {
                continue;
            }
            if (channel.getInformation() == null)
            {
                channel.setInformation(new YoutubeChannelInformation());
            }
            ChannelSnippet snippet = youtubeChannel.getSnippet();
            channel.getInformation().setName(snippet.getTitle());
            channel.getInformation().setDescription(snippet.getDescription());

            if (channel.getStatistics() == null)
            {
                channel.setStatistics(new YoutubeChannelStatistics());
            }
            ChannelStatistics statistics = youtubeChannel.getStatistics();
            channel.getStatistics().setSubscribers(statistics.getSubscriberCount());
            channel.getStatistics().setViews(statistics.getViewCount());
            channel.getStatistics().setVideos(statistics.getVideoCount());

            if (youtubeChannel.getContentDetails() != null && youtubeChannel.getContentDetails().getRelatedPlaylists() != null)
            {
                channel.setUploadsId(youtubeChannel.getContentDetails().getRelatedPlaylists().getUploads());
            }
            else
            {
                channel.setUploadsId(null);
            }

            this.youtubeChannelRepository.save(channel);

            if (channel.getUploadsId() != null)
            {
                Map<String, YoutubeVideo> videosMap = this.youtubeVideoRepository.getYoutubeVideosByChannel(channel).stream().collect(Collectors.toMap(YoutubeVideo::getId, t -> t));

                YouTube.PlaylistItems.List playlistItemsRequest = this.youTube.playlistItems().list("snippet");
                playlistItemsRequest.setMaxResults(50L);
                playlistItemsRequest.setPlaylistId(channel.getUploadsId());
                playlistItemsRequest.setKey(this.configuration.getYoutube().getKey());

                List<PlaylistItem> playlistItems = new ArrayList<>();
                PlaylistItemListResponse playlistItemsResponse = null;
                while ((playlistItemsResponse == null || (!Objects.equals(playlistItemsResponse.getPageInfo().getResultsPerPage(), playlistItemsResponse.getPageInfo().getTotalResults()) && playlistItemsResponse.getNextPageToken() != null)) && (playlistItemsResponse = playlistItemsRequest.execute()) != null)
                {
                    playlistItems.addAll(playlistItemsResponse.getItems());
                    playlistItemsRequest.setPageToken(playlistItemsResponse.getNextPageToken());
                }
                for (PlaylistItem playlistItem : playlistItems)
                {
                    YoutubeVideo video = videosMap.get(playlistItem.getSnippet().getResourceId().getVideoId());
                    if (video == null)
                    {
                        video = new YoutubeVideo(playlistItem.getSnippet().getResourceId().getVideoId(), channel);
                    }
                    video.setName(playlistItem.getSnippet().getTitle());
                    video.setDescription(playlistItem.getSnippet().getDescription());
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(new Date(playlistItem.getSnippet().getPublishedAt().getValue()));
                    video.setPublished(calendar);
                    this.youtubeVideoRepository.save(video);
                    videosMap.remove(video.getId());
                }
                this.youtubeVideoRepository.delete(videosMap.values());
            }
        }
    }
}
