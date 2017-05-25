package pl.edu.tirex.blazingsquad.web.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.edu.tirex.blazingsquad.web.channels.YoutubeChannel;
import pl.edu.tirex.blazingsquad.web.channels.YoutubeVideo;

import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import java.util.List;

public interface YoutubeVideoRepository extends JpaRepository<YoutubeVideo, String>
{
    YoutubeVideo getYoutubeVideoById(String videoId);

    List<YoutubeVideo> getYoutubeVideosByChannel(YoutubeChannel channel);

    List<YoutubeVideo> getYoutubeVideosByOrderByPublishedDesc(Pageable pageable);
}
