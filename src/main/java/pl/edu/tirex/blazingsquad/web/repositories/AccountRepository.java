package pl.edu.tirex.blazingsquad.web.repositories;

import org.hibernate.jpa.criteria.CriteriaQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
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
        if (account.getId() == 0)
        {
            this.entityManager.persist(account);
        }
        else
        {
            this.entityManager.merge(account);
        }
        this.entityManager.flush();
    }

    public boolean alreadyInDatabase(Account account)
    {
        TypedQuery<Account> query = this.entityManager.createQuery("select account from Account account where account.name.name=:name or account.name.displayName=:display_name or account.email=:email", Account.class);
        query.setParameter("name", account.getName().getName());
        query.setParameter("display_name", account.getName().getDisplayName());
        query.setParameter("email", account.getEmail());

        List<Account> resultList = query.getResultList();
        return resultList.size() > 0 && resultList.get(0) != null;
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
