package nhom8.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {
    private Integer id;
    private String name;
    private Boolean status;
    private String createdAt;
    private String updatedAt;

    @Override
    public String toString(){
        return this.name;
    }
}
