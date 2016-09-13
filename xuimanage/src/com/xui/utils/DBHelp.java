package com.xui.utils;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DBHelp {
	private static SqlSession session = null;
	private static DBHelp db;
	private DBHelp(){};
	public static synchronized DBHelp getInstance(){
		if(db == null){
			db = new DBHelp();
		}
		return db;
	}
	public static synchronized SqlSession getSqlSession(){
		if(session == null){
			try {
				InputStream is = DBHelp.class.getClassLoader().getResourceAsStream(ConfigUtil.DBCONFIG);
				SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
				session = sessionFactory.openSession();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return session;
	}
	public static void clearCache(){
		if(session != null){
			session.clearCache();
		}else{
			try {
				throw new Exception();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.err.print("session为空，请调用getSession进行初始化");;
			}
		}
	}
	public static void closeSession(){
		if(session != null){
			session.close();
		}else{
			try {
				throw new Exception();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.err.print("session已经不存在了！");;
			}
		}
	}
}




