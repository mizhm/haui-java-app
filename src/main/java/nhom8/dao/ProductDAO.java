package nhom8.dao;

import nhom8.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class ProductDAO extends DAO<Product> {
    @Override
    public ArrayList<Product> getAll() throws SQLException {
        return null;
    }

    @Override
    public Product get(int id) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Object> create(Product product) throws SQLException {
        return Map.of();
    }

    @Override
    public Map<String, Object> update(Product product) throws SQLException {
        return Map.of();
    }

    @Override
    public Map<String, Object> delete(int id) throws SQLException {
        return Map.of();
    }
}
