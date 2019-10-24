/*
 * Created on 2005-11-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 * V101 ZHANGGAN 2006/05/07 -New MySQL-connector used on windowsXP.
 * V102 zhanggan 2006/09/26 -Delete the debug line.
 */
package mybean;
import java.sql.*;
import java.util.Properties;
//import com.mysql.jdbc.ReplicationDriver;     //V101 +
/**
 * @author zhanggan
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DbConnect1 {

	String username = "zhanggan";
	String password = "zhanggan";
	String url="jdbc:mysql://localhost/zgprogram";
	Connection sqlConn = null; 
	public DbConnect1() throws ClassNotFoundException, SQLException{
        Class.forName ( "com.mysql.jdbc.Driver" );
		//Class.forName ( "org.gjt.mm.mysql.Driver" );        //V101 -
		//System.out.println ( "MySQL Driver Found" );   //V102 --
		Properties p = new Properties();
		p.setProperty("user",username);
		p.setProperty("password",password);
		p.setProperty("useUnicode","true");
		p.setProperty("characterEncoding","gb2312");
		
		sqlConn= java.sql.DriverManager.getConnection (url,p);
		sqlConn.setAutoCommit(true);

	}
	public Connection getCnnct(){
		return sqlConn;
	}
	public void close() throws SQLException
	{
		sqlConn.close();
	}

}
