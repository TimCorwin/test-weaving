package test.weaving.dal;

import java.util.Set;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepo extends CrudRepository<Author,Integer> {

  @Query("select a from Author a JOIN a.books books ")
  Set<Author> getAllEagerly();

  @EntityGraph("books")
  Set<Author> getAllBy();

}
