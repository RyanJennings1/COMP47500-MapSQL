package mapsql.sql.statement;

import java.util.Map;

import mapsql.sql.condition.Equals;
import mapsql.sql.core.Condition;
import mapsql.sql.core.Row;
import mapsql.sql.core.SQLException;
import mapsql.sql.core.SQLResult;
import mapsql.sql.core.SQLStatement;
import mapsql.sql.core.Table;
import mapsql.sql.core.TableDescription;
import mapsql.util.List;

public class DropTable implements SQLStatement {
	private String name;
	
	public DropTable(String name) {
		this.name = name;
	}
	
	@Override
	public SQLResult execute(Map<String, Table> tables) throws SQLException {
		if (this.name.equals("mapsql.tables")) {
			throw new SQLException("Table 'mapsql.tables' cannot be modified");
		}

		if (tables.get(this.name) == null) {
			throw new SQLException("Table not found");
		}

		tables.remove(this.name);
		// Remove the row recording the table in the mapsql.tables table
		tables.get("mapsql.tables").delete(new Equals("table", this.name));

		return new SQLResult() {
			public String toString() {
				return "OK";
			}

			@Override
			public TableDescription description() {
				return null;
			}

			@Override
			public List<Row> rows() {
				return null;
			}
		};
	}

}
