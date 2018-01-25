package test.weaving.dal;

import static java.util.stream.Collectors.groupingBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.After;
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

  @Autowired
  GenreRepo genreRepo;
  @Autowired
  MappingRepo mappingRepo;
  private Author jane;
  private Author joan;
  private Author authorWithoutBooks;
  private HashSet<Author> authors;
  private Book howToCode;
  private Book howToCook;
  private Book whyWeCook;
  private Set<Book> books;
  private Genre howto;
  private Genre cooking;
  private Genre compScience;
  private Set<Genre> genres;
  private BookGenreMapping map1;
  private BookGenreMapping map2;
  private BookGenreMapping map3;
  private BookGenreMapping map4;
  private BookGenreMapping map5;
  private Set<BookGenreMapping> maps;

  @Before
  public void setUp() throws Exception {
    jane = new Author(null, "Jane", "Smith", null);
    joan = new Author(null, "Joan", "Smith", null);
    authorWithoutBooks = new Author(null, "John", "Smith", null);

    authors = new HashSet<>();
    authors.add(jane);
    authors.add(joan);
    authors.add(authorWithoutBooks);

    howToCode = new Book(null, "How To Code", null, "", null);
    howToCode.addAuthor(jane);
    howToCook = new Book(null, "How To Cook", null, "", null);
    howToCook.addAuthor(joan);
    whyWeCook = new Book(null, "Why We Cook", null, "", null);
    whyWeCook.addAuthor(joan);

    books = new HashSet<>();
    books.add(howToCode);
    books.add(howToCook);
    books.add(whyWeCook);

    howto = new Genre(null, "howto", "self teaching", null);
    cooking = new Genre(null, "cooking", "", null);
    compScience = new Genre(null, "Computer Science", "10101", null);
    genres = new HashSet<>();
    genres.add(howto);
    genres.add(cooking);
    genres.add(compScience);

    map1 = new BookGenreMapping(null, howto, howToCode);

    map2 = new BookGenreMapping(null, compScience, howToCode);

    map3 = new BookGenreMapping(null, cooking, howToCook);

    map4 = new BookGenreMapping(null, howto, howToCook);
    map5 = new BookGenreMapping(null, cooking, whyWeCook);

    maps = new HashSet<>();
    maps.add(map1);
    maps.add(map2);
    maps.add(map3);
    maps.add(map4);
    maps.add(map5);

//    authorRepo.save(authors);
    genreRepo.save(genres);
    bookRepo.save(books);

    mappingRepo.save(maps);

  }

  @After
  public void teardown() throws Exception {
    //del
    mappingRepo.deleteAll();
    bookRepo.deleteAll();
    authorRepo.deleteAll();
    genreRepo.deleteAll();

  }







  @Test
  public void importAuthorWithBooks() {

    Book newBook = new Book(null, "new", authorWithoutBooks, "", null);
    bookRepo.save(newBook);

    Iterable<Book> b = bookRepo.findAll();
    Author result = authorRepo.findFirstByFirstname("John");
    result.linkBooks(b);

    assertThat(result.getBooks().size(), equalTo(1));
  }

  @Test
  public void updateAuthorWithoutModifyingBooks() {
    joan.setLastname("Carpenter");
    authorRepo.save(joan);

    Author resultAuthor = authorRepo.findFirstByFirstname("Joan");
    Book resultBook = bookRepo.findFirstByTitle(howToCook.getTitle());

    assertThat(resultAuthor.getLastname(), equalTo(joan.getLastname()));
    assertThat(resultBook, equalTo(howToCook));

  }


  @Test
  public void updateBooksWithoutModifyingAuthors() {
    howToCode.setDescription("Java is Complicated");
    bookRepo.save(howToCode);

    Author resultAuthor = authorRepo.findFirstByFirstname("Jane");
    Book resultBook = bookRepo.findFirstByTitle(howToCode.getTitle());

    assertThat(resultAuthor.getLastname(), equalTo(jane.getLastname()));
    assertThat(resultBook, equalTo(howToCode));
    assertThat(resultBook.getDescription(), equalTo(howToCode.getDescription()));


  }


  @Test
  public void moveBookToANewAuthor() {
    howToCode.setAuthor(joan);
    bookRepo.save(howToCode);

    Author oldA = authorRepo.findFirstByFirstname("Jane");
    Author newA = authorRepo.findFirstByFirstname("Joan");

    Book resultBook = bookRepo.findFirstByTitle(howToCode.getTitle());

    assertThat(resultBook, equalTo(howToCode));
    assertThat(oldA, equalTo(jane));
    assertThat(newA, equalTo(joan));


  }


  @Test
  public void readAuthorWithBooks() {
    Iterable<Book> b = bookRepo.findAll();
    Author result = authorRepo.findFirstByFirstname("Joan");
    result.linkBooks(b);

    assertThat(result.getBooks().size(), equalTo(2));
  }

  //would be on the domain service
  private Set<Genre> getGenresFromMaps(Set<BookGenreMapping> m2) {
    Map<Genre, List<BookGenreMapping>> groups = m2.stream()
        .collect(groupingBy(
            BookGenreMapping::getGenre)); // reorganize books under each Genre for once-through parsing

    Set<Genre> result = new HashSet<>();
    for (Entry<Genre, List<BookGenreMapping>> e : groups.entrySet()) {
      Genre k = e.getKey();
      e.getValue().stream().forEach(m -> {
        k.addBook(m.getBook());  //bidirectional add
      });
      result.add(k); //add filled genre to result Set
    }
    return result;
  }

  @Test
  public void getBooksForGenre() {
    maps = mappingRepo.findAllByGenre(howto);
    Set<Genre> result = getGenresFromMaps(maps);

    Set<Book> expected = new HashSet<>();
    expected.add(howToCode);
    expected.add(howToCook);
    result.forEach(r -> assertThat(r.getBooks(), equalTo(expected)));

  }

  @Test
  public void getGenresAndBooksForAuthor() {

    Set<BookGenreMapping> m2 = mappingRepo.findAllByBookAuthorFirstname(jane.getFirstname());

    Set<Genre> result = getGenresFromMaps(m2);

    Set<Genre> expected = new HashSet<>();
    expected.add(compScience);
    expected.add(howto);

    assertThat(result, equalTo(expected));

    for (Genre genre : result) {
      assertThat(genre.books.size(), greaterThan(0));
      for (Book b : genre.books) {
        assertThat(b.getAuthor(), is(notNullValue()));
      }
    }

  }


  @Test
  public void mapGenreAndBook() {
    BookGenreMapping given = new BookGenreMapping(null, compScience, whyWeCook);
    mappingRepo.save(given);

    maps = mappingRepo.findAllByGenre(compScience);
    Set<Genre> result = getGenresFromMaps(maps);

    Set<Book> expected = new HashSet<>();
    expected.add(howToCode);
    expected.add(whyWeCook);
    result.forEach(r -> assertThat(r.getBooks(), equalTo(expected)));
  }

  @Test
  public void unMapBookFromGenre() {
    mappingRepo.delete(map1);

    maps = mappingRepo.findAllByGenre(howto);
    Set<Genre> result = getGenresFromMaps(maps);

    Set<Book> expected = new HashSet<>();
    expected.add(howToCook);
    result.forEach(r -> assertThat(r.getBooks(), equalTo(expected)));
  }

  @Test
  public void deleteBookWithoutImpactingGenre() {
    mappingRepo.deleteAllByBook(whyWeCook);
    bookRepo.delete(whyWeCook);
    Set<Genre> result = getGenresFromMaps(mappingRepo.findAllByGenre(cooking));

    Set<Book> expected = new HashSet<>();
    expected.add(howToCook);
    result.forEach(r -> assertThat(r.getBooks(), equalTo(expected)));

  }

  @Test
  public void changeGenreForBook() {
    BookGenreMapping newMap = new BookGenreMapping(null, compScience, whyWeCook);
    mappingRepo.delete(map5);
    mappingRepo.save(newMap);
    Set<BookGenreMapping> result = mappingRepo
        .findAllByBookAuthorFirstname(whyWeCook.getAuthor().getFirstname());

    Book resultBook = bookRepo.findFirstByTitle(whyWeCook.getTitle());

    assertThat(result.contains(newMap), equalTo(true));
    assertThat(result.contains(map5), equalTo(false));
    assertThat(resultBook, equalTo(whyWeCook));


  }


}