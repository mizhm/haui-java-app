package nhom8.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDetail implements Serializable {
    private Integer billId;
    private Integer productId;
    private Integer amount;

    //related to Product model
    private String productName;
    private String productPrice;
}
