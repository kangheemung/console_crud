package jp.co.sss.crud.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jp.co.sss.crud.db.DBController;

/**
 * 社員管理システム実行用クラス
 */
public class SystemStart {
	/**
	 * メイン処理
	 *
	 * @param args
	 *            コマンドライン引数
	 */
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int menuNo = 0;
		try {
			do {
				// メニューの表示
				System.out.println("=== 社員管理システム ===");
				System.out.println("1. 全件表示");
				System.out.println("2. 登録");
				System.out.println("3. 更新");
				System.out.println("4. 削除");	
				System.out.println("5. 社員名検索"); // メニュー追加
				System.out.println("6. 部署ID検索");//部署ID検索（追加機能1）
				System.out.println("7. 終了");
				//				System.out.print("メニュー番号を入力してください:");
				
				
				// メニュー番号の入力
				//String menuNoStr = br.readLine();
				//			menuNo = Integer.parseInt(menuNoStr);

				while (true) {
					System.out.print("メニュー番号を入力してください：");
					String menuNoStr = br.readLine();

					// 入力が空かどうかをチェック
					if (menuNoStr == null || menuNoStr.trim().isEmpty()) {
						System.out.println("空の入力です。1〜6の数字を入力してください。");
						continue;
					}

					try {
						menuNo = Integer.parseInt(menuNoStr);

						// 範囲チェック
						if (menuNo >= 1 && menuNo <= 7) {
							break; // 正常な入力なのでループ終了
						} else {
							System.out.println("1〜7の数字を入力してください。");
						}
					} catch (NumberFormatException e) {
						System.out.println("数値として認識できません。1〜6の数字を入力してください。");
					}
				}

				// 機能の呼出
				switch (menuNo) {
				case 1:

					DBController.findAll();
					break;

				case 2:
					// 登録する値を入力
					String empName;
					do {
						System.out.print("社員名を入力してください: ");
						empName = br.readLine();

						// 空文字チェック
						if (empName == null || empName.trim().isEmpty()) {
							System.out.println("社員名は空欄にできません。もう一度入力してください。");
							continue;
						}

						// 数字のみチェック（正規表現）
						if (empName.matches("\\d+")) {
							System.out.println("社員名は文字で入力して下さい。");
							continue;
						}

						// 問題がなければループを抜ける
						break;
					} while (true);

					int genderInput;
					do {
						System.out.print("性別(1:男性, 2:女性):");
						try {
							genderInput = Integer.parseInt(br.readLine());
							if (genderInput == 1 || genderInput == 2) {
								break; // 正しい値ならループを抜ける
							} else {
								System.out.println("1か2を入力してください。");
							}
						} catch (NumberFormatException e) {
							System.out.println("エラー：数字を入力してください。");
						}
					} while (true);
					String gender = String.valueOf(genderInput);

					String birthday;
					do {
						System.out.print("生年月日(西暦年/月/日):");
						birthday = br.readLine();

						if (birthday == null || birthday.trim().isEmpty()) {
							System.out.println("空欄ではなく、正しい日付形式で入力してください（例：2000/01/01）");
							continue;
						}

						String[] parts = birthday.trim().split("/");
						if (parts.length != 3) {
							System.out.println("形式エラー：yyyy/MM/dd で入力してください（例：2000/01/01）");
							continue;
						}

						try {
							int year = Integer.parseInt(parts[0]);
							int month = Integer.parseInt(parts[1]);
							int day = Integer.parseInt(parts[2]);

							LocalDate date = LocalDate.of(year, month, day); // 妥当性チェック
							birthday = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")); // ここでStringに整形しなおす
							break;
						} catch (DateTimeException | NumberFormatException e) {
							System.out.println("正しい日付を入力してください（例：2000/01/01）");
						}
					} while (true);

					int deptId = -1;
					do {
						System.out.print("部署ID(1:営業部、2:経理部、3:総務部):");
						String input = br.readLine();
						try {
							deptId = Integer.parseInt(input);
							if (deptId >= 1 && deptId <= 3) {
								break; // 正常値ならループを抜ける
							} else {
								System.out.println("1以上3以下の整数を入力してください。");
							}
						} catch (NumberFormatException e) {
							System.out.println("エラー：整数として認識できません。再度入力してください。");
						}
					} while (true);

					DBController.insert(empName, gender, birthday, String.valueOf(deptId));
					break;

				case 3:
					System.out.println("社員を更新します。");

					String oldName;
					String update_empId = null;
					String newName = null;

					// 社員IDを取得して現在の社員名を確認
					do {
						System.out.print("社員IDを入力してください: ");
						String input = br.readLine();

						try {
							// 半角数字として整数化できるかチェック
							int empIdNumber = Integer.parseInt(input);
							update_empId = String.valueOf(empIdNumber); // 必要に応じて文字列に戻す

							// 社員名取得
							oldName = DBController.findNameById(update_empId);
							if (oldName == null || oldName.isEmpty()) {
								System.out.println("更新する社員の社員IDを入力してください。");
							} else {
								System.out.println("社員名：" + oldName);
								break;
							}

						} catch (NumberFormatException e) {
							System.out.println("社員IDは半角数字で入力してください。");
						}

					} while (true);

					// 新しい社員名の入力
					do {
						System.out.print("新しい社員名を入力してください: ");
						newName = br.readLine();

						//						if (newName == null || newName.trim().isEmpty()) {
						//							System.out.println("社員名は空欄にできません。もう一度入力してください。");
						//							continue;
						//						}
						if (newName == null || newName.trim().isEmpty()) {

							break;
						}

						if (newName.matches("\\d+")) {
							System.out.println("社員名には数字以外を含めてください。");
							continue;
						}

						break;
					} while (true);

					// 性別チェック付き
					String update_gender = null;
					int updateGenderInput;

					do {
						System.out.print("性別を入力してください（1:男性、2:女性:）");
						String genderInputStr = br.readLine();

						// 空欄ならスキップして終了
						if (genderInputStr == null || genderInputStr.trim().isEmpty()) {

							break;
						}

						try {
							updateGenderInput = Integer.parseInt(genderInputStr);
							if (updateGenderInput == 1 || updateGenderInput == 2) {
								update_gender = String.valueOf(updateGenderInput);
								break;
							} else {
								System.out.println("1 または 2 を入力してください。");
							}
						} catch (NumberFormatException e) {
							System.out.println("エラー：半角数字で入力してください。");
						}
					} while (true);
					;

					// 生年月日チェック付き
					String update_birthday;
					do {
						System.out.print("生年月日を入力してください（西暦年/月/日): ");
						update_birthday = br.readLine();

						if (update_birthday == null || update_birthday.trim().isEmpty()) {
							//							System.out.println("空欄ではなく、正しい日付形式で入力してください（例：2000/01/01）");
							//							continue;
							break;
						}

						String[] parts = update_birthday.trim().split("/");
						if (parts.length != 3) {
							System.out.println("形式エラー：yyyy/MM/dd で入力してください（例：2000/01/01）");
							continue;
						}

						try {
							int year = Integer.parseInt(parts[0]);
							int month = Integer.parseInt(parts[1]);
							int day = Integer.parseInt(parts[2]);
							LocalDate date = LocalDate.of(year, month, day);
							update_birthday = date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
							break;
						} catch (DateTimeException | NumberFormatException e) {
							System.out.println("正しい日付を入力してください（例：2000/01/01）");
						}
					} while (true);

					// 部署IDチェック付き
					String update_deptId = null;

					do {
						System.out.print("部署IDを入力してください（1:営業部、2:経理部、3:総務部）: ");
						String input = br.readLine();

						// 入力が空欄ならスキップ
						if (input == null || input.trim().isEmpty()) {

							break;
						}

						try {
							int deptIdInput = Integer.parseInt(input);
							if (deptIdInput >= 1 && deptIdInput <= 3) {
								update_deptId = String.valueOf(deptIdInput);
								break;
							} else {
								System.out.println("1以上3以下の整数を入力してください。");
							}
						} catch (NumberFormatException e) {
							System.out.println("エラー：整数として認識できません。再度入力してください。");
						}
					} while (true);

					// 更新処理呼び出し
					DBController.update(newName, update_gender, update_birthday, update_deptId, update_empId);

					break;
				case 4:
					// 削除
					System.out.print("削除する社員IDを入力してください:");
					String empId = br.readLine();
					DBController.delete(empId);
					break;
				
				case 5:
					String searchName;
					do {
						System.out.print("検索する社員名を入力してください: ");
						searchName = br.readLine();

						// 空欄チェック
						if (searchName == null || searchName.trim().isEmpty()) {
							System.out.println("社員名を入力してください:");
							continue;
						}

						// 数字だけの入力チェック（例："1234" など）
						if (searchName.matches("\\d+")) {
							System.out.println("社員名には数字のみではなく、名前を正しく入力してください。");
							continue;
						}

						// 条件OKなら検索処理に進む
						break;
					} while (true);

					DBController.searchByName(searchName);
					break;

				case 6:
					System.out.print("部署ID(1：営業部、2：経理部、3：総務部)を入力してください:");
					String searchID = br.readLine();
					DBController.searchById(searchID);
					break;
				case 7:
				    System.out.println("システムを終了します。");
				    System.exit(0); // 強制終了してループを抜ける
				    break;

				}
			} while (menuNo != 5);
		} catch (Exception e) {
			System.out.println("システムエラーが発生しました");
			e.printStackTrace();
		}
		System.out.println("システムを終了します。");
	}

}
