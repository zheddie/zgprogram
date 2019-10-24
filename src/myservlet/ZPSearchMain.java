/*
 * Created on 2005-12-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * V101 ZHANGGAN 20060509  -Content search enabled.
 * V102 zhanggan 20060516  -"null" string for initial value/Add date for startDate.
 * V103 zhanggan 20060912  -Change the initial date to be 7 days before today.
 * v104 ZHANGGAN 20060913  -Remove mouseover focus functions for password and date.
 * V105 zhanggan 20060918  -Add "delete" function.
 * V106 zhanggan 20070101  -Add create date for each line.Initally ,all create date were set to be the access date from database.
 * V107 zhanggan 20070101  -add color for every even line.
 * V108 zhanggan 20070121  -Support firefox for the format of search criteria part.
 */

package myservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mybean.DbConnect1;
import java.util.Date;
import java.text.SimpleDateFormat;
//V103 >>
import java.util.Calendar;
import java.util.GregorianCalendar;

//V103 <<
/** 
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ZPSearchMain extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3019633962235168510L;

	/**
	 * Constructor of the object.
	 */
	public ZPSearchMain() {
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
        //V102>>
		Date nowTime = new Date();
		//V103 >>
		GregorianCalendar SevenDaysBeforeToday = new GregorianCalendar();
		SevenDaysBeforeToday.setTime(nowTime);
		SevenDaysBeforeToday.add(Calendar.DAY_OF_YEAR,-7);
		Date NewTime = SevenDaysBeforeToday.getTime();
		//V103 <<
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
		//V102 <<
		StringBuffer sbhtml = new StringBuffer();
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		Connection sqlConn = null;
		Statement sqlStmt = null;
		String sqlCmd = null;
		ResultSet sqlRst = null;
		String strContent = request.getParameter("content");    //V101+
		String strStartDate = request.getParameter("startdate");    //V102+
		String strTitle = request.getParameter("title");		
		String strPass = request.getParameter("passwd");
		HttpSession session = request.getSession(false);
		if (strPass == null && session!=null) strPass = (String) session.getAttribute("passwd");
		sbhtml.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		sbhtml.append("<HTML>");
		sbhtml.append("  <HEAD><TITLE>ZgProgramV1.0</TITLE></HEAD>");
		sbhtml.append("  <BODY>\n");
		sbhtml.append("<table width=\"100%\" border=\"0\" >");
		sbhtml.append("<form name=\"searchform\" method=\"post\" action=\"ZPSearchMain\">");
		
		//sbhtml.append("    <tr bgcolor=\"#E0F4F8\"><td >Title</td><td colspan = \"2\">");  //V102-
		sbhtml.append("    <tr bgcolor=\"#E0F4F8\"><td width=\"7%\">Title</td><td width=\"63%\">");    //V102+ V108c
		if( strTitle ==null) strTitle="";
		if( strContent ==null) strContent="";          //V102+
		//V103 >>
		//if( strStartDate ==null) strStartDate=sdfDate.format(nowTime);          //V102+
		if( strStartDate ==null) strStartDate=sdfDate.format(NewTime);
		//V103 <<
		sbhtml.append("<input type=\"text\" name=\"title\" id=\"title\" value=\""+strTitle+"\" size=\"60\" onmouseover=\"this.focus()\" onfocus=\"this.select()\"></td>");	//V108c
		sbhtml.append("<td width=\"15%\"><input type=\"text\" name=\"startdate\" id=\"startdate\" value=\""+strStartDate+"\" size=\"10\" onfocus=\"this.select()\"></td>");  //V102+ V108c
		sbhtml.append("<td width=\"15%\">");	//V108c
		//V104 >>
		//sbhtml.append("<input type=\"password\" name=\"passwd\" onmouseover=\"this.focus()\" onfocus=\"this.select()\" size=\"20\"");
		sbhtml.append("<input type=\"password\" name=\"passwd\" size=\"10\" onfocus=\"this.select()\"");		//V108c
		//V104 <<
		if(strPass!=null) sbhtml.append(" value=\"" +strPass+"\">");
		else sbhtml.append(" value=\"\">");
		sbhtml.append("</td></tr><tr bgcolor=\"#E0F4F8\"><td width=\"7%\">Content</td><td width=\"63%\">");
		sbhtml.append("<input type=\"text\" name=\"content\" id=\"content\" value=\""+strContent+"\" size=\"60\" onmouseover=\"this.focus()\" onfocus=\"this.select()\"></td>");	//V108c
		//V104 >>
		//sbhtml.append("</td><td><input type=\"text\" name=\"startdate\" id=\"startdate\" value=\""+strStartDate+"\" size=\"%100\" onmouseover=\"this.focus()\" onfocus=\"this.select()\">");  //V102+
		//sbhtml.append("<td><input type=\"text\" name=\"startdate\" id=\"startdate\" value=\""+strStartDate+"\" size=\"10\" onfocus=\"this.select()\"></td>");  //V102+ V108-
		//V104 <<
		sbhtml.append("<td width=\"15%\"><input type=\"submit\" name=\"search\" value=\"Search\"></td></form><td width=\"15%\">");	//V108c
		sbhtml.append("<form name=\"addnewform\" method=\"post\" action=\"ZPEditTitle\">");
		sbhtml.append("<input type=\"submit\" name=\"addnew\" value=\"AddNew\" ></form>");
		//sbhtml.append("</td></tr><tr><td colspan=\"4\" align=\"center\">");  //V102-
		sbhtml.append("</td></tr><tr><td colspan=\"5\" align=\"center\">");   //V102+
		sbhtml.append("<table width=\"100%\" border=\"1\">");
		
		try{
			DbConnect1 dbCnnct = new mybean.DbConnect1();
			sqlConn= dbCnnct.getCnnct();

			sqlStmt=sqlConn.createStatement (java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);
			if(strTitle != null &&strPass!=null && ! "".equals(strTitle)&&"zg1307ZG".equals(strPass))      //V102 -
			{
				if(session== null)
				{
					session = request.getSession();
					session.setMaxInactiveInterval(60*60*5);
					session.setAttribute("login","true");
					session.setAttribute("passwd",strPass);
				}
				sqlCmd = "select title ,idntfr,accessdate,createdate from zgprogram where title like '%"+   //V106 changed
					strTitle+
					"%'"
					+"and content  like '%" +strContent + "%'"+        //V101 +
					"and accessdate  >= '" +strStartDate + "'"+        //V102 +
					"and status=0 order by  accessdate desc,title ";
				//sbhtml.append(sqlCmd);           //V101 Test use.
				sqlRst = sqlStmt.executeQuery(sqlCmd);
				sqlRst.last();
				boolean isEven = true ;  //V107 ++
				if(sqlRst.getRow()>0)
				{
					sqlRst.first();
					do{
						//V107 >>
						if(!isEven){
							sbhtml.append("<tr>");
						}
						else {
							sbhtml.append("<tr bgcolor=\"#B7F4B7\">");
						}
						//V107 <<
						sbhtml.append("<td width=\"%4\"><a href=\"ZPEditTitle?idntfr="+sqlRst.getString(2)+"\">Edit</a></td>");
						sbhtml.append("<td width=\"%80\"><a href=\"ZPViewTitle?idntfr="+sqlRst.getString(2)+"\">"+sqlRst.getString(1)+"</a></td>");
						//sbhtml.append("<td width=\"%10\">"+sqlRst.getString(3)+"</td>");  //V105 --
						//V105 >>
						sbhtml.append("<td width=\"%6\">"+sqlRst.getString(3)+"</td>");
						sbhtml.append("<td width=\"%6\">"+sqlRst.getString(4)+"</td>");		//V106 ++
						//strTitle = strTitle.replaceAll("%","%25");
						//strContent = strContent.replaceAll("%","%25");
						sbhtml.append("<td width=\"%4\"><a href=\"ZPDelProcess?idntfr="+sqlRst.getString(2)
								+"&title="+URLEncoder.encode(strTitle,"UTF-8")
								+"&content="+URLEncoder.encode(strContent,"UTF-8")
								+"&startdate="+strStartDate+"\"><img src=\"./resources/delete.jpg\"></a></td>");     //V106 changed
						//V105 <<
						sbhtml.append("</tr>");
						isEven = !isEven;  //V107 ++
					}while (sqlRst.next());
				}
				else
					sbhtml.append("<hr><center><h3>No data found!.</h3></center></hr>");
				sqlRst.close();
			}
			else
				sbhtml.append("<hr><center><h3>Please input the title you want to display <br>with the correct password!</h3></center></hr>");
			
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
		sbhtml.append(" </table></td></tr></table>");
		sbhtml.append("  </BODY>");
		sbhtml.append("</HTML>");
		out.println(sbhtml);

		out.flush();
		out.close();
	}

}
