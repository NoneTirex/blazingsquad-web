package pl.edu.tirex.blazingsquad.web.security;

public interface Cryptography
{
    boolean matchPassword(HashedPassword hashedPassword, String password) throws HashException;

    String hashPassword(String password) throws HashException;
}
