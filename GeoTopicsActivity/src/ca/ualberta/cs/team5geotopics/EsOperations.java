package ca.ualberta.cs.team5geotopics;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import com.google.gson.Gson;
import android.content.Context;


public class EsOperations {
	//http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/indices-create-index.html
	//http://www.elasticsearch.org/guide/en/elasticsearch/client/java-api/current/index_.html
	/**
	 * This method retrieves the singleton elasticsearch client from GeoTopicsApplication
	 * in order to index the comment.
	 * 
	 * @param comment 	the comment to be idexed on the server
	 * @param args 		an array of string containing the information need to prepare the Index
	 * @return			returns an IndexResponse that contains information pertaining to putting the comment on server.
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
	
	//http://karussell.wordpress.com/2011/02/07/get-started-with-elasticsearch/
	/**
	 * This method creates a new index on the cluster specified in GeoTopicsApplication
	 * 
	 * @param indexName the name of the index to create
	 * @return returnString returns a string that contains any exceptions
	 */
	
	public static String createIndex(String indexName){
		String returnString = null;
		Client client = GeoTopicsApplication.getClient();
		
		try{
			client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet();
		}
		catch(Exception e){
			returnString = e.toString();
		}
		
		return returnString;
	}
}



