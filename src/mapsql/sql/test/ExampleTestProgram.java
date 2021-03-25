package mapsql.sql.test;

import mapsql.sql.condition.Equals;
import mapsql.sql.condition.GreaterThan;
import mapsql.sql.condition.GreaterThanOrEqual;
import mapsql.sql.condition.LessThan;
import mapsql.sql.condition.LessThanOrEqual;
import mapsql.sql.condition.Like;
import mapsql.sql.condition.OrCondition;
import mapsql.sql.core.Condition;
import mapsql.sql.core.Field;
import mapsql.sql.core.SQLException;
import mapsql.sql.core.SQLManager;
import mapsql.sql.core.SQLResult;
import mapsql.sql.core.SQLStatement;
import mapsql.sql.field.CHARACTER;
import mapsql.sql.field.INTEGER;
import mapsql.sql.statement.CreateTable;
import mapsql.sql.statement.Delete;
import mapsql.sql.statement.DropTable;
import mapsql.sql.statement.Insert;
import mapsql.sql.statement.Select;
import mapsql.sql.statement.Update;

import mapsql.sql.core.Row;

public class ExampleTestProgram {
	static SQLManager manager = new SQLManager();
	
	public static void main(String[] args) {
		createTableStatement();

		showTables();
		insertData();
		selectTable();

		insertPartialData();
		selectTable();
		
		updateData();
		updateOrData();
		selectTable();

		deleteData();
		selectTable();
		
		dropTable();
		showTables();

		testLessThan();
		testLessThanOrEqual();
		testGreaterThan();
		testGreaterThanOrEqual();

		testLikeStartOfString();
		testLikeEndOfString();
		testLikeBothSidesOfString();

		testUpdate();

		testCheckNotNull();
	}

