package com.cal.db;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlMapConfig {

	private SqlSessionFactory sqlSessionFactory;
	
	public SqlSessionFactory getSqlSessionFactory() {
		String resource = "com/cal/db/cal-config.xml";
		Reader reader = null;
		
		try {
			reader = Resources.getResourceAsReader(resource);
			sqlSessionFactory=new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		return sqlSessionFactory;
		
	}
}
