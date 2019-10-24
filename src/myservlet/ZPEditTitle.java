/*
 * Created on 2005-12-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * V100 ZHANGGAN 20060918 -AVOID THE DUPLICATE SUBMIT OCCURE.
 * V101 zhanggan 20060919 -Add Free Rich HTML editor.
 * V102 zhanggan 20061231 -Move the commit&return buttons to frond of the first line to workaround the long line issue which is still pending.
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
public class ZPEditTitle extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1116073860911969961L;

	/**
	 * Constructor of the object.
	 */
	public ZPEditTitle() {
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
		if(request.getParameter("idntfr")!=null&& "true".equals(strLogin))
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
					sbhtml.append("  <HEAD><TITLE>ZgProgramV1.0[" +
							sqlRst.getString(1)+
							"]</TITLE>" );
					sbhtml.append("<script language=\"javascript\" src=\"./js/editor/edtor.js\"></script>");  //V101 ++
					sbhtml.append("<script language=\"javascript\">" +
							"\nfunction pageback(strTitle){" +
							//"\nlocation.href=\"ZPSearchMain?title=\"+strTitle;" +
							" history.back();"+
							"\n}" +
							"</script>" +
							"</HEAD>");
					sbhtml.append("  <BODY>");
					sbhtml.append("<form name=\"editall\" method = \"post\" action=\"ZPEditProcess\">");
					sbhtml.append("<table align=\"center\"><tr>");   //V102C
					sbhtml.append("<td align=\"right\"><input type=\"submit\" name=\"validate\" id=\"validate\" value=\"validate\"></td>");	//V102++
					sbhtml.append("<td><input type=\"button\" name=\"return\" value=\"Return\" onClick=\"pageback('"+sqlRst.getString(1)+"');\"  style=\"width:60\"></td>");	//V102++					
					sbhtml.append("<td>Title</td>");
					sbhtml.append("<td><input type=\"text\" name=\"title\" id=\"title\" value=\"" +
							sqlRst.getString(1)+
							"\" size=\"100\"></td>");
					//sbhtml.append("<td align=\"right\"><input type=\"submit\" name=\"validate\" id=\"validate\" value=\"validate\"></td>");	//V102--
					//sbhtml.append("<td><input type=\"button\" name=\"return\" value=\"Return\" onClick=\"pageback('"+sqlRst.getString(1)+"');\"  style=\"width:60\"></td>");  //V102--
					sbhtml.append("</tr><tr><td colspan=\"4\">");
					String editString =  sqlRst.getString(2);
					//editString = editString.replaceAll("[\n\r]","<br>");
					editString = editString.replaceAll("\\\\","\\\\\\\\");
					editString = editString.replaceAll("[\']","\\\\\\\'");
					editString = editString.replaceAll("[\"]","\\\\\\\"");					
					//editString = editString.replaceAll("\\n","<br>");
					//editString = editString.replaceAll("\\n\\r","<br>");
					sbhtml.append("<textarea name=\"content\" id=\"content\" cols=\"120\" rows=\"30\">" +
							//editString +
							"</textarea>");
					//V101 >>
					sbhtml.append("<script language=\"javascript\">");
					sbhtml.append("var h = new HTMLEditor(\"htmlbox\",editall.content,\"100%\",500,\""+editString+"\");");
					sbhtml.append("</script>");
					//V101 <<
					sbhtml.append("</td></tr></table><input type=\"hidden\" name=\"idntfr\" value=\""+strIdntfr+"\"></form>");

				}
				else
					sbhtml.append("Please select one title to Edit.");
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
		else //For new created.
		{
			sbhtml.append("  <HEAD><TITLE>ZgProgramV1.0</TITLE>" );
			sbhtml.append("<script language=\"javascript\" src=\"./js/editor/edtor.js\"></script>");  //V101 ++
			sbhtml.append("<script language=\"javascript\">" +
					"\nfunction pageback(strTitle){" +
					//"\nlocation.href=\"ZPSearchMain?title=\"+strTitle;" +
					" history.back();"+
					"\n}" +
					"</script>" +
					"</HEAD>");
			//V100 >>
			sbhtml.append("<script language='JavaScript1.2'>");
			sbhtml.append("  var submitted = false;");
			sbhtml.append("  function submitform() {");
			sbhtml.append("    if (!submitted) {");
			sbhtml.append("      submitted = true;");
			sbhtml.append("      document.forms[0].submit();");
			sbhtml.append("    }");
			sbhtml.append("    else");
			sbhtml.append("      alert(\"Duplicate request!\");;");
			sbhtml.append("    return false;");
			sbhtml.append("  }");
			sbhtml.append("</script>");
			//V100 <<
			sbhtml.append("  <BODY>");
			if ("true".equals(strLogin))//For Add new .
			{
				//sbhtml.append("<form name=\"editall\" method = \"post\" action=\"ZPEditProcess\">");  //V100 --
				sbhtml.append("<form name=\"editall\" method = \"post\" onSubmit=\"return submitform()\" action=\"ZPEditProcess\">");  //V100 ++
				sbhtml.append("<table align=\"center\">");
				sbhtml.append("<tr><td>Title</td>");
				sbhtml.append("<td><input type=\"text\" name=\"title\" id=\"title\" value=\"[UNTITLED]\" size=\"100\"></td>");
				sbhtml.append("<td align=\"right\"><input type=\"submit\" name=\"validate\" id=\"validate\" value=\"validate\"></td>");
				sbhtml.append("<td><input type=\"button\" name=\"return\" value=\"Return\" onClick=\"pageback('');\"  style=\"width:60\"></td>");
				sbhtml.append("</tr><tr><td colspan=\"4\">");
				sbhtml.append("<textarea name=\"content\" id=\"content\" cols=\"120\" rows=\"20\"></textarea>");
				//V101 >>
				sbhtml.append("<script language=\"javascript\">"); 
				sbhtml.append("var h = new HTMLEditor(\"htmlbox\",editall.content,\"100%\",300,\"Please input your content here...\");");
				sbhtml.append("</script>");
				//V101 <<				
				sbhtml.append("</td></tr></table><input type=\"hidden\" name=\"idntfr\" value=\"\"></form>");
			}
			else
				sbhtml.append("<hr><b>You haven't login!</b></hr>");
		}
		sbhtml.append("  </BODY>");
		sbhtml.append("</HTML>");
		out.println(sbhtml);

		out.flush();
		out.close();
	}

}
