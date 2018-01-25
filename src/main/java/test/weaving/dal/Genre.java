package test.weaving.dal;

import java.util.HashSet;
import java.util.Set;
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
@Table(name = "genres")
@EqualsAndHashCode(of = {"name"})
@ToString(exclude = "books")
public class Genre {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;
  private String description;

  @Transient
  Set<Book> books;

  public void addBook(Book book) {
    if (book.getGenres() == null) {
      book.setGenres(new HashSet<>());
    }
    if (this.books == null) {
      books = new HashSet<>();
    }
    this.books.add(book);
    book.getGenres().add(this);
  }
}

