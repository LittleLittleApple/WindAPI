package wind;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//这里我们建立一个DBHelper类
public class DBHelper {
	private static DBHelper instance = null;
	private  String sqlUrl = null;
	private  String sqlUser = null;
	private  String sqlPassword = null;
	private DBHelper(ConfigurationSQL sqlCfg){ 
		this.sqlUrl = sqlCfg.url;
		this.sqlUser = sqlCfg.username;
		this.sqlPassword = sqlCfg.password;
	}
	
	public static DBHelper getInstance(ConfigurationSQL sqlCfg) {
		if(instance == null) {
			instance = new DBHelper(sqlCfg);
		}
		return instance;
	}
	

	// 此方法为获取数据库连接
	public Connection getCon() {
		Connection con = null;
		try {
			String driver = "com.mysql.jdbc.Driver"; // 数据库驱动
			String url = sqlUrl;//
			String user = sqlUser; // 用户名
			String password = sqlPassword;// 密码
			Class.forName(driver); // 加载数据库驱动
			con = DriverManager.getConnection(url, user, password);
			con.setClientInfo("supportBigNumbers", "true");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	// 查询语句
	public ResultSet executeQuery(String sql) throws SQLException {
		Connection con = getCon();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}

	public ResultSet executeQuery(String sql, Object... obj)
			throws SQLException {
		Connection con = getCon();
		PreparedStatement pstmt = con.prepareStatement(sql);
		for (int i = 0; i < obj.length; i++) {
			pstmt.setObject(i + 1, obj[i]);
		}
		ResultSet rs = pstmt.executeQuery();
		return rs;
	}

	// 执行增删改
	public int executeNonQuery(String sql) throws SQLException {
		Connection con = getCon();
		Statement stmt = con.createStatement();
		return stmt.executeUpdate(sql);
	}

	public int executeNonQuery(String sql, Object... obj)
			throws SQLException {
		Connection con = getCon();
		PreparedStatement pstmt = con.prepareStatement(sql);
		for (int i = 0; i < obj.length; i++) {
			pstmt.setObject(i + 1, obj[i]);
		}
		return pstmt.executeUpdate();
	}
	
	//业务相关。 TODO: 需要抽取到业务类
	
	public int syncWsd(String stockCode, String... fields){
		
		return -1;
	}
}