package test.weaving.dal;

import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

public interface MappingRepo extends CrudRepository<BookGenreMapping, Integer>{

  Set<BookGenreMapping> findAllByGenre(Genre genre);

  Set<BookGenreMapping> findAllByBookAuthorFirstname(String firstName);

@Transactional
  void deleteAllByBook(Book b);
@Transactional
void deleteAllByGenre(Genre g);



}
