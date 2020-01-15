package com.cal.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.cal.db.SqlMapConfig;
import com.cal.dto.CalDto;

public class CalDao extends SqlMapConfig{
	
	private String namespace ="com.cal.mapper.";
	
	public int insert(CalDto dto) {
		SqlSession session = null;
		int res = 0;
		
		try {
			session = getSqlSessionFactory().openSession(true);
			res = session.insert(namespace+"insert",dto);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return res;
	}
	
	public List<CalDto> getCalList(String id, String yyyyMMdd){
		
//		String sql = "SELECT SEQ, ID, TITLE, CONTENT,MDATE, REGDATE"
//				+ " FROM CALBOARD"
//				+ " WHERE ID = ? AND SUBSTR(MDATE,1,8) = ? ";
		SqlSession session = null;
		List<CalDto> list = null;
		CalDto dto = new CalDto();
		
		
		try {
			session=getSqlSessionFactory().openSession(true);
			dto.setId(id);
			dto.setMdate(yyyyMMdd);
			list = session.selectList(namespace+"getCalList",dto);
		}catch(Exception e){
			System.out.println("[ERROR]: selectList");
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}
	
	public List<CalDto> getCalViewList(String id, String yyyyMM){
		
		SqlSession session = null;
		List<CalDto> list = null;
		CalDto dto = new CalDto();
		
		try {
			session = getSqlSessionFactory().openSession(true);
			
			dto.setId(id);
			dto.setMdate(yyyyMM);
			
			list = session.selectList(namespace+"getCalViewList",dto);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		return list;
	}
	
	public int getCalViewCount(String id, String yyyyMMdd) {
		
		SqlSession session = null;
		int count = 0;
		CalDto dto = new CalDto();
		
		try {
			System.out.println("id, yyyyMMdd : " + id + yyyyMMdd);
			session = getSqlSessionFactory().openSession(false);
			
			dto.setId(id);
			dto.setMdate(yyyyMMdd);
			
			count = session.selectOne(namespace+"getCalViewCount",dto);
			
			System.out.println("DAo의 카운트 : "+count);
			if(count>0) {
				session.commit();
			}
		} catch(Exception e) {
			System.out.println("[ERROR] getCalViewCount");
			e.printStackTrace();
		} finally {
			session.close();
		}
		
		
		return count;
	}
	
}






























