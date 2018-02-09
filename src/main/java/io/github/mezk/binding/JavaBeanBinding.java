package io.github.mezk.binding;

import java.util.UUID;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.JoinRowMapper;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;

import io.github.mezk.HikariCp;

public class JavaBeanBinding {
    public static void main(String[] args) {
        DataSource ds = HikariCp.dataSource();

        Flyway flyway = new Flyway();
        flyway.setDataSource(ds);
        flyway.migrate();

        Person p1 = new Person();
        p1.setPersonId(UUID.randomUUID());
        p1.setName("Andrei");
        p1.setAge(24);

        Document d1 = new Document();
        d1.setDocumentId(UUID.randomUUID());
        d1.setPersonId(p1.getPersonId());
        d1.setCode("DOC_CODE1244");
        p1.setDocument(d1);

        Jdbi jdbi = Jdbi.create(ds);
        jdbi.registerRowMapper(Person.class, BeanMapper.<Person>of(Person.class))
            .registerRowMapper(Document.class, BeanMapper.<Document>of(Document.class));

        insert(jdbi, p1);

        Person p2 = getWithDocument(jdbi, p1.getPersonId());
        System.out.println(p2);

        Person p3 = get(jdbi, p1.getPersonId());
        System.out.println(p3);
    }

    private static Person getWithDocument(Jdbi jdbi, UUID personId) {
        return jdbi.withHandle(h -> {
            return h.createQuery("SELECT persons.person_id, name, age, document_id, code "
                + "FROM persons "
                + "LEFT JOIN documents ON persons.person_id = documents.person_id "
                + "WHERE persons.person_id = :personId")
                .bind("personId", personId)
                .map(JoinRowMapper.forTypes(Person.class, Document.class))
                .stream()
                .map(p -> {
                    Person prs = p.get(Person.class);
                    prs.setDocument(p.get(Document.class));
                    return prs;
                })
                .findFirst()
                .orElse(null);
        });
    }

    private static Person get(Jdbi jdbi, UUID personId) {
        return jdbi.withHandle(h -> {
            return h.createQuery("SELECT persons.person_id, name, age FROM persons "
                + "WHERE persons.person_id = :personId")
                .bind("personId", personId)
                .mapToBean(Person.class)
                .findFirst()
                .orElse(null);
        });
    }

    private static void insert(Jdbi jdbi, Person person) {
        jdbi.useTransaction(h -> {
            h.createUpdate("INSERT INTO persons (person_id, name, age) "
                + "VALUES (:personId, :name, :age)")
                .bindBean(person)
                .execute();

            Document document = person.getDocument();
            if (document != null) {
                h.createUpdate("INSERT INTO documents (document_id, person_id, code) "
                    + "VALUES (:documentId, :personId, :code)")
                    .bindBean(document)
                    .execute();
            }
        });
    }
}
