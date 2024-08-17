package nhom8.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill implements Serializable {
    private Integer id;
    private Boolean status;
    private String createdAt;
    private String updatedAt;
    private Integer userId;

    //related to bill detail
    private Float total;

    //search field
    private Date searchDate;
}
