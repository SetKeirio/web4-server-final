package util;


import lombok.experimental.UtilityClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;
import java.util.function.Function;

@UtilityClass
public class Transfer {
    private final EntityManagerFactory factory = Persistence.createEntityManagerFactory("lab4");

    public <T> T callbackInitialize(Function<EntityManager, T> change){
        EntityManager m = factory.createEntityManager();
        try{
            m.getTransaction().begin();
            T obj = change.apply(m);
            m.getTransaction().commit();
            m.close();
            return obj;
        }
        catch(Exception e){
            if (m.getTransaction().isActive()) {
                m.getTransaction().rollback();
            }
            m.close();
            System.out.println("Во время обращения к базе данных произошла ошибка");
            System.out.println(e);
        }
        return null;

    }

    public void initialize(Consumer<EntityManager> t){
        callbackInitialize(m -> {
            t.accept(m);
            return null;
        });

    }
}
