package pl.edu.tirex.blazingsquad.web.utils;

import pl.edu.tirex.blazingsquad.web.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class HttpRequestUtils
{
    public static String getHostURL(HttpServletRequest request)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme()).append("://");
        sb.append(request.getServerName());
        if (request.getServerPort() != 80)
        {
            sb.append(':').append(request.getServerPort());
        }
        return sb.toString();
    }

    public static Account getAccountFromSession(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
        if (session == null)
        {
            return null;
        }
        Object accountObject = session.getAttribute("account");
        if (!(accountObject instanceof Account))
        {
            return null;
        }
        return (Account) accountObject;
    }
}
