package pl.edu.tirex.blazingsquad.web;

import com.sparkpost.Client;
import com.sparkpost.exception.SparkPostException;
import com.sparkpost.model.AddressAttributes;
import com.sparkpost.model.OptionsAttributes;
import com.sparkpost.model.RecipientAttributes;
import com.sparkpost.model.TemplateContentAttributes;
import com.sparkpost.model.TransmissionWithRecipientArray;
import com.sparkpost.model.responses.Response;
import com.sparkpost.resources.ResourceTransmissions;
import com.sparkpost.transport.IRestConnection;
import com.sparkpost.transport.RestConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.tirex.blazingsquad.web.security.CryptographyService;
import pl.edu.tirex.blazingsquad.web.security.HashException;
import pl.edu.tirex.blazingsquad.web.security.HashedPassword;
import pl.edu.tirex.blazingsquad.web.forms.AccountRegisterForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountManager
{
    private final CryptographyService cryptographyService;
    private final Client sparkPostClient;

    @Autowired
    public AccountManager(CryptographyService cryptographyService, Client sparkPostClient)
    {
        System.out.println(cryptographyService);
        this.cryptographyService = cryptographyService;
        this.sparkPostClient = sparkPostClient;
    }

    public void sendConfirmRegister(Account account, String baseUrl) throws SparkPostException
    {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("base_url", baseUrl);
        parameters.put("email_token", account.getName().getName());
        this.sendMail(account.getEmail(), "Potwierdzenie rejestracji", "confirm-register", parameters);
    }

    public void sendMail(String email, String subject, String mailScheme, Map<String, String> parameters) throws SparkPostException
    {
        this.sendMail(Collections.singletonList(email), subject, mailScheme, parameters);
    }

    public void sendMail(List<String> recipients, String subject, String mailScheme, Map<String, String> parameters) throws SparkPostException
    {
        String content = readResource("mails/" + mailScheme + ".txt");
        String htmlContent = readResource("mails/" + mailScheme + ".html");
        if (content == null)
        {
            return;
        }
        for (Map.Entry<String, String> entry : parameters.entrySet())
        {
            content = content.replace("%{" + entry.getKey() + "}", entry.getValue());
            if (htmlContent != null)
            {
                htmlContent = htmlContent.replace("%{" + entry.getKey() + "}", entry.getValue());
            }
        }
        TransmissionWithRecipientArray transmission = new TransmissionWithRecipientArray();
        List<RecipientAttributes> recipientArray = new ArrayList<>();

        for (String recipient : recipients)
        {
            RecipientAttributes recipientAttribs = new RecipientAttributes();
            recipientAttribs.setAddress(new AddressAttributes(recipient));
            recipientArray.add(recipientAttribs);
        }

        transmission.setRecipientArray(recipientArray);
        TemplateContentAttributes contentAttributes = new TemplateContentAttributes();
        contentAttributes.setFrom(new AddressAttributes(this.sparkPostClient.getFromEmail()));
        contentAttributes.setSubject(subject);
        contentAttributes.setText(content);
        contentAttributes.setHtml(htmlContent);
        transmission.setContentAttributes(contentAttributes);
        OptionsAttributes options = new OptionsAttributes();
        if (this.sparkPostClient.getFromEmail().toLowerCase().contains("@sparkpostbox.com"))
        {
            options.setSandbox(Boolean.TRUE);
        }
        options.setOpenTracking(Boolean.FALSE);
        options.setClickTracking(Boolean.FALSE);
        transmission.setOptions(options);

        IRestConnection connection = new RestConnection(this.sparkPostClient);
        Response response = ResourceTransmissions.create(connection, 0, transmission);
    }

    public void addAccountToSession(HttpServletRequest request, Account account)
    {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(50 * 60); // 50 minute in seconds
        session.setAttribute("account", account);
    }

    public String readResource(String file)
    {
        InputStream input = AccountManager.class.getClassLoader().getResourceAsStream(file);
        if (input == null)
        {
            return null;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input)))
        {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean comparePassword(Account account, String password) throws HashException
    {
        return this.cryptographyService.comparePassword(account.getPassword(), password);
    }

    public Account createAccount(AccountRegisterForm accountRegisterForm) throws HashException
    {
        HashedPassword hashedPassword = this.cryptographyService.hashPassword(accountRegisterForm.getPassword(), accountRegisterForm.getUsername());
        return new Account(new Name(accountRegisterForm.getUsername(), null), hashedPassword, accountRegisterForm.getEmail());
    }
}
