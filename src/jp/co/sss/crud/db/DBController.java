package jp.co.sss.crud.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jp.co.sss.crud.util.ConstantSQL;

/**
 * データベース操作用クラス
 */
public class DBController {
	/**
	 * 全件表示
	 *
	 * @throws ClassNotFoundException
	 *             ドライバクラスが存在しない場合に送出
	 * @throws SQLException
	 *             データベース操作時にエラーが発生した場合に送出
	 */
	public static void findAll() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			// DBに接続
			connection = DBManager.getConnection();

			// ステートメントを作成
			preparedStatement = connection.prepareStatement(ConstantSQL.SQL_FIND_ALL);

			// SQL文を実行
			resultSet = preparedStatement.executeQuery();

			// レコードを出力
			System.out.println("社員ID\t社員名\t性別\t生年月日\t部署名");
			while (resultSet.next()) {
				System.out.print(resultSet.getString("emp_id") + "\t");
				System.out.print(resultSet.getString("emp_name") + "\t");

				int gender = Integer.parseInt(resultSet.getString("gender"));
				if (gender == 1) {
					System.out.print("男性\t");
				} else if (gender == 2) {
					System.out.print("女性\t");
				}

				System.out.print(resultSet.getString("birthday") + "\t");
				System.out.println(resultSet.getString("dept_name"));
			}

