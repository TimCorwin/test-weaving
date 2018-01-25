package test.weaving.dal;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
@ToString(exclude = "genres")
@EqualsAndHashCode(of = {"title"})
public class Book {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private  String title;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "author")
  private Author author;

  private String description;

  @Transient
  Set<Genre> genres = new HashSet<>();

  public void addAuthor(Author author) {
    if (author.getBooks() == null){
      author.setBooks(new HashSet<>());
    }
    author.getBooks().add(this);
    this.author = author;
  }







}
