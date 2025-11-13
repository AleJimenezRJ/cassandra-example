package cassandra.controller;

import cassandra.dto.ExampleTableDTO;
import cassandra.service.ExampleTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@Api(value = "Example Table API", description = "Operations for managing example table data")
public class ExampleTableController {

    static final String TEXT_FIELD_1_ENDPOINT = "/api/text_field_1/";

    private ExampleTableService ExampleTableService;

    @Autowired
    public ExampleTableController(ExampleTableService ExampleTableService) {
        this.ExampleTableService = ExampleTableService;
    }

    @ApiOperation(value = "Get records by text field 1", 
                  response = ExampleTableDTO.class, 
                  responseContainer = "List",
                  notes = "Retrieve all records that match the specified text_field_1 value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved list"),
        @ApiResponse(code = 404, message = "No records found"),
        @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(value = TEXT_FIELD_1_ENDPOINT + "{textField1}")
    public ResponseEntity<Iterable<ExampleTableDTO>> returnExampleTablesForTextField1(
            @ApiParam(value = "Text field 1 value to search for", required = true, example = "text_field_1")
            @PathVariable String textField1) {
        Collection<ExampleTableDTO> ExampleTableDTOs = this.ExampleTableService.findByTextField1(textField1);
        return new ResponseEntity<>(ExampleTableDTOs, HttpStatus.OK);
    }

}
