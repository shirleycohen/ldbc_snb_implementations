package com.ldbc.impls.workloads.ldbc.snb.jdbc;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.ldbc.driver.DbException;
import com.ldbc.driver.Operation;
import com.ldbc.driver.OperationHandler;
import com.ldbc.driver.ResultReporter;
import com.ldbc.driver.workloads.ldbc.snb.interactive.LdbcNoResult;

public abstract class JdbcMultipleUpdateOperationHandler<OperationType extends Operation<LdbcNoResult>, OperationResult, QueryStore> 
implements OperationHandler<OperationType, JdbcDbConnectionStore<QueryStore>> {

@Override
public void executeOperation(OperationType operation, JdbcDbConnectionStore<QueryStore> state,
		ResultReporter resultReporter) throws DbException {
	Connection conn = state.getConnection();
	String query="";
	try {
		List<String> queryStrings = getQueryString(state, operation);
		for (String queryString : queryStrings) {
			query=queryString;
			Statement stmt = conn.createStatement();
			state.logQuery(operation.getClass().getSimpleName(), queryString);
			stmt.execute(queryString);
			stmt.close();
		}
	} catch (BatchUpdateException e) {
		System.out.println(e.getNextException().getNextException());
		throw new RuntimeException(query+"::",e.getNextException());
	} catch (SQLException e) {
		System.out.println(query+"::"+e);
		throw new RuntimeException(e);
	} catch (Exception e) {
		throw new RuntimeException(query+"::",e);
	} finally {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		resultReporter.report(0, LdbcNoResult.INSTANCE, operation);
	}
}

public abstract List<String> getQueryString(JdbcDbConnectionStore<QueryStore> state, OperationType operation);
}