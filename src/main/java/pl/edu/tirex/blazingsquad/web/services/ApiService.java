package pl.edu.tirex.blazingsquad.web.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.sparkpost.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import pl.edu.tirex.blazingsquad.web.configuration.ApplicationConfiguration;

@Service
public class ApiService
{
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private final ApplicationConfiguration configuration;

    @Autowired
    public ApiService(ApplicationConfiguration configuration)
    {
        this.configuration = configuration;
    }

    @Bean
    public YouTube getYoutube()
    {
        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, null).setApplicationName("blazingsquad-web").build();
    }

    @Bean
    public Client getSparkPostClient()
    {
        Client client = new Client(this.configuration.getSparkPost().getKey());
        client.setFromEmail(this.configuration.getEmail());
        return client;
    }

    @Bean
    public GoogleAuthorizationCodeFlow getGoogleAuthorizationCodeFlow()
    {
        GoogleAuthorizationCodeFlow.Builder builder = new GoogleAuthorizationCodeFlow.Builder(ApiService.HTTP_TRANSPORT, ApiService.JSON_FACTORY, this.configuration.getGoogle().getClientId(), this.configuration.getGoogle().getClientSecret(), this.configuration.getGoogle().getScopes());
        builder.setAccessType("offline");
        builder.setApprovalPrompt("auto");
        return builder.build();
    }
}