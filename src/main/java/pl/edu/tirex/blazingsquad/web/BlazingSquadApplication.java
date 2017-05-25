package pl.edu.tirex.blazingsquad.web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.edu.tirex.blazingsquad.web.channels.YoutubeChannel;
import pl.edu.tirex.blazingsquad.web.groups.Group;
import pl.edu.tirex.blazingsquad.web.repositories.AccountRepository;
import pl.edu.tirex.blazingsquad.web.repositories.GroupRepository;
import pl.edu.tirex.blazingsquad.web.repositories.YoutubeChannelRepository;

@EnableScheduling
@SpringBootApplication
public class BlazingSquadApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(BlazingSquadApplication.class);
    }

    //    @Bean
    public CommandLineRunner initChannels(YoutubeChannelRepository channelRepository, AccountRepository accountRepository, GroupRepository groupRepository)
    {
        return args ->
        {
//            YoutubeChannel youtubeChannel = new YoutubeChannel("UCWUw2iQzT8ZKPuzU3FrF9UA", "");
//            channelRepository.save(youtubeChannel);
//            channelRepository.save(new YoutubeChannel("UCL1s7OtDPaX3SdhW5433PRw", ""));
//            channelRepository.save(new YoutubeChannel("UCsTJxi_9fODHdO_1bmJbqvQ", ""));
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
