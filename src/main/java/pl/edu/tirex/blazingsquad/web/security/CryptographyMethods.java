package pl.edu.tirex.blazingsquad.web.security;

import pl.edu.tirex.blazingsquad.web.security.crypts.BCryptCryptography;
import pl.edu.tirex.blazingsquad.web.security.crypts.Md5Cryptography;

import java.util.HashMap;
import java.util.Map;

public class CryptographyMethods
{
    private static final Map<String, Cryptography> cryptographyByNameMap = new HashMap<>();
    private static final Map<Class<? extends Cryptography>, String> cryptographyMap = new HashMap<>();

    public static Cryptography getCryptography(String name)
    {
        return cryptographyByNameMap.get(name.toLowerCase());
    }

    public static String getCryptographyName(Cryptography cryptography)
    {
        return getCryptographyName(cryptography.getClass());
    }

    public static String getCryptographyName(Class<? extends Cryptography> cryptographyClass)
    {
        return cryptographyMap.get(cryptographyClass);
    }

    public static <E extends Cryptography> E registerCryptography(Class<E> cryptographyClass)
    {
        CryptographyMethod cryptographyAnnotation = cryptographyClass.getAnnotation(CryptographyMethod.class);
        if (cryptographyAnnotation == null)
        {
            return null;
        }
        String method = cryptographyAnnotation.value();
        if (method.isEmpty())
        {
            throw new RuntimeException("Method of `" + cryptographyClass + "` can not be null!");
        }
        Cryptography cryptographyMethod = cryptographyByNameMap.get(method.toLowerCase());
        if (cryptographyMethod != null)
        {
            throw new RuntimeException("Already have cryptography method with name `" + method + " [" + cryptographyMethod.getClass() + "]");
        }
        try
        {
            E cryptography = cryptographyClass.newInstance();
            cryptographyByNameMap.put(method.toLowerCase(), cryptography);
            cryptographyMap.put(cryptographyClass, method);
            return cryptography;
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    static
    {
        registerCryptography(Md5Cryptography.class);
        registerCryptography(BCryptCryptography.class);
    }
}
