package mapsql.sql.core;

import java.util.HashMap;
import java.util.Map;

import mapsql.sql.condition.Equals;
import mapsql.util.LinkedList;
import mapsql.util.List;
import mapsql.util.Position;

public class Table {
	private TableDescription description;
	private List<Row> rows = new LinkedList<Row>();
	
	public Table(TableDescription description) {
		this.description = description;
	}
	
	public TableDescription description() {
		return description;
	}

	public List<Row> select(Condition where) throws SQLException {
		List<Row> list = new LinkedList<Row>();
		for (Row row : rows) {
			if (row.satisfies(where, description)) list.insertLast(row);
		}
		return list;
	}

	public void insert(String[] columns, String[] values) throws SQLException {
		Map<String, String> data = new HashMap<String, String>();
		
		for (int i=0; i < columns.length; i++) {
			Field field = description.findField(columns[i]);
			if (field.isUnique()) {
				if (!select(new Equals(columns[i], values[i])).isEmpty()) {
					throw new SQLException("Column '" + columns[i] + "' is UNIQUE - a row with '" + values[i] + "' already exists");
				}
			}
			data.put(columns[i], field.validate(values[i])); 
		}
		
		Field[] fields = description.fields();
		for (int i=0; i < fields.length; i++) {
			if (!data.containsKey(fields[i].name())) {
				String val = fields[i].defaultValue();

				if (fields[i].isUnique()) {
					if (!select(new Equals(fields[i].name(), val)).isEmpty()) {
						throw new SQLException("Column '" + fields[i].name() + "' is UNIQUE - a row with '" + val + "' already exists");
					}
				}
				data.put(fields[i].name(), val);
			}
		}
		rows.insertLast(new Row(data));
	}

	/**
	 * @description: Find the rows that match and then for each column, value pair given update the row
	 * @param columns: Column names on table
	 * @param values: Values to update for column names
	 * @param where: Condition to find matching rows
	 * @throws SQLException
	 */
	public void update(String[] columns, String[] values, Condition where) throws SQLException {
		for (Row row: this.rows) {
			if (row.satisfies(where, this.description)) { 
				for (int i = 0; i < columns.length; i++) {
					row.data.put(columns[i], values[i]);
				}
			}
		}
	}

	/**
	 * @description: Iterate through linked list and if row found, remove it 
	 * @param where: Condition to find matching rows
	 * @throws SQLException
	 */
	public void delete(Condition where) throws SQLException {
		Position<Row> current = this.rows.first();
		for (Row row: rows) {
			if (row.satisfies(where, description)) {
				rows.remove(current);
				break;
			}
			current = this.rows.next(current);
		}
	}
}
