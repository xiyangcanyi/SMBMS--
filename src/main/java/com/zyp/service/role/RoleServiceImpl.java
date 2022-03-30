package com.zyp.service.role;

import com.zyp.dao.BaseDao;
import com.zyp.dao.role.RoleDao;
import com.zyp.dao.role.RoleDaoImpl;
import com.zyp.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    //引入Dao
    private RoleDao roleDao;
    public RoleServiceImpl(){
        roleDao=new RoleDaoImpl();
    }
    @Override
    public List<Role> getRoleList() {
        Connection connection = null;
        List<Role> roleList = null;
        try {
            connection = BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection, null, null);
        }
        return roleList;
    }
    @Test
    public void test(){
        RoleServiceImpl roleService=new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();

            for (Role role : roleList) {
                System.out.println(role.getRoleName());
            }


    }
}
