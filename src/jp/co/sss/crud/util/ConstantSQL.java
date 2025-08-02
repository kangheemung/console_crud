package jp.co.sss.crud.util;

/**
 * SQL文の管理用クラス
 */
public class ConstantSQL {
	/** SQL文(全件検索) */

    public static final String SQL_FIND_ALL = "SELECT emp_id, emp_name, gender, birthday, dept_name FROM employee INNER JOIN department ON employee.dept_id = department.dept_id";	/** SQL文(登録) */
	//public static String SQL_INSERT = "INSERT INTO employee VALUES(seq_emp.NEXTVAL, ?, ?, ?, ?)";
	public static String SQL_INSERT = "INSERT INTO employee (emp_id, emp_name, gender, birthday, dept_id) " +"VALUES (seq_emp.NEXTVAL, ?, ?, TO_DATE(?, 'YYYY/MM/DD'), TO_NUMBER(?))";

	public static String SQL_UPDATE =
		    "UPDATE employee SET " +
		    "emp_name = CASE WHEN ? IS NOT NULL THEN ? ELSE emp_name END, " +
		    "gender = CASE WHEN ? IS NOT NULL THEN TO_NUMBER(?) ELSE gender END, " +
		    "birthday = CASE WHEN ? IS NOT NULL THEN TO_DATE(?, 'YYYY/MM/DD') ELSE birthday END, " +
		    "dept_id = CASE WHEN ? IS NOT NULL THEN ? ELSE dept_id END " +
		    "WHERE emp_id = ?";




	public static String SQL_DELETE = "DELETE FROM employee WHERE emp_id = ?";

}
