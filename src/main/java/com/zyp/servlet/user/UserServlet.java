package com.zyp.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.zyp.pojo.Role;
import com.zyp.pojo.User;
import com.zyp.service.UserService;
import com.zyp.service.UserServiceImpl;
import com.zyp.service.role.RoleServiceImpl;
import com.zyp.utils.Constants;
import com.zyp.utils.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO 自动生成的方法存根
        String method = req.getParameter("method");
        if (method.equals("savepwd") && method != null) {
            try {
                this.updatePwd(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(method.equals("pwdmodify")&&method!=null){
            this.pwdModify(req,resp);
        }else if(method.equals("query")&& method != null){
            this.query(req, resp);
        }
        //实现复用~~~~~~
        // 想添加新的增删改查，直接用if(method != "savepwd" && method != null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    public void updatePwd(HttpServletRequest req,HttpServletResponse resp) throws Exception {
        // 通过session获得用户id
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
//        System.out.println("servlet1: "+newpassword);
//        System.out.println(o);
        boolean flag = false;
        if (o != null && newpassword != null) {
            UserServiceImpl userService = new UserServiceImpl();

            try {
                flag = userService.updatePwd(((User) o).getId(), newpassword);
                System.out.println("servlet2: "+newpassword);
            } catch (SQLException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            } catch (Exception e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
            if (flag) {
                req.setAttribute("message", "密码修改成功，请退出，使用新密码登录");
                // 密码修改成功,移除session(移除后不能再次修改密码,建议不移除)
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                // 密码修改失败
                req.setAttribute("message", "密码修改失败");
            }

        } else {
            // 密码修改有问题
            req.setAttribute("message", "新密码有问题");
        }
        try {
            req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req, resp);
        } catch (ServletException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }


    }
    public void pwdModify(HttpServletRequest req,HttpServletResponse resp){
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        Map<String, String> resultMap = new HashMap<String, String>();
        if(o==null) {//session失效，session过期了
            resultMap.put("result","seesionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){//输入密码为空
            resultMap.put("result","error");
        }else {//
            String userPassword = ((User)o).getUserPassword();//seesion中的用户密码
            if(oldpassword.equals(userPassword)) {
                resultMap.put("result","true");
            }else {
                resultMap.put("result","false");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter writer = null;
            writer = resp.getWriter();
            writer.write(JSONArray.class.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
    public void query(HttpServletRequest req,HttpServletResponse resp){
        //查询用户列表
        String queryUserName= req.getParameter("queryUserName");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole=0;
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList=null;
        //第一此请求肯定是走第一页，页面大小固定的
        //设置页面容量
        int pageSize = 5;//把它设置在配置文件里,后面方便修改
        //当前页码
        int currentPageNo = 1;
        if(queryUserName==null){
            queryUserName="";

        }
        if(temp!=null&&!temp.equals("")){
            queryUserRole= Integer.parseInt(temp);
        }
        if(pageIndex!=null){
            currentPageNo= Integer.parseInt(pageIndex);
        }
        //获取用户的总数(分页还有上一页和下一页)
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        PageSupport pageSupport=new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        int totalPageCount =(int)(totalCount+pageSize-1)/pageSize;
        //控制首页和尾页，如果页面小于1，就显示第一页
        if(totalCount<1){
            currentPageNo=1;
        }else if(currentPageNo>totalPageCount){
            currentPageNo=totalPageCount;
        }
        //获取用户列表
        userList=userService.getUserList(queryUserName,queryUserRole,currentPageNo,pageSize);
        req.setAttribute("userList",userList);
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserRole",queryUserRole);
        req.setAttribute("queryUserName",queryUserName);

        //返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
}
