package pl.edu.tirex.blazingsquad.web.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties
public class ApplicationConfiguration
{
    private Youtube youtube = new Youtube();
    private Google google = new Google();
    private SparkPost sparkPost = new SparkPost();
    private String email;
    private String hash = "MD5";

    public Youtube getYoutube()
    {
        return youtube;
    }

    public Google getGoogle()
    {
        return google;
    }

    public SparkPost getSparkPost()
    {
        return sparkPost;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getHash()
    {
        return hash;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public static class Google
    {
        private String clientId;
        private String clientSecret;
        private List<String> scopes;

        public String getClientId()
        {
            return clientId;
        }

        public void setClientId(String clientId)
        {
            this.clientId = clientId;
        }

        public String getClientSecret()
        {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret)
        {
            this.clientSecret = clientSecret;
        }

        public List<String> getScopes()
        {
            return scopes;
        }

        public void setScopes(List<String> scopes)
        {
            this.scopes = scopes;
        }
    }

    public static class Youtube
    {
        private String key;

        public String getKey()
        {
            return key;
        }

        public void setKey(String key)
        {
            this.key = key;
        }
    }

    public static class SparkPost
    {
        private String key;

        public String getKey()
        {
            return key;
        }

        public void setKey(String key)
        {
            this.key = key;
        }
    }
}
