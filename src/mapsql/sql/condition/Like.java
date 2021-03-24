package mapsql.sql.condition;

import java.util.Map;

import mapsql.sql.core.Field;
import mapsql.sql.core.SQLException;
import mapsql.sql.core.TableDescription;
import mapsql.sql.field.CHARACTER;

public class Like extends AbstractCondition {
	private String column;
	private String value;
	
	public Like(String column, String value) {
		this.column = column;
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean evaluate(TableDescription description, Map<String, String> data) throws SQLException {
		Field field = description.findField(column);
		String query = data.get(column);
		if (value.startsWith("%") && !value.endsWith("%")) {
			return query.endsWith(value.substring(1));
		} else if (value.endsWith("%") && !value.startsWith("%")) {
			return query.startsWith(value.substring(0, value.length()-1));
		} else if (value.contains("%")){
			return query.contains(value.split("%")[1]);
		}
		return false;
	}
}
