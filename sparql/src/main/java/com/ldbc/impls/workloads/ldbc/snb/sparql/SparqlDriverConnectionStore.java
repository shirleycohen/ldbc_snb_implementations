package com.ldbc.impls.workloads.ldbc.snb.sparql;

import com.bigdata.rdf.sail.webapp.client.RemoteRepository;
import com.bigdata.rdf.sail.webapp.client.RemoteRepositoryManager;
import com.ldbc.driver.DbConnectionState;

import java.util.Map;

public abstract class SparqlDriverConnectionStore<DbQueryStore> extends DbConnectionState {
	private DbQueryStore queryStore;
	private final boolean printNames;
	private final boolean printStrings;
	private final boolean printResults;
	private final String endpoint;
	private final RemoteRepository repository;

	public SparqlDriverConnectionStore(Map<String, String> properties, DbQueryStore store) {
		super();

		printNames = Boolean.valueOf(properties.get("printQueryNames"));
		printStrings = Boolean.valueOf(properties.get("printQueryStrings"));
		printResults = Boolean.valueOf(properties.get("printQueryResults"));
		endpoint = properties.get("endpoint");

		final RemoteRepositoryManager repo = new RemoteRepositoryManager(endpoint, false /* useLBS */);
		repository = repo.getRepositoryForDefaultNamespace();

		queryStore = store;
	}

	public final DbQueryStore getQueryStore() {
		return queryStore;
	}

	public final boolean isPrintResults() {
		return printResults;
	}

	public final void logQuery(String queryType, String query) {
		if (printNames) {
			System.out.println("########### " + queryType);
		}
		if (printStrings) {
			System.out.println(query);
		}
	}

	public RemoteRepository getRepository() {
		return repository;
	}

}
