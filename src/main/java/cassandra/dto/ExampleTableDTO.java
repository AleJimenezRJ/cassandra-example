package cassandra.dto;

import cassandra.entity.ExampleTable;
import com.univocity.parsers.annotations.Parsed;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(description = "Example Table Data Transfer Object")
public class ExampleTableDTO {

    @ApiModelProperty(notes = "Primary key text field 1", example = "text_field_1", required = true)
    private String textField1;
    
    @Parsed(index = 0)
    @ApiModelProperty(notes = "Clustering key text field 2", example = "text_field_2", required = true)
    private String textField2;
    
    @Parsed(index = 1)
    @ApiModelProperty(notes = "Clustering key integer field 1", example = "1", required = true)
    private int intField1;
    
    @Parsed(index = 2)
    @ApiModelProperty(notes = "Clustering key integer field 2", example = "2", required = true)
    private int intField2;

    public ExampleTableDTO() {
    }

    public ExampleTableDTO(String textField1,
                           String textField2,
                           int intField1,
                           int intField2) {
        this.textField1 = textField1;
        this.textField2 = textField2;
        this.intField1 = intField1;
        this.intField2 = intField2;
    }

    public static ExampleTableDTO instanceOf(ExampleTable ExampleTable) {
        return new ExampleTableDTO(
                ExampleTable.getExampleTablePrimaryKey().getTextField1(),
                ExampleTable.getExampleTablePrimaryKey().getTextField2(),
                ExampleTable.getExampleTablePrimaryKey().getIntField1(),
                ExampleTable.getExampleTablePrimaryKey().getIntField2()
        );
    }

    public String getTextField1() {
        return textField1;
    }

    public void setTextField1(String textField1) {
        this.textField1 = textField1;
    }

    public String getTextField2() {
        return textField2;
    }

    public void setTextField2(String textField2) {
        this.textField2 = textField2;
    }

    public int getIntField1() {
        return intField1;
    }

    public void setIntField1(int intField1) {
        this.intField1 = intField1;
    }

    public int getIntField2() {
        return intField2;
    }

    public void setIntField2(int intField2) {
        this.intField2 = intField2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExampleTableDTO that = (ExampleTableDTO) o;
        return intField1 == that.intField1 &&
                intField2 == that.intField2 &&
                Objects.equals(textField1, that.textField1) &&
                Objects.equals(textField2, that.textField2);
    }

    @Override
    public int hashCode() {

        return Objects.hash(textField1, textField2, intField1, intField2);
    }

    @Override
    public String toString() {
        return "ExampleTableDTO{" +
                "textField1='" + textField1 + '\'' +
                ", textField2='" + textField2 + '\'' +
                ", intField1=" + intField1 +
                ", intField2=" + intField2 +
                '}';
    }
}
