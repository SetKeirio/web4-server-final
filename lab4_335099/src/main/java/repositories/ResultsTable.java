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
public class ResultsTable implements RepositoryInterface<Result>{
    @Override
    public void save(Result repository) {
        Transfer.initialize(m -> m.persist(repository));
    }

    @Override
    public void clear() {
        String q = Data.DELETE_FROM + Data.RESULTS_TABLE;
        Transfer.initialize(m -> m.createQuery(q).executeUpdate());
    }

    @Override
    public List<Result> get() {
        String q = Data.SELECT + Data.RESULT + Data.FROM + Data.RESULTS_TABLE + Data.RESULT;
        List<Result> answer = Transfer.callbackInitialize(m -> m.createQuery(q, Result.class).getResultList());
        for (Result r: answer){
            r.setInarea(Transformation.checkInArea(r.getX(), r.getY(), r.getR()));
        }
        return answer;
    }
}
