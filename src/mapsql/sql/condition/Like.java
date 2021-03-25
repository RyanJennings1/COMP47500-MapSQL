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
		String query = data.get(column);
		/**
		 * If the query begins with % but doesn't end with it then it is in
		 * the form '%....'
		 * If the query ends with % but doesn't start with it then it is in the 
		 * form '....%'
		 * Otherwise if it contains % but failed the first two statements then it
		 * has to be in the form %...%
		 */
		int count_per = 0;
		for (char ch: value.toCharArray()) {
			if (ch == '%') {
				count_per++;
			}
			if (count_per > 2) {
				throw new SQLException("Incorrect use of LIKE operator");
			}
		}
		if (value.startsWith("%") && !value.endsWith("%")) {
			return query.endsWith(value.substring(1));
		} else if (value.endsWith("%") && !value.startsWith("%")) {
			return query.startsWith(value.substring(0, value.length()-1));
		} else if (value.startsWith("%") && value.endsWith("%")){
			return query.contains(value.split("%")[1]);
		}
		return false;
	}
}
