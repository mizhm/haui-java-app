package nhom8.dao;

import nhom8.model.BillDetail;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BillDetailDAO extends DAO<BillDetail> {
    @Override
    public ArrayList<BillDetail> getAll() throws SQLException {
        return null;
    }

    public ArrayList<BillDetail> getAll(int billId) throws SQLException {
        ArrayList<BillDetail> list = new ArrayList<BillDetail>();
        String sql = "{call usp_get_bill_detail_by_bill(?)}";
        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, billId);
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            BillDetail detail = new BillDetail();
            detail.setBillId(rs.getInt(1));
            detail.setProductId(rs.getInt(2));
            detail.setAmount(rs.getInt(3));
            detail.setPrice(rs.getFloat(4));
            detail.setProductName(rs.getString(5));
            list.add(detail);
        }
        return list;
    }

    @Override
    public BillDetail get(int id) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Object> create(BillDetail billDetail) throws SQLException {
        String sql = "{call usp_insert_bill_detail(?, ?, ?, ?, ?)}";
        Map<String, Object> output = new HashMap<>();

        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, billDetail.getBillId());
        cs.setInt(2, billDetail.getProductId());
        cs.setInt(3, billDetail.getAmount());
        cs.registerOutParameter(4, Types.BIT);
        cs.registerOutParameter(5, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(4));
        output.put("message", cs.getNString(5));
        return output;
    }

    @Override
    public Map<String, Object> update(BillDetail billDetail) throws SQLException {
        String sql = "{call usp_update_bill_detail(?, ?, ?, ?, ?)}";
        Map<String, Object> output = new HashMap<>();

        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, billDetail.getBillId());
        cs.setInt(2, billDetail.getProductId());
        cs.setInt(3, billDetail.getAmount());
        cs.registerOutParameter(4, Types.BIT);
        cs.registerOutParameter(5, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(4));
        output.put("message", cs.getNString(5));
        return output;
    }

    @Override
    public Map<String, Object> delete(int id) throws SQLException {
        return Map.of();
    }

    public Map<String, Object> delete(int billId, int productId) throws SQLException {
        Map<String, Object> output = new HashMap<>();
        String sql = "{call usp_delete_bill_detail(?,?, ?, ?)}";

        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, billId);
        cs.setInt(2, productId);
        cs.registerOutParameter(3, Types.BIT);
        cs.registerOutParameter(4, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(3));
        output.put("message", cs.getNString(4));
        return output;
    }
}
