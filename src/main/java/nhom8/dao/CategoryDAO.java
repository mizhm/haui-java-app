package nhom8.dao;

import nhom8.model.Category;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryDAO extends DAO<Category> {

    @Override
    public ArrayList<Category> getAll() throws SQLException {
        ArrayList<Category> list = new ArrayList<>();
        String sql = "{call usp_get_all_categories(?, ?)";
        CallableStatement cs = conn.prepareCall(sql);
        cs.setNull(1, Types.NVARCHAR);
        cs.setNull(2, Types.BOOLEAN);
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            Category obj = new Category(rs.getInt("id"), rs.getNString("name"), rs.getBoolean("status"), rs.getString("created_at"), rs.getString("updated_at"));
            list.add(obj);
        }
        return list;
    }

    @Override
    public Category get(int id) throws SQLException {
        String sql = "{Call usp_get_category_by_id(?)}";
        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, id);
        ResultSet rs = cs.executeQuery();
        return new Category(rs.getInt("id"), rs.getNString("name"), rs.getBoolean("status"), rs.getString("created_at"), rs.getString("updated_at"));
    }

    @Override
    public Map<String, Object> create(Category category) throws SQLException {
        String sql = "{call usp_insert_category(?, ?, ?, ?)}";
        Map<String, Object> output = new HashMap<>();

        CallableStatement cs = conn.prepareCall(sql);
        cs.setNString(1, category.getName());
        cs.setBoolean(2, category.getStatus());
        cs.registerOutParameter(3, Types.BIT);
        cs.registerOutParameter(4, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(3));
        output.put("message", cs.getNString(4));
        return output;
    }

    @Override
    public Map<String, Object> update(Category category) throws SQLException {
        Map<String, Object> output = new HashMap<>();
        String sql = "{call usp_update_category(?,?,?,?,?)}";

        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, category.getId());
        cs.setNString(2, category.getName());
        cs.setBoolean(3, category.getStatus());
        cs.registerOutParameter(4, Types.BIT);
        cs.registerOutParameter(5, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(4));
        output.put("message", cs.getNString(5));
        return output;
    }

    @Override
    public Map<String, Object> delete(int id) throws SQLException {
        Map<String, Object> output = new HashMap<>();
        String sql = "{call usp_delete_category(?,?,?)}";

        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, id);
        cs.registerOutParameter(2, Types.BIT);
        cs.registerOutParameter(3, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(2));
        output.put("message", cs.getNString(3));
        return output;
    }
}
