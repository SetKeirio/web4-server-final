package repositories;

import java.sql.SQLException;
import java.util.List;

public interface RepositoryInterface<T> {
    
    default void save(T repository){
        throw new UnsupportedOperationException();
    }

    default void update(T repository){
        throw new UnsupportedOperationException();
    }

    default List<T> get(){
        throw new UnsupportedOperationException();
    }

    default List<T> find(String property, Object object){
        throw new UnsupportedOperationException();
    }

    default void clear(){
        throw new UnsupportedOperationException();
    }

    default T findOne(Object object){
        throw new UnsupportedOperationException();
    }
}
