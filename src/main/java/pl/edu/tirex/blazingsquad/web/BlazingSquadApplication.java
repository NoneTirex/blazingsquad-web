package pl.edu.tirex.blazingsquad.web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
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

    //    @Bean
    public CommandLineRunner initChannels(YoutubeChannelRepository channelRepository, AccountRepository accountRepository, CryptographyService cryptographyService)
    {
        return args ->
        {
            //            Account account = new Account(new Name("tirex", null), cryptographyService.hashPassword("samoloty2"), "tirexgta@gmail.com");
            //            accountRepository.save(account);
            //            System.out.println(account);
            //            accountRepository.save(account);
            //            System.out.println(account);
            //            account = accountRepository.findByName("tirex");
            //            System.out.println(account);
            //
            //            YoutubeChannel youtubeChannel = new YoutubeChannel("UCWUw2iQzT8ZKPuzU3FrF9UA", "");
            //            channelRepository.save(youtubeChannel);
            //            System.out.println(youtubeChannel);
            //            channelRepository.save(youtubeChannel);
            //            System.out.println(youtubeChannel);
            //            channelRepository.save(youtubeChannel);
            //            System.out.println(youtubeChannel);
            //
            //            //            groupFlagRepository.save(GroupFlag.USER);
            //
            //            Group group = new Group(new Name("user", "uzytkownik"));
            //            //            group.getFlags().add(GroupFlag.USER);
            //            groupRepository.save(group);
            //
            //            Account account = new Account(new Name("admin", "SzymonSzymek"), "admin", "mail@gmail.com");
            //            account.setGroup(group);
            //            account.setYoutubeChannel(youtubeChannel);
            //            accountRepository.save(account);
            //
            //            System.out.println();
            //            account = accountRepository.findByName("szymon");
            //            System.out.println();
            //            System.out.println(account);
        };
    }
}
