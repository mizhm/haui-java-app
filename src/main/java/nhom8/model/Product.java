package nhom8.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private Integer id;
    private Integer categoryId;
    private String name;
    private Float price;
    private Boolean status;
    private String createdAt;
    private String updatedAt;

    //related to Category model
    private String categoryName;

    //for search purpose
    private Float fromPrice;
    private Float toPrice;

}
