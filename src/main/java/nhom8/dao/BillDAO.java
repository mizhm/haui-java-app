package nhom8.dao;

import nhom8.model.Bill;
import nhom8.utils.Common;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BillDAO extends DAO<Bill> {
    @Override
    public ArrayList<Bill> getAll() throws SQLException {
        ArrayList<Bill> list = new ArrayList<Bill>();
        String sql = "{call usp_get_all_bill(?,?)}";
        CallableStatement cs = conn.prepareCall(sql);
        cs.setNull(1, Types.INTEGER);
        cs.setNull(2, Types.BOOLEAN);
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            Bill bill = new Bill();
            bill.setId(rs.getInt(1));
            bill.setStatus(rs.getBoolean(2));
            bill.setCreatedAt(rs.getString(3));
            bill.setUpdatedAt(rs.getString(4));
            bill.setTotal(rs.getFloat(5));
            list.add(bill);
        }
        return list;
    }

    @Override
    public Bill get(int id) throws SQLException {
        return null;
    }

    @Override
    public Map<String, Object> create(Bill bill) throws SQLException {
        String sql = "{call usp_insert_bill(?, ?, ?)}";
        Map<String, Object> output = new HashMap<>();

        CallableStatement cs = conn.prepareCall(sql);
        cs.setBoolean(1, bill.getStatus());
        cs.registerOutParameter(2, Types.BIT);
        cs.registerOutParameter(3, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(2));
        output.put("message", cs.getNString(3));
        return output;
    }

    @Override
    public Map<String, Object> update(Bill bill) throws SQLException {
        Map<String, Object> output = new HashMap<>();
        String sql = "{call usp_update_bill(?,?,?,?)}";

        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, bill.getId());
        cs.setBoolean(2, bill.getStatus());
        cs.registerOutParameter(3, Types.BIT);
        cs.registerOutParameter(4, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(3));
        output.put("message", cs.getNString(4));
        return output;
    }

    @Override
    public Map<String, Object> delete(int id) throws SQLException {
        Map<String, Object> output = new HashMap<>();
        String sql = "{call usp_delete_bill(?,?,?)}";

        CallableStatement cs = conn.prepareCall(sql);
        cs.setInt(1, id);
        cs.registerOutParameter(2, Types.BIT);
        cs.registerOutParameter(3, Types.NVARCHAR);
        cs.execute();

        output.put("status", cs.getBoolean(2));
        output.put("message", cs.getNString(3));
        return output;
    }

    public Bill getNewBill() throws SQLException {
        Bill bill = new Bill();
        String sql = "{call usp_get_new_bill()}";
        CallableStatement cs = conn.prepareCall(sql);
        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            bill.setId(rs.getInt(1));
            bill.setStatus(rs.getBoolean(2));
            bill.setCreatedAt(rs.getString(3));
            bill.setUpdatedAt(rs.getString(4));
        }
        return bill;
    }

    public ArrayList<Bill> getWithCondition(Bill bill) throws SQLException {
        ArrayList<Bill> list = new ArrayList<Bill>();
        String sql = "{call usp_get_all_bill(?,?,?)}";
        CallableStatement cs = conn.prepareCall(sql);
        if (!Common.isNullOrEmpty(bill.getId())) {
            cs.setInt(1, bill.getId());
        } else {
            cs.setNull(1, Types.INTEGER);
        }
        cs.setNull(2, Types.BOOLEAN);

        if (!Common.isNullOrEmpty(bill.getSearchDate())) {
            cs.setDate(3, bill.getSearchDate());
        } else {
            cs.setNull(3, Types.DATE);
        }

        ResultSet rs = cs.executeQuery();
        while (rs.next()) {
            Bill obj = new Bill();
            obj.setId(rs.getInt(1));
            obj.setStatus(rs.getBoolean(2));
            obj.setCreatedAt(rs.getString(3));
            obj.setUpdatedAt(rs.getString(4));
            obj.setTotal(rs.getFloat(5));
            list.add(obj);
        }
        return list;
    }
}
