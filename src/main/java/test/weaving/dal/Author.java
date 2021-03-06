package test.weaving.dal;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authors")
@EqualsAndHashCode(of = {"firstname", "lastname"})
@ToString(exclude = "books")
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String firstname;
  private String lastname;

  @Transient
  Set<Book> books;

  public void addBook(Book book) {
    if (books == null) {
      books = new HashSet<>();
    }
    books.add(book);
    book.setAuthor(this);
  }

  public void linkBooks(Iterable<Book> books) {
    Set<Book> myBooks = StreamSupport.stream(books.spliterator(), false)
        .filter(b -> b.getAuthor().equals(this))
        .collect(Collectors.toSet());
    this.books = myBooks;
    ;

  }


}
