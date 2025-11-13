package cassandra.controller;

import cassandra.dto.ExampleTableDTO;
import cassandra.service.ExampleTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@Tag(name = "Example Table API", description = "Operations for managing example table data")
public class ExampleTableController {

    static final String TEXT_FIELD_1_ENDPOINT = "/api/text_field_1/";

    private ExampleTableService ExampleTableService;

    @Autowired
    public ExampleTableController(ExampleTableService ExampleTableService) {
        this.ExampleTableService = ExampleTableService;
    }

    @Operation(summary = "Get records by text field 1", 
               description = "Retrieve all records that match the specified text_field_1 value")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
        @ApiResponse(responseCode = "404", description = "No records found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = TEXT_FIELD_1_ENDPOINT + "{textField1}")
    public ResponseEntity<Iterable<ExampleTableDTO>> returnExampleTablesForTextField1(
            @Parameter(description = "Text field 1 value to search for", required = true, example = "text_field_1")
            @PathVariable String textField1) {
        Collection<ExampleTableDTO> ExampleTableDTOs = this.ExampleTableService.findByTextField1(textField1);
        return new ResponseEntity<>(ExampleTableDTOs, HttpStatus.OK);
    }

}