	private static void executeStatement(SQLStatement statement) {
		try {
			SQLResult result = manager.execute(statement);
			System.out.println(result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createTableStatement() {
		executeStatement(
				new CreateTable(
						"contact", 
						new Field[] {
								new INTEGER("id", true, false, true), 
								new CHARACTER("name", 30, false, true), 
								new CHARACTER("email", 30, false, false)
						}
				)
		);
	}
	
	public static void showTables() {
		executeStatement(new Select("mapsql.tables", new String[] { "*" }));
	}
	
	public static void selectTable() {
		executeStatement(new Select("contact", new String[] { "*" }));
	}
	
	public static void dropTable() {
		executeStatement(new DropTable("contact"));
	}
	
	public static void insertData() {
		executeStatement(
				new Insert(
						"contact", 
						new String[] {"name", "email"}, 
						new String[] {"Rem", "rem.collier@ucd.ie"}
				)
		);
	}

	public static void insertPartialData() {
		executeStatement(new Insert("contact", new String[] {"name"}, new String[] {"Henry"}));
	}

	public static void updateData() {
		executeStatement(
				new Update(
						"contact", 
						new String[] {"email"}, 
						new String[] {"henry.mcloughlin@ucd.ie"}, 
						new Equals("id", "2")
				)
		);
	}
	
	public static void updateOrData() {
		executeStatement(
				new Update(
						"contact", 
						new String[] {"email"}, 
						new String[] {"henry.mcloughlin@ucd.ie"}, 
						new OrCondition(
								new Equals("id", "1"), 
								new Equals("id", "2")
						)
				)
		);
	}
	
	public static void deleteData() {
		executeStatement(new Delete("contact", new Equals("id", "2")));
	}
	
	public static void selectTableWithColumns() {
		executeStatement(new Select("contact", new String[] { "id", "name" }));
	}

	public static void testLessThan() {
		before();
		// Check select returns 1 of 2 contacts
		try {
			SQLResult result = manager.execute(
					new Select(
						"contacts",
						new String[] { "id", "name" },
						new LessThan("id", "2")
					)
			);
			System.out.println(result);
			assertResult("testLessThan: Expected: %b, Actual: %b\n", true, result.rows().size() == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		after();
	}

	public static void testLessThanOrEqual() {
		before();
		// Check select returns 2 of 3 contacts
		try {
			SQLResult result = manager.execute(
					new Select(
						"contacts",
						new String[] { "id", "name" },
						new LessThanOrEqual("id", "2")
					)
			);
			System.out.println(result);
			assertResult("testLessThanOrEqual: Expected: %b, Actual: %b\n", true, result.rows().size() == 2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		after();
	}

	public static void testGreaterThan() {
		before();
		// Check select returns 2 of 3 contacts
		try {
			SQLResult result = manager.execute(
					new Select(
						"contacts",
						new String[] { "id", "name" },
						new GreaterThan("id", "2")
					)
			);
			System.out.println(result);
			assertResult("testGreaterThan: Expected: %b, Actual: %b\n", true, result.rows().size() == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		after();
	}

	public static void testGreaterThanOrEqual() {
		before();
		// Check select returns 2 of 3 contacts
		try {
			SQLResult result = manager.execute(
					new Select(
						"contacts",
						new String[] { "id", "name" },
						new GreaterThanOrEqual("id", "2")
					)
			);
			System.out.println(result);
			assertResult("testGreaterThanOrEqual: Expected: %b, Actual: %b\n", true, result.rows().size() == 2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		after();
	}

	public static void testLikeStartOfString() {
		before();
		try {
			SQLResult result = manager.execute(
					new Select(
						"contacts",
						new String[] { "id", "name" },
						new Like("name", "%hn")
					)
			);
			System.out.println(result);
			assertResult("testLikeStartOfString: Expected: %b, Actual: %b\n", true, result.rows().size() == 1);
			//assertResult("testLikeStartOfString: Expected: %s, Actual: %s\n", "John", result.rows().get("name") == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		after();
	}

	public static void testLikeEndOfString() {
		before();
		try {
			SQLResult result = manager.execute(
					new Select(
						"contacts",
						new String[] { "id", "name" },
						new Like("name", "R%")
					)
			);
			System.out.println(result);
			assertResult("testLikeEndOfString: Expected: %b, Actual: %b\n", true, result.rows().size() == 1);
			//assertResult("testLikeStartOfString: Expected: %s, Actual: %s\n", "John", result.rows().get("name") == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		after();
	}

	public static void testLikeBothSidesOfString() {
		before();
		try {
			SQLResult result = manager.execute(
					new Select(
						"contacts",
						new String[] { "id", "name" },
						new Like("name", "%o%")
					)
			);
			System.out.println(result);
			assertResult("testLikeEndOfString: Expected: %b, Actual: %b\n", true, result.rows().size() == 1);
			//assertResult("testLikeBothSidesOfString: Expected: %s, Actual: %s\n", "John", result.rows().get("name") == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		after();
	}

	public static void testUpdate() {
		before();
		try {
			executeStatement(
					new Update(
							"contacts", 
							new String[] {"email"}, 
							new String[] {"henry.mcloughlin@ucd.ie"}, 
							new Equals("id", "2")
					)
			);
			SQLResult selectResult = manager.execute(
					new Select(
							"contacts", 
							new String[] {"id", "email"},
							new Equals("id", "2")
					)
			);
			assertResult("testUpdate rows returned: Expected: %b, Actual: %b\n", true, selectResult.rows().size() == 1);
			for (Row row: selectResult.rows()) {
				assertResult("testUpdate value updated: Expected: %b, Actual: %b\n",
						true,
						row.get("email").equals("henry.mcloughlin@ucd.ie"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		after();
	}

	public static void testCheckNotNull() {
		before();
		// Insert with not null field missing to throw an error
		//no name
		Boolean errorThrown = false;
		try {
			manager.execute(
					new Insert(
						"contacts", 
						new String[] {"email"}, 
						new String[] {"pat.burke@ucd.ie"}
					)
			);
		} catch (SQLException e) {
			errorThrown = true;
			assertResult("testCheckNotNull insert error: Expected: %b, Actual: %b\n",
					true,
					e.getMessage().equals("Missing Value for NOT NULL field 'name'"));
		}
		assertResult("testCheckNotNull error thrown: Expected: %b, Actual: %b\n", true, errorThrown);
		// Insert with fields filled no error and record created
		executeStatement(
				new Insert(
					"contacts", 
					new String[] {"name", "email"}, 
					new String[] {"Pat", "pat.burke@ucd.ie"}
				)
		);
		try {
			SQLResult selectResult = manager.execute(
					new Select(
							"contacts", 
							new String[] {"id", "name"},
							new Equals("name", "Pat")
					)
			);
			assertResult("testCheckNotNull rows returned: Expected: %b, Actual: %b\n", true, selectResult.rows().size() == 1);
			for (Row row: selectResult.rows()) {
				assertResult("testCheckNotNull name inserted: Expected: %b, Actual: %b\n",
						true,
						row.get("name").equals("Pat"));
			}
		} catch (SQLException e) {
			System.out.println(e.getStackTrace());
		}
		after();
	}

	private static void before() {
		// Create new table
		executeStatement(
				new CreateTable(
					"contacts", 
					new Field[] {
						new INTEGER("id", true, false, true), 
						new CHARACTER("name", 30, false, true), 
						new CHARACTER("email", 30, false, false)
					}
				)
		);
		// Add in three contacts
		executeStatement(
				new Insert(
						"contacts", 
						new String[] {"name", "email"}, 
						new String[] {"Rem", "rem.collier@ucd.ie"}
				)
		);
		executeStatement(
				new Insert(
						"contacts", 
						new String[] {"name", "email"}, 
						new String[] {"John", "john.murphy@ucd.ie"}
				)
		);
		executeStatement(
				new Insert(
						"contacts", 
						new String[] {"name", "email"}, 
						new String[] {"Liam", "liam.murphy@ucd.ie"}
				)
		);
	}

	private static void after() {
		// Drop table
		executeStatement(new DropTable("contacts"));
	}

	private static void assertResult(String printfMessage, Boolean expected, Boolean actual) {
		if (expected == actual) {
			System.out.printf(printfMessage, expected, actual);
		} else {
			System.err.printf(printfMessage, expected, actual);
		}
		
	}
}
