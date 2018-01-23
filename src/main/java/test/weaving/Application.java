package test.weaving;

import java.util.Collections;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;
import test.weaving.dal.Book;
import test.weaving.dal.BookRepo;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = BookRepo.class)
@EntityScan(basePackageClasses = Book.class)
public class Application  extends JpaBaseConfiguration {
  private static final String persistenceUnit = "PersistenceUnit";


    /**
     * @param dataSource
     * @param properties
     * @param jtaTransactionManagerProvider
     */
    protected Application(DataSource dataSource, JpaProperties properties,
        ObjectProvider<JtaTransactionManager> jtaTransactionManagerProvider,
        ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
      super(dataSource, properties, jtaTransactionManagerProvider, transactionManagerCustomizers);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration#createJpaVendorAdapter()
     */
    @Override
    protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
      return new EclipseLinkJpaVendorAdapter();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration#getVendorProperties()
     */
    @Override
    protected Map<String, Object> getVendorProperties() {

      return Collections.singletonMap("eclipselink.weaving", "static");
    }

    public static void main(String[] args) {

      BookRepo repository = SpringApplication.run(Application.class, args).getBean(BookRepo.class);

      System.out.println(repository.findAll());
    }
}
