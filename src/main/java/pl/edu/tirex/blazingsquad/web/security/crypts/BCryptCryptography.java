package pl.edu.tirex.blazingsquad.web.security.crypts;

import pl.edu.tirex.blazingsquad.web.security.Cryptography;
import pl.edu.tirex.blazingsquad.web.security.CryptographyMethod;
import pl.edu.tirex.blazingsquad.web.security.HashException;
import pl.edu.tirex.blazingsquad.web.security.HashedPassword;

@CryptographyMethod("BCRYPT")
public class BCryptCryptography implements Cryptography
{
    @Override
    public boolean matchPassword(HashedPassword hashedPassword, String password) throws HashException
    {
        return BCrypt.checkpw(password, hashedPassword.getHash());
    }

    @Override
    public String hashPassword(String password) throws HashException
    {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
