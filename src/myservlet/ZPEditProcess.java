/*
 * Created on 2005-12-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * V101 zhanggan 20060504 -Process the e:\xxx\yyy issues of "\"
 * V102 zhanggan 20070119 - Add create date for each line.
 */
package myservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mybean.DbConnect1;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ZPEditProcess extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5280549168025307099L;

	/**
	 * Constructor of the object.
	 */
	public ZPEditProcess() {
		super();
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		StringBuffer sbhtml = new StringBuffer();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		Connection sqlConn = null;
		Statement sqlStmt = null;
		String sqlCmd = null;
		ResultSet sqlRst = null;
		sbhtml.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		sbhtml.append("<HTML>");
		sbhtml.append("  <HEAD><TITLE>ZgProgramV1.0</TITLE></HEAD>");
		sbhtml.append("  <BODY>");
		String strTitle = request.getParameter("title");
		String strContent = request.getParameter("content");
		HttpSession session = request.getSession(false);
		String strLogin="false";
		String strIdntfr = "";
		if(session!=null)
			strLogin= (String) session.getAttribute("login");
		if(strTitle !=null && strContent != null&& "true".equals(strLogin)){
			try{
				
				DbConnect1 dbCnnct = new mybean.DbConnect1();
				sqlConn= dbCnnct.getCnnct();
				sqlStmt=sqlConn.createStatement (java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);
				strContent = strContent.replaceAll("[\n\r]","");            //Delete any \n\r.
				strContent = strContent.replaceAll("\\\\","\\\\\\\\");      //V101+ one to two?
				strContent = strContent.replaceAll("[\']","\\\\\\\'");
				strContent = strContent.replaceAll("[\"]","\\\\\\\"");
				
				if(request.getParameter("idntfr")!=null && !"".equals(request.getParameter("idntfr")))
				{
					strIdntfr = request.getParameter("idntfr");
					sqlCmd = "update zgprogram set title=\"" +
							strTitle +"\",content=\""+
							strContent+"\",accessdate=now() "+
							" where idntfr=\"" +
							strIdntfr +"\"";
					sbhtml.append(sqlCmd);                  //V101+ for test only.
				}
				else
				{
					sqlCmd = "select max(idntfr) from zgprogram";
					sqlRst = sqlStmt.executeQuery(sqlCmd);
					sqlRst.first();
					int iMax = sqlRst.getInt(1);
					iMax++;
					strIdntfr = String.valueOf(iMax);
					strIdntfr = "0000000000".substring(0,strIdntfr.length()) +strIdntfr;
					sqlCmd = "insert into zgprogram (title,content,idntfr,accessdate,createdate) values('"+strTitle+"','"+strContent+"','"+strIdntfr+"',now(),now())";  //V101 changed.
					sqlRst.close();
				}
				sqlStmt.executeUpdate(sqlCmd);
				sbhtml.append(sqlCmd);
				response.sendRedirect("ZPEditTitle?idntfr="+strIdntfr);

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
