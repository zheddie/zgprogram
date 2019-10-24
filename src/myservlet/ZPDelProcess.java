/*
 * Created on 2006-9-15
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package myservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.sql.Connection;
//import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mybean.DbConnect1;

/**
 * @author zhanggan
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ZPDelProcess extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2375904896114174654L;
	/**
	 * 
	 */
	public ZPDelProcess() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doPost(request,response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		StringBuffer sbhtml = new StringBuffer();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		Connection sqlConn = null;
		Statement sqlStmt = null;
		String sqlCmd = null;
		//ResultSet sqlRst = null;
		sbhtml.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		sbhtml.append("<HTML>");
		sbhtml.append("  <HEAD><TITLE>ZgProgramV1.0</TITLE></HEAD>");
		sbhtml.append("  <BODY>");
		String strContent = request.getParameter("content");
		String strStartDate = request.getParameter("startdate");
		String strTitle = request.getParameter("title");
		HttpSession session = request.getSession(false);
		String strLogin="false";
		String strIdntfr = "";
		if(session!=null)
			strLogin= (String) session.getAttribute("login");
		if("true".equals(strLogin)){
			try{
				
				DbConnect1 dbCnnct = new mybean.DbConnect1();
				sqlConn= dbCnnct.getCnnct();
				sqlStmt=sqlConn.createStatement (java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY); 

				
				if(request.getParameter("idntfr")!=null && !"".equals(request.getParameter("idntfr")))
				{
					strIdntfr = request.getParameter("idntfr");
					sqlCmd = "update zgprogram set status=-2 ,accessdate=now() where idntfr=\"" +
							strIdntfr +"\"";
					sbhtml.append(sqlCmd); 
				}

				sqlStmt.executeUpdate(sqlCmd);
				sbhtml.append(sqlCmd);
				response.sendRedirect("ZPSearchMain?title="+URLEncoder.encode(strTitle,"UTF-8")
						+"&startdate="+strStartDate 
						+"&content="+URLEncoder.encode(strContent,"UTF-8"));

				sqlStmt.close();
			}
			catch(Exception e){
				sbhtml.append("ERROR:"+e.getMessage());
				StackTraceElement [] steAll = e.getStackTrace();
				for(int so =0 ;so <steAll.length;so++)
				{
					sbhtml.append(steAll[so].getClassName());
					sbhtml.append(":"+steAll[so].getMethodName());
					sbhtml.append(String.valueOf(steAll[so].getLineNumber()));
					sbhtml.append("<br>");
				}			
			}

		}
			
		sbhtml.append("  </BODY>");
		sbhtml.append("</HTML>");
		out.println(sbhtml);

		out.flush();
		out.close();
	}
}
