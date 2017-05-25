package pl.edu.tirex.blazingsquad.web.security.crypts;

import pl.edu.tirex.blazingsquad.web.security.Cryptography;
import pl.edu.tirex.blazingsquad.web.security.CryptographyMethod;
import pl.edu.tirex.blazingsquad.web.security.HashException;
import pl.edu.tirex.blazingsquad.web.security.HashedPassword;
import pl.edu.tirex.blazingsquad.web.utils.HashUtils;

import java.security.NoSuchAlgorithmException;

@CryptographyMethod("MD5")
public class Md5Cryptography implements Cryptography
{
    @Override
    public boolean matchPassword(HashedPassword hashedPassword, String password) throws HashException
    {
        return this.hashPassword(password).equals(hashedPassword.getHash());
    }

    @Override
    public String hashPassword(String password) throws HashException
    {
        try
        {
            return HashUtils.md5(password);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new HashException("Unexcepted error with MD5 hash", e);
        }
    }
}