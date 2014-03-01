package ca.ualberta.cs.team5geotopics;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;

import com.google.gson.Gson;


public class EsOperations {
	//http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/indices-create-index.html
	//http://www.elasticsearch.org/guide/en/elasticsearch/client/java-api/current/index_.html
	/**
	 * This method retrieves the singleton elasticsearch client from GeoTopicsApplication
	 * in order to index the comment.
	 * 
	 * @param comment 	the comment to be idexed on the server
	 * @param args 		an array of string containing the information need to prepare the Index
	 */
	public static IndexResponse putComment(String[] args, CommentModel comment){
		Gson gson = new Gson();
		String jsonComment = gson.toJson(comment);
		Client client = GeoTopicsApplication.getClient();
		//for clarity
		String index = args[0];
		String type = args[1];
		String id = args[2];
		return	client.prepareIndex(index, type, id)
					.setSource(jsonComment)
					.execute()
					.actionGet();
		}
	
}



