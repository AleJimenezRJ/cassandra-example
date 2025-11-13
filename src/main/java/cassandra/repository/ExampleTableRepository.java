package cassandra.repository;

import cassandra.entity.ExampleTable;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExampleTableRepository extends CrudRepository<ExampleTable, String> {

    @Query("Select * from example_table where text_field_1=?0")
    List<ExampleTable> findByTextField1(String textField1);

}

