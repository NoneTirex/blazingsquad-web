package pl.edu.tirex.blazingsquad.web.security;

public class CryptographyService
{
    private final Cryptography defaultCryptographyMethod;

    public CryptographyService(Cryptography defaultCryptographyMethod)
    {
        this.defaultCryptographyMethod = defaultCryptographyMethod;
    }

    public HashedPassword hashPassword(String password, String salt) throws HashException
    {
        if (this.defaultCryptographyMethod == null)
        {
            return null;
        }
        String cryptographyName = CryptographyMethods.getCryptographyName(this.defaultCryptographyMethod);
        String hash = this.defaultCryptographyMethod.hashPassword(password);
        return new HashedPassword(cryptographyName, hash);
    }

    public boolean comparePassword(HashedPassword hashedPassword, String password) throws HashException
    {
        Cryptography cryptography = CryptographyMethods.getCryptography(hashedPassword.getMethod());
        return cryptography != null && cryptography.matchPassword(hashedPassword, password);
    }
}
