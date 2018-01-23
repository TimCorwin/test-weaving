package test.weaving.dal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private  String title;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "author")
  private Author author;


}
