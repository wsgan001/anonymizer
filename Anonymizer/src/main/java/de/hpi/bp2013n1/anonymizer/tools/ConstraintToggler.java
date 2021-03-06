package de.hpi.bp2013n1.anonymizer.tools;

/*
 * #%L
 * Anonymizer
 * %%
 * Copyright (C) 2013 - 2014 HPI Bachelor's Project N1 2013
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import de.hpi.bp2013n1.anonymizer.Constraint;
import de.hpi.bp2013n1.anonymizer.db.TableField;
import de.hpi.bp2013n1.anonymizer.shared.Config;
import de.hpi.bp2013n1.anonymizer.shared.Rule;
import de.hpi.bp2013n1.anonymizer.shared.Scope;
import de.hpi.bp2013n1.anonymizer.util.SQLHelper;

public class ConstraintToggler {

	private static Logger logger = Logger.getLogger(ConstraintToggler.class
			.getName());

	public ConstraintToggler() {
	}

	public static Set<Constraint> disableConstraints(Connection connection,
			Config config, Scope scope) {
		boolean supportsDisableAllForeignKeys = ConstraintToggler
				.databaseSupportsDisableAllForeignKeys(connection);
		if (supportsDisableAllForeignKeys) {
			try (Statement disableStatement = connection.createStatement()) {
				try {
					disableStatement.execute(SQLHelper
							.disableAllForeignKeys(connection));
				} catch (SQLException e) {
					logger.warning(String.format(
							"Failed to disable foreign key constraints: %s",
							e.getMessage()));
				}
			} catch (SQLException e) {
				logger.warning(String.format(
						"An error occured about disabling a constraint: %s",
						e.getMessage()));
			}
			return null;
		} else {
			Set<Constraint> constraints = ConstraintToggler.findConstraints(
					connection, config, scope);
			ConstraintToggler.disableConstraintsSeparately(constraints,
					connection);
			return constraints;
		}
	}

	private static void disableConstraintsSeparately(
			Set<Constraint> constraints, Connection connection) {
		logger.info("Disabling " + constraints.size() + " constraints.");
		try (Statement disableStatement = connection.createStatement()) {
			for (Constraint constraint : constraints) {
				String qualifiedTableName = SQLHelper.qualifiedTableName(
						constraint.schemaName, constraint.tableName);
				System.out.println("Disabling constraint "
						+ constraint.constraintName + " on "
						+ qualifiedTableName + "\r");
				try {
					disableStatement.execute(SQLHelper.disableForeignKey(
							connection, qualifiedTableName,
							constraint.constraintName));
				} catch (SQLException e) {
					logger.warning(String.format(
							"Could not disable constraint %s on %s: %s",
							constraint.constraintName, qualifiedTableName,
							e.getMessage()));
				}
			}
		} catch (SQLException e) {
			logger.warning("Failed to close Statement used to disable constraints.");
		}
		logger.info("Finished: Disabling " + constraints.size()
				+ " constraints.");
	}

	public static Set<Constraint> findConstraints(Connection connection,
			Config config, Scope scope) {
		ConstraintNameFinder finder = new ConstraintNameFinder(connection);
		Set<Constraint> constraints = new HashSet<Constraint>();
		Set<String> alreadyDoneTables = new HashSet<String>();

		for (Rule rule : config.rules) {
			TableField ruleTableField = rule.getTableField();
			if (!alreadyDoneTables.contains(ruleTableField.table)) {
				constraints.addAll(finder
						.findConstraintNames(ruleTableField.table));
				alreadyDoneTables.add(ruleTableField.table);
			}
			for (TableField tableField : rule.getDependants()) {
				if (!alreadyDoneTables.contains(tableField.table)) {
					constraints.addAll(finder
							.findConstraintNames(tableField.table));
					alreadyDoneTables.add(tableField.table);
				}
			}
		}

		for (String tableName : scope.tables) {
			if (!alreadyDoneTables.contains(tableName)) {
				constraints.addAll(finder.findConstraintNames(tableName));
				alreadyDoneTables.add(tableName);
			}
		}

		return constraints;
	}

	public static boolean databaseSupportsDisableAllForeignKeys(
			Connection connection) {
		try {
			return SQLHelper.supportsDisableAllForeignKeys(connection);
		} catch (SQLException e) {
			logger.warning(String.format(
					"Could not determine whether database supports disabling "
							+ "all foreign key constraints at once: %s",
					e.getMessage()));
			return false;
		}
	}

	public static void enableConstraints(Collection<Constraint> constraints,
			Connection connection) {
		boolean supportsDisableAllForeignKeys = ConstraintToggler
				.databaseSupportsDisableAllForeignKeys(connection);
		if (supportsDisableAllForeignKeys) {
			try (Statement enableStatement = connection.createStatement()) {
				try {
					enableStatement.execute(SQLHelper
							.enableAllForeignKeys(connection));
				} catch (SQLException e) {
					logger.warning(String.format(
							"Failed to enable foreign key constraints: %s",
							e.getMessage()));
				}
			} catch (SQLException e) {
				logger.warning(String.format(
						"An error occured about enabling a constraint: %s",
						e.getMessage()));
			}
		} else {
			ConstraintToggler.enableConstraintsSeparately(constraints,
					connection);
		}
	}

	private static void enableConstraintsSeparately(
			Collection<Constraint> constraints, Connection connection) {
		logger.info("Enabling " + constraints.size() + " constraints.");
		try (Statement disableStatement = connection.createStatement()) {
			for (Constraint constraint : constraints) {
				String qualifiedTableName = SQLHelper.qualifiedTableName(
						constraint.schemaName, constraint.tableName);
				System.out.println("Enabling constraint "
						+ constraint.constraintName + " on "
						+ qualifiedTableName + "\r");
				try {
					disableStatement.execute(SQLHelper.enableForeignKey(
							connection, qualifiedTableName,
							constraint.constraintName));
				} catch (SQLException e) {
					logger.warning(String.format(
							"Could not enable constraint %s on %s: %s",
							constraint.constraintName, qualifiedTableName,
							e.getMessage()));
				}
			}
		} catch (SQLException e) {
			logger.warning("Failed to close Statement used to enable constraints.");
		}
		logger.info("Finished: Enabling constraints.");
	}

}
