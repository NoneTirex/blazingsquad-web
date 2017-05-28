package pl.edu.tirex.blazingsquad.web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.edu.tirex.blazingsquad.web.channels.YoutubeChannel;
import pl.edu.tirex.blazingsquad.web.repositories.AccountRepository;
import pl.edu.tirex.blazingsquad.web.repositories.YoutubeChannelRepository;
import pl.edu.tirex.blazingsquad.web.security.CryptographyService;

@EnableScheduling
@SpringBootApplication
public class BlazingSquadApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(BlazingSquadApplication.class);
    }
}
