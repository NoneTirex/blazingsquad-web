package pl.edu.tirex.blazingsquad.web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pl.edu.tirex.blazingsquad.web.AccountMethodArgumentResolver;
import pl.edu.tirex.blazingsquad.web.security.Cryptography;
import pl.edu.tirex.blazingsquad.web.security.CryptographyMethods;
import pl.edu.tirex.blazingsquad.web.security.CryptographyService;
import pl.edu.tirex.blazingsquad.web.security.HashException;

import javax.servlet.SessionTrackingMode;
import java.util.Collections;
import java.util.List;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter
{
    private final ApplicationConfiguration configuration;
    private final AccountMethodArgumentResolver testHandler;

    @Autowired
    public WebConfiguration(ApplicationConfiguration configuration, AccountMethodArgumentResolver testHandler)
    {
        this.configuration = configuration;
        this.testHandler = testHandler;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
    {
        argumentResolvers.add(this.testHandler);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(this.testHandler);
        super.addInterceptors(registry);
    }

    @Bean
    public ServletContextInitializer configureServletContextInitializer()
    {
        return servletContext -> servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
    }

    @Bean
    public CryptographyService createCryptography() throws HashException
    {
        Cryptography cryptography = CryptographyMethods.getCryptography(this.configuration.getHash());
        if (cryptography == null)
        {
            return null;
        }
        return new CryptographyService(cryptography);
    }
}
