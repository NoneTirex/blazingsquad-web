package pl.edu.tirex.blazingsquad.web.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.tirex.blazingsquad.web.Account;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class AccountRepository
{
    private final EntityManager entityManager;

    @Autowired
    public AccountRepository(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public void save(Account account)
    {
        this.entityManager.merge(account);
    }

    public Account findByName(String name)
    {
        TypedQuery<Account> query = this.entityManager.createQuery("select account from Account account left join account.youtubeChannel youtubeChannel where account.name.name=:name", Account.class);
        query.setParameter("name", name);
        List<Account> resultList = query.getResultList();
        if (resultList.size() > 0)
        {
            return resultList.get(0);
        }
        return null;
    }

    public Account findByEmail(String email)
    {
        TypedQuery<Account> query = this.entityManager.createQuery("select account from Account account left join account.youtubeChannel youtubeChannel where account.email=:email", Account.class);
        query.setParameter("email", email);
        List<Account> resultList = query.getResultList();
        if (resultList.size() > 0)
        {
            return resultList.get(0);
        }
        return null;
    }
}
