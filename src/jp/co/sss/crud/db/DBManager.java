package jp.co.sss.crud.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * データベース接続&切断用クラス
 */
public class DBManager {
	
	/** ドライバクラス名 */
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
         
	/** 接続するDBのURL */
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/xepdb1";

	/** DB接続するためのユーザ名 */
	private static final String USER_NAME = "console_crud_user";

	/** DB接続するためのパスワード */
	private static final String PASSWORD = "systemsss";

	/**
	 * DBと接続する
	 *
	 * @return DBコネクション
	 * @throws ClassNotFoundException
	 *             ドライバクラスが見つからなかった場合
	 * @throws SQLException
	 *             DB接続に失敗した場合
	 */
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		
		// JDBCドライバクラスをJVMに登録
		Class.forName(DRIVER);

		// DBに接続
		Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);

		return connection;
	}

	/**
	 * DBとの接続を切断する
	 *
	 * @param connection
	 *            DBとの接続情報
	 * @throws SQLException
	 *             クローズ処理に失敗した場合に送出
	 */
	public static void close(Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	/**
	 * PreparedStatementをクローズする
	 *
	 * @param preparedStatement
	 *            ステートメント情報
	 * @throws SQLException
	 *             クローズ処理に失敗した場合に送出
	 */
	public static void close(PreparedStatement preparedStatement) throws SQLException {
		if (preparedStatement != null) {
			preparedStatement.close();
		}
	}

	/**
	 * ResultSetをクローズする
	 *
	 * @param resultSet
	 *            SQL検索結果
	 * @throws SQLException
	 *             クローズ処理に失敗した場合に送出
	 */
	public static void close(ResultSet resultSet) throws SQLException {
		if (resultSet != null) {
			resultSet.close();
		}
	}

}
