package mapsql.sql.command;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import mapsql.shell.core.SQLVisitor;
import mapsql.shell.parser.MapSQL;
import mapsql.shell.parser.ParseException;
import mapsql.shell.parser.SimpleNode;
import mapsql.sql.core.SQLCommand;
import mapsql.sql.core.SQLException;
import mapsql.sql.core.SQLManager;
import mapsql.sql.core.SQLOperation;
import mapsql.util.List;

public class Sources implements SQLCommand {
	private String filename;
	
	public Sources(String filename) {
		this.filename = filename;
	}
	
	@Override
	public String execute(SQLManager manager) throws SQLException {
		String errReturn = "Error while attempting to parse " + this.filename;
		try {
			Scanner scanner = new Scanner(new File(this.filename));
			while (scanner.hasNextLine()) {
				String sql = scanner.nextLine();
				
				try {
					SimpleNode n = new MapSQL(new ByteArrayInputStream(sql.getBytes())).Start();

					List<SQLOperation> operations = (List<SQLOperation>) n.jjtAccept(new SQLVisitor(), null);
					for (SQLOperation operation : operations) {
						System.out.println(manager.execute(operation));
					}
				} catch (ParseException e) {
					System.out.println(e.getMessage());
					return errReturn;
				} catch (SQLException e) {
					System.out.println(e.getMessage());
					return errReturn;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return errReturn;
		}
		
		return this.filename + " parsed successfully";
	}
}
