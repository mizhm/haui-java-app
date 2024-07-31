package nhom8.dao;

import nhom8.model.Bill;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class BillDAO extends DAO<Bill> {
    @Override
    public ArrayList<Bill> getAll() throws SQLException {
        return null;
    }

    @Override
    public Bill get(int id) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Object> create(Bill bill) throws SQLException {
        return Map.of();
    }

    @Override
    public Map<String, Object> update(Bill bill) throws SQLException {
        return Map.of();
    }

    @Override
    public Map<String, Object> delete(int id) throws SQLException {
        return Map.of();
    }
}
