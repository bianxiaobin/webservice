package com.xui.db;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import com.xui.bean.User;
import com.xui.utils.ConfigUtil;
import com.xui.utils.DBHelp;

public class UserDB {
	SqlSession session = DBHelp.getSqlSession();
	public int insertUser(User user){
		String mapper = "com.xui.bean.mapper.userMapper.insertUser";
		int count = session.insert(mapper, user);
		session.commit();
		return count;
	}
	public User login(User user){
		String mapper = "com.xui.bean.mapper.userMapper.getUser";
		return session.selectOne(mapper, user);
	}
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		InputStream is = UserDB.class.getClassLoader().getResourceAsStream(ConfigUtil.DBCONFIG);
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SqlSession session = sessionFactory.openSession();
//		String statement = "com.xui.bean.mapper.userMapper.getUser";
		String statement = "com.xui.bean.mapper.userMapper.insertUser";
		User u = new User();
		u.setPwd("12345");
		u.setUser_name("hanghang");
//		User user = session.selectOne(statement,u);
//		System.out.println(user);;
		int i = session.insert(statement,u);
		session.close();session.clearCache();
		System.out.println(i);
	}
}
