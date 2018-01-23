package test.weaving.dal;

import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookRepoTest {

  @Autowired
  BookRepo bookRepo;
  @Autowired
  AuthorRepo authorRepo;

  @Before
  public void setUp() throws Exception {
  }


  @Test
  public void book() {
    Author author = new Author();
    author.setFirstname("Jane");
    author.setLastname("Smith");
    Book book = new Book(null, "How To Code", author);
    bookRepo.save(book);
    Iterable<Book> f = bookRepo.findAll();
    Set<Book> e = bookRepo.getAllEagerly();

    Set<Author> a = authorRepo.getAllEagerly();
    Set<Author> a2 = authorRepo.getAllBy();
    for (Author auth : a) {
      Set<Book> b = auth.getBooks();
    }
  }


}