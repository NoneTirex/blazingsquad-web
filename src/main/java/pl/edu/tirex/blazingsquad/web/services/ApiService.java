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
    private final ApplicationConfiguration configuration;

    @Autowired
    public ApiService(ApplicationConfiguration configuration)
    {
        this.configuration = configuration;
    }

    @Bean
    public Client getSparkPostClient()
    {
        Client client = new Client(this.configuration.getSparkPost().getKey());
        client.setFromEmail(this.configuration.getEmail());
        return client;
    }
}