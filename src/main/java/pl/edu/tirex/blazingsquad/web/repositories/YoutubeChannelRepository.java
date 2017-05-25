package pl.edu.tirex.blazingsquad.web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.tirex.blazingsquad.web.channels.YoutubeChannel;

import java.util.List;

public interface YoutubeChannelRepository extends JpaRepository<YoutubeChannel, String>
{
    List<YoutubeChannel> findAllByInformationIsNotNull();
}