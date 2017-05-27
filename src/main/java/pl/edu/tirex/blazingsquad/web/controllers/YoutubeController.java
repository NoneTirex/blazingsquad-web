package pl.edu.tirex.blazingsquad.web.controllers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ChannelSnippet;
import com.google.api.services.youtube.model.ChannelStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.tirex.blazingsquad.web.Account;
import pl.edu.tirex.blazingsquad.web.channels.YoutubeChannel;
import pl.edu.tirex.blazingsquad.web.channels.youtube.YoutubeChannelInformation;
import pl.edu.tirex.blazingsquad.web.channels.youtube.YoutubeChannelStatistics;
import pl.edu.tirex.blazingsquad.web.groups.RequiredAuthorized;
import pl.edu.tirex.blazingsquad.web.repositories.AccountRepository;
import pl.edu.tirex.blazingsquad.web.repositories.YoutubeChannelRepository;
import pl.edu.tirex.blazingsquad.web.services.ApiService;
import pl.edu.tirex.blazingsquad.web.utils.HttpRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequiredAuthorized
@RequestMapping("/youtube")
public class YoutubeController
{
    private final YoutubeChannelRepository youtubeChannelRepository;
    private final AccountRepository accountRepository;
    private final GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow;

    @Autowired
    public YoutubeController(YoutubeChannelRepository youtubeChannelRepository, AccountRepository accountRepository, GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow)
    {
        this.youtubeChannelRepository = youtubeChannelRepository;
        this.accountRepository = accountRepository;
        this.googleAuthorizationCodeFlow = googleAuthorizationCodeFlow;
    }

    @RequestMapping(value = "/auth")
    public String youtubeAuth(HttpServletRequest request, Account account, @RequestParam(value = "code", required = false) String code, @RequestParam(value = "error", required = false) String error)
    {
        if (code == null && (error == null || !error.equalsIgnoreCase("true")))
        {
            return "redirect:/";
        }
        GoogleAuthorizationCodeTokenRequest codeTokenRequest = this.googleAuthorizationCodeFlow.newTokenRequest(code);
        codeTokenRequest.setRedirectUri(HttpRequestUtils.getHostURL(request) + "/youtube/auth");
        try
        {
            GoogleTokenResponse tokenResponse = codeTokenRequest.execute();
            if (tokenResponse.getRefreshToken() == null)
            {
                return "redirect:login";
            }
            Credential credential = new GoogleCredential();
            credential.setAccessToken(tokenResponse.getAccessToken());
            YouTube youTube = new YouTube.Builder(ApiService.HTTP_TRANSPORT, ApiService.JSON_FACTORY, credential).setApplicationName("blazingsquad-web").build();
            ChannelListResponse channelResponse = youTube.channels().list("snippet,contentDetails,statistics").setMine(true).execute();

            System.out.println((account != null && channelResponse.size() > 0) + " [" + (account != null) + "]");
            if (account != null && channelResponse.size() > 0)
            {
                Channel youtubeChannel = channelResponse.getItems().get(0);
                YoutubeChannel channel = new YoutubeChannel(youtubeChannel.getId(), tokenResponse.getRefreshToken());
                channel.setUploadsId(youtubeChannel.getContentDetails().getRelatedPlaylists().getUploads());
                channel.setRefreshToken(tokenResponse.getRefreshToken());

                YoutubeChannelInformation channelInformation = channel.getInformation();
                ChannelSnippet snippet = youtubeChannel.getSnippet();
                if (snippet == null)
                {
                    System.out.println("Ponownie sie cos spierdolilo");
                    return "redirect:login";
                }
                if (channelInformation == null)
                {
                    channelInformation = new YoutubeChannelInformation();
                }
                channelInformation.setName(snippet.getTitle());
                channelInformation.setDescription(snippet.getDescription());
                channel.setInformation(channelInformation);

                ChannelStatistics statistics = youtubeChannel.getStatistics();
                if (statistics != null)
                {
                    YoutubeChannelStatistics channelStatistics = channel.getStatistics();
                    if (channelStatistics == null)
                    {
                        channelStatistics = new YoutubeChannelStatistics();
                    }
                    channelStatistics.setSubscribers(statistics.getSubscriberCount());
                    channelStatistics.setViews(statistics.getViewCount());
                    channelStatistics.setVideos(statistics.getVideoCount());
                    channel.setStatistics(channelStatistics);
                }

                account.setYoutubeChannel(channel);
                this.youtubeChannelRepository.save(channel);
                this.accountRepository.save(account);
            }
        }
        catch (TokenResponseException e)
        {
            System.out.println("Wystapil blad z API");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @RequestMapping("/login")
    public String youtubeLogin(HttpServletRequest request)
    {
        GoogleAuthorizationCodeRequestUrl authorizationUrl = this.googleAuthorizationCodeFlow.newAuthorizationUrl();
        authorizationUrl.setRedirectUri(HttpRequestUtils.getHostURL(request) + "/youtube/auth");
        return "redirect:" + authorizationUrl.build();
    }
}
