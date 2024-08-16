package nhom8.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HomeDAO {
    public Map<String, Object> getStatistic() {
        try {
            Map<String, Object> list = new HashMap<String, Object>();
            list.put("category", new CategoryDAO().getAll().size());
            list.put("product", new ProductDAO().getAll().size());
            list.put("bill", new BillDAO().getAll().size());
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
