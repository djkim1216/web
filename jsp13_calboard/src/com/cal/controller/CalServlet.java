package com.cal.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cal.dao.CalDao;
import com.cal.dao.Util;
import com.cal.dto.CalDto;

@WebServlet("/calcontroller.do")
public class CalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		String command = request.getParameter("command");
		CalDao dao = new CalDao();
		
		if(command.equals("calendar")) {
			response.sendRedirect("calendar.jsp");
		} else if(command.equals("insertcal")) {
			String year = request.getParameter("year");
			String month = request.getParameter("month");
			String date = request.getParameter("date");
			String hour = request.getParameter("hour");
			String min = request.getParameter("min");
			
			String id = request.getParameter("id");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String mdate = year+
					Util.isTwo(month)+
					Util.isTwo(date)+
					Util.isTwo(hour)+
					Util.isTwo(min);
			
			int res = dao.insert(new CalDto(0,id,title,content,mdate,null));
			System.out.println("res : "+res );
			
			if(res>0) {
				response.sendRedirect("calcontroller.do?command=calendar");
			} else {
				request.setAttribute("msg", "일정 추가 실패");
				dispatch("error.jsp", request, response);
			}
			
		} else if(command.equals("callist")) {
			String year = request.getParameter("year");
			String month = request.getParameter("month");
			String date = request.getParameter("date");
			
			String yyyyMMdd = year + Util.isTwo(month) + Util.isTwo(date);
			
			List<CalDto> list = dao.getCalList("kh", yyyyMMdd);
			request.setAttribute("list", list);
			dispatch("callist.jsp", request,response);
			
		}
	}

	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		doGet(request,response);
	}
	
	public void dispatch(String url, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatch = request.getRequestDispatcher(url);
		dispatch.forward(request, response);
	}

}
