package test.weaving.dal;

import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepo extends CrudRepository<Book, Integer> {


  @Query("select b from Book b JOIN b.author authors ")
  Set<Book> getAllEagerly();

  Book findFirstByTitle(String title);

}