			System.out.println("");
		} finally {
			// ResultSetをクローズ
			DBManager.close(resultSet);
			// Statementをクローズ
			DBManager.close(preparedStatement);
			// DBとの接続を切断
			DBManager.close(connection);
		}
	}

	/**
	 * 登録
	 *
	 * @param empName
	 *            社員名
	 * @param gender
	 *            性別
	 * @param birthday
	 *            生年月日
	 * @param deptId
	 *            部署ID
	 * @throws ClassNotFoundException
	 *             ドライバクラスが存在しない場合に送出
	 * @throws SQLException
	 *             データベース操作時にエラーが発生した場合に送出
	 */
	public static void insert(String empName, String gender, String birthday, String deptId)
			throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// DBに接続
			connection = DBManager.getConnection();

			// ステートメントを作成
			preparedStatement = connection.prepareStatement(ConstantSQL.SQL_INSERT);

			// 入力値をバインド
			preparedStatement.setString(1, empName);
			preparedStatement.setString(2, gender);
			preparedStatement.setString(3, birthday);
			preparedStatement.setString(4, deptId);

			// SQL文を実行
			preparedStatement.executeUpdate();

			// 登録完了メッセージを出力
			System.out.println("社員情報を登録しました");
		} finally {
			DBManager.close(preparedStatement);
			DBManager.close(connection);
		}
	}

	public static String findNameById(String empId) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String empName = null;

		try {
			connection = DBManager.getConnection();
			String sql = "SELECT emp_name FROM employee WHERE emp_id = ?";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, empId);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				empName = resultSet.getString("emp_name");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			DBManager.close(resultSet);
			DBManager.close(preparedStatement);
			DBManager.close(connection);
		}

		return empName;
	}

	public static void update(String empName, String gender, String birthday, String deptId, String empId)
			throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DBManager.getConnection();
			preparedStatement = connection.prepareStatement(ConstantSQL.SQL_UPDATE);

			// emp_name 判定用 / 新値
			preparedStatement.setString(1, empName);
			preparedStatement.setString(2, empName);

			// gender 判定用 / 新値
			preparedStatement.setString(3, gender);
			preparedStatement.setString(4, gender);

			// birthday 判定用 / 新値（TO_DATE使用のためVARCHARで渡す）
			if (birthday == null || birthday.trim().isEmpty()) {
				preparedStatement.setNull(5, java.sql.Types.VARCHAR);
				preparedStatement.setNull(6, java.sql.Types.VARCHAR);
			} else {
				preparedStatement.setString(5, birthday);
				preparedStatement.setString(6, birthday);
			}

			// dept_id 判定用 / 新値（TO_NUMBER使用のためINTEGERで渡す）
			if (deptId == null || deptId.trim().isEmpty()) {
				preparedStatement.setNull(7, java.sql.Types.INTEGER);
				preparedStatement.setNull(8, java.sql.Types.INTEGER);
			} else {
				preparedStatement.setInt(7, Integer.parseInt(deptId));
				preparedStatement.setInt(8, Integer.parseInt(deptId));
			}

			// emp_id（WHERE句用）
			preparedStatement.setInt(9, Integer.parseInt(empId));

			// 更新実行
			preparedStatement.executeUpdate();

			// 任意で完了メッセージ（削除してもOK）
			System.out.println("社員情報を更新しました。");

		} catch (SQLException e) {
			e.printStackTrace(); // 詳細なエラー出力で原因を特定しやすく
		} finally {
			DBManager.close(preparedStatement);
			DBManager.close(connection);
		}
	}



	public static void delete(String empId) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// DBに接続
			connection = DBManager.getConnection();

			// ステートメントを作成
			preparedStatement = connection.prepareStatement(ConstantSQL.SQL_DELETE);

			// 入力値をバインド
			preparedStatement.setString(1, empId);

			// SQL文を実行
			int cnt = preparedStatement.executeUpdate();

			System.out.println(cnt + "件のデータを削除しました。");
		} finally {
			DBManager.close(preparedStatement);
			DBManager.close(connection);
		}
	}

	public static void searchByName(String empName) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = DBManager.getConnection();
			String sql = "SELECT emp_id, emp_name, gender, TO_CHAR(birthday, 'YYYY/MM/DD') AS birthday, dept_name " +
					"FROM employee e INNER JOIN department d ON e.dept_id = d.dept_id " +
					"WHERE emp_name LIKE ? ORDER BY emp_id";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "%" + empName + "%");

			resultSet = preparedStatement.executeQuery();

			System.out.println("社員ID\t社員名\t性別\t誕生日\t\t部署名");
			boolean hasResults = false;

			while (resultSet.next()) {
				hasResults = true;
				System.out.printf(
						"%d\t%s\t%s\t%s\t%s%n",
						resultSet.getInt("emp_id"),
						resultSet.getString("emp_name"),
						resultSet.getString("gender"),
						resultSet.getString("birthday"),
						resultSet.getString("dept_name"));
			}

			if (!hasResults) {
				// 検索結果なし：項目名のみ表示してメニューに戻る
				System.out.println("※該当する社員が見つかりませんでした。");
			}

		} finally {
			DBManager.close(resultSet);
			DBManager.close(preparedStatement);
			DBManager.close(connection);
		}
	}

	public static void searchById(String deptId) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = DBManager.getConnection();

			String sql = "SELECT emp_id, emp_name, gender, TO_CHAR(birthday, 'YYYY/MM/DD') AS birthday, dept_name " +
					"FROM employee e INNER JOIN department d ON e.dept_id = d.dept_id " +
					"WHERE e.dept_id = ? ORDER BY emp_id";

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, deptId);

			resultSet = preparedStatement.executeQuery();

			System.out.println("社員ID\t社員名\t性別\t生年月日\t部署名");
			boolean hasResults = false;

			while (resultSet.next()) {

				hasResults = true;
				int genderCode = resultSet.getInt("gender");
				String genderLabel = (genderCode == 1) ? "男性" : (genderCode == 2) ? "女性" : "未設定";

				System.out.printf("%d\t%s\t%s\t%s\t%s%n",
						resultSet.getInt("emp_id"),
						resultSet.getString("emp_name"),
						genderLabel,
						resultSet.getString("birthday"),
						resultSet.getString("dept_name"));
			}

			if (!hasResults) {
				System.out.println("該当する社員は存在しません。");
			}

		} finally {
			DBManager.close(resultSet);
			DBManager.close(preparedStatement);
			DBManager.close(connection);
		}
	}

}