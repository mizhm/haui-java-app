package nhom8.dao;

import nhom8.model.BillDetail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class BillDetailDAO extends DAO<BillDetail> {
    @Override
    public ArrayList<BillDetail> getAll() throws SQLException {
        return null;
    }

    @Override
    public BillDetail get(int id) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Object> create(BillDetail billDetail) throws SQLException {
        return Map.of();
    }

    @Override
    public Map<String, Object> update(BillDetail billDetail) throws SQLException {
        return Map.of();
    }

    @Override
    public Map<String, Object> delete(int id) throws SQLException {
        return Map.of();
    }
}
