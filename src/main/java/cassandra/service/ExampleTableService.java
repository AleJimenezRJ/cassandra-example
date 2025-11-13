package cassandra.service;

import cassandra.dto.ExampleTableDTO;

import java.util.Collection;

public interface ExampleTableService {

    Collection<ExampleTableDTO> findByTextField1(String textField1);

}
