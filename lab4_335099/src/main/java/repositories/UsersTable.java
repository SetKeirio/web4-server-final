package repositories;

import entities.Result;
import entities.User;
import util.Transfer;
import util.Transformation;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Singleton
@Startup
@LocalBean
public class UsersTable implements RepositoryInterface<User>{

    @Override
    public void update(User repository) {
        Transfer.initialize(m -> m.merge(repository));
    }

    @Override
    public void save(User repository) {
        Transfer.initialize(m -> m.persist(repository));
    }

    @Override
    public List<User> get() {
        String q = Data.SELECT + Data.RESULT + Data.FROM + Data.USER_TABLE + Data.RESULT;
        List<User> answer = Transfer.callbackInitialize(m -> m.createQuery(q, User.class).getResultList());
        return answer;
    }

    @Override
    public List<User> find(String where, Object obj) {
        return Transfer.callbackInitialize(m -> {
            CriteriaBuilder builder = m.getCriteriaBuilder();
            CriteriaQuery<User> cq = builder.createQuery(User.class);
            Root<User> user = cq.from(User.class);
            Predicate predicate = builder.equal(user.get(where), obj);
            cq.where(predicate);
            TypedQuery<User> q = m.createQuery(cq);
            return q.getResultList();
        });
    }

    @Override
    public User findOne(Object object) {
        String username = (String) object;
        List<User> users = get();
        User answer = null;
        if (users != null) {
            for (User u : users) {
                if (u.getUsername().equals(username)) {
                    answer = u;
                }
            }
        }
        return answer;
    }
}
