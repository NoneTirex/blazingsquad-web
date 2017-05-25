package pl.edu.tirex.blazingsquad.web.controllers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.edu.tirex.blazingsquad.web.groups.Flag;
import pl.edu.tirex.blazingsquad.web.groups.GroupFlag;
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
    public String youtubeAuth(HttpServletRequest request, @RequestParam(value = "code", required = false) String code, @RequestParam(value = "error", required = false) String error)
    {
        GoogleAuthorizationCodeTokenRequest codeTokenRequest = this.googleAuthorizationCodeFlow.newTokenRequest(code);
        codeTokenRequest.setRedirectUri(HttpRequestUtils.getHostURL(request) + "/youtube/auth");
        try
        {
            GoogleTokenResponse tokenResponse = codeTokenRequest.execute();
            if (tokenResponse.getRefreshToken() == null)
            {
                System.out.println("Cos sie ewidentnie spierdolilo!");
            }
            Credential credential = new GoogleCredential();
            credential.setAccessToken(tokenResponse.getAccessToken());

            YouTube youTube = new YouTube.Builder(ApiService.HTTP_TRANSPORT, ApiService.JSON_FACTORY, credential).setApplicationName("blazingsquad-web").build();
            ChannelListResponse channelResponse = youTube.channels().list("snippet,contentDetails").setMine(true).execute();
            System.out.println(channelResponse.getItems());

            //            if (account != null && channelResponse.size() > 0)
            //            {
            //                Channel channel = channelResponse.getItems().get(0);
            //                YoutubeChannel youtubeChannel = new YoutubeChannel(channel.getId(), tokenResponse.getRefreshToken());
            //                youtubeChannel.setUploadsId(channel.getContentDetails().getRelatedPlaylists().getUploads());
            //                YoutubeChannelInformation youtubeChannelInformation = new YoutubeChannelInformation();
            //                youtubeChannelInformation.setUsername(channel.getSnippet().getTitle());
            //                youtubeChannelInformation.setDescription(channel.getSnippet().getDescription());
            //                youtubeChannel.setInformation(youtubeChannelInformation);
            //                account.setYoutubeChannel(youtubeChannel);
            //                this.youtubeChannelRepository.save(youtubeChannel);
            //                this.accountRepository.save(account);
            //            }
        }
        catch (TokenResponseException e)
        {
            System.out.println("Wystapil blad z API");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "index";
    }

    @RequestMapping("/login")
    public String youtubeLogin(HttpServletRequest request)
    {
        GoogleAuthorizationCodeRequestUrl authorizationUrl = this.googleAuthorizationCodeFlow.newAuthorizationUrl();
        authorizationUrl.setRedirectUri(HttpRequestUtils.getHostURL(request) + "/youtube/auth");
        return "redirect:" + authorizationUrl.build();
    }
}
