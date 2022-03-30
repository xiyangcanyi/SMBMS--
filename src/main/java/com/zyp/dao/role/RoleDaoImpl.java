package com.zyp.dao.role;

import com.zyp.dao.BaseDao;
import com.zyp.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{
    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {
        PreparedStatement pstm=null;
        ResultSet rs=null;
        ArrayList<Role> roleList = new ArrayList<Role>();
        if(connection!=null){
            String sql="select  * from smbms_role;";
            Object[] params={};
            try {
                rs= BaseDao.execute(connection,pstm,rs,sql,params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (rs.next()){
                Role _role=new Role();
                _role.setId(rs.getInt("id"));
                _role.setRoleName(rs.getString("roleName"));
                _role.setRoleCode( rs.getString("roleCode"));
                roleList.add(_role);

            }
            BaseDao.closeResource(null,pstm,rs);

        }
        System.out.println("1"+roleList);
        return roleList;
    }
}
