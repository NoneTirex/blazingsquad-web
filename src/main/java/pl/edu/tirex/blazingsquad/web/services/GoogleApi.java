package pl.edu.tirex.blazingsquad.web.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import pl.edu.tirex.blazingsquad.web.configuration.ApplicationConfiguration;

import java.io.IOException;

@Service
public class GoogleApi
{
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private final ApplicationConfiguration configuration;
    private final GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow;

    @Autowired
    public GoogleApi(ApplicationConfiguration configuration, GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow)
    {
        this.configuration = configuration;
        this.googleAuthorizationCodeFlow = googleAuthorizationCodeFlow;
    }

    public GoogleAuthorizationCodeFlow getGoogleAuthorization()
    {
        return googleAuthorizationCodeFlow;
    }

    @Bean
    public GoogleAuthorizationCodeFlow getGoogleAuthorizationCodeFlow()
    {
        GoogleAuthorizationCodeFlow.Builder builder = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, this.configuration.getGoogle().getClientId(), this.configuration.getGoogle().getClientSecret(), this.configuration.getGoogle().getScopes());
        builder.setAccessType("offline");
        builder.setApprovalPrompt("auto");
        return builder.build();
    }

    @Bean
    public YouTube getYoutube()
    {
        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, null).setApplicationName("blazingsquad-web").build();
    }

    public YouTube getYoutube(Credential credential)
    {
        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("blazingsquad-web").build();
    }

    public boolean revokeGoogleApi(String token)
    {
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
        GenericUrl url = new GenericUrl("https://accounts.google.com/o/oauth2/revoke");
        url.set("token", token);
        try
        {
            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse response = request.execute();
            System.out.println(response.parseAsString());
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
