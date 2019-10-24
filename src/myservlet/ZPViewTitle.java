/*
 * Created on 2005-12-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * V100 zhanggan 20060913 -add return button from view one line.And spum the <PRE> tag to view more readable.
 */
package myservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mybean.*;
/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ZPViewTitle extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5040623708685743567L;

	/**
	 * Constructor of the object.
	 */
	public ZPViewTitle() {
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

		HttpSession session = request.getSession(false);
		String strLogin="false";
		if(session!=null)
			strLogin= (String) session.getAttribute("login");
		if(request.getParameter("idntfr")!=null && "true".equals(strLogin))
		{
			String strIdntfr = request.getParameter("idntfr");
			try{
				DbConnect1 dbCnnct = new mybean.DbConnect1();
				sqlConn= dbCnnct.getCnnct();
				sqlStmt=sqlConn.createStatement (java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY); 
				sqlCmd = "select title ,content from zgprogram where idntfr=\"" +
						strIdntfr +"\" order by title ";
				sqlRst = sqlStmt.executeQuery(sqlCmd);
				sqlRst.last();
				if(sqlRst.getRow()>0)
				{
					sqlRst.first();
					//V100 >>
					/*sbhtml.append("  <HEAD><TITLE>ZgProgramV1.0[" +
							sqlRst.getString(1)+
							"]</TITLE></HEAD>");
					*/
					sbhtml.append("  <HEAD><TITLE>ZgProgramV1.0[" +
							sqlRst.getString(1)+
							"]</TITLE><script language=\"javascript\">" +
							"\nfunction pageback(strTitle){" +
							" history.back();"+
							"\n}" +
							"</script></HEAD>");
					//V100 <<
					sbhtml.append("  <BODY>");
					do{
						//V100 >>
						//sbhtml.append(sqlRst.getString(1)+"<br><pre>"); //V100 --
						//sbhtml.append(sqlRst.getString(2)+"</pre><br>"); //V100 --
						sbhtml.append("<table><tr><td width=\"60\"><input type=\"button\" name=\"return\" value=\"Return\" onClick=\"pageback('"
								+sqlRst.getString(1)+
								"');\"  style=\"width:60\"></td>"
								+"<td align=\"left\"><b>"
								+sqlRst.getString(1)+"</b></td>"
								+"<td><FORM><INPUT onclick=window.print(); type=button value=Print name=Print></FORM></td>"
								+"</tr>");
						String viewString = sqlRst.getString(2);
						//viewString = viewString.replaceAll("[\n\r]","<br>");
						//viewString = viewString.replaceAll("\\\\","\\\\\\\\");
						//viewString = viewString.replaceAll("\\n","<br>");
						sbhtml.append("<tr><td colspan=\"3\">"+viewString+"</td></tr>");
						sbhtml.append("<tr><td width=\"60\" colspan=\"3\"><input type=\"button\" name=\"return\" value=\"Return\" onClick=\"pageback('"
								+sqlRst.getString(1)+
								"');\"  style=\"width:60\"></td></tr></table>");
						
//						V100 <<
					}while (sqlRst.next());
				}
				else
				{
					sbhtml.append("  <HEAD><TITLE>ZgProgramV1.0</TITLE></HEAD>");
					sbhtml.append("  <BODY>");
					sbhtml.append("Please select one title to display.");
				}
				sqlRst.close();
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
		else
		{
			sbhtml.append("  <HEAD><TITLE>ZgProgramV1.0</TITLE></HEAD>");
			sbhtml.append("  <BODY><center><b>Please login firstly!</b></center>");
		}
		sbhtml.append("  </BODY>");
		sbhtml.append("</HTML>");
		out.println(sbhtml);

		out.flush();
		out.close();

	}

}
