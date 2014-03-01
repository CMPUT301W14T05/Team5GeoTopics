package ca.ualberta.cs.team5geotopics;

import android.util.Log;
import android.widget.Toast;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;




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
	public static String putComment(String[] args, String jsonComment){
		JestClient client = GeoTopicsApplication.getClient();
		//for clarity
		String index = args[0];
		String type = args[1];
		String id = args[2];
		
		Index indexToPush = new Index.Builder(jsonComment).index(index).type(type).id(id).build();
		JestResult result = null;
		try {
			result = client.execute(indexToPush);
		} catch (Exception e) {
			Log.w("putComment", e.toString());
			return e.toString();
		}
		return result.toString();
		}
	
	//http://karussell.wordpress.com/2011/02/07/get-started-with-elasticsearch/
	/**
	 * This method creates a new index on the cluster specified in GeoTopicsApplication
	 * 
	 * @param indexName the name of the index to create
	 * @return returnString returns a string that contains any exceptions
	 */
	
	public static String createIndex(String indexName){
		JestClient client = GeoTopicsApplication.getClient();
		
		Index indexToPush = new Index.Builder(indexName).build();
		JestResult result = null;
		try{
			result = client.execute(indexToPush);
		}
		catch(Exception e){
			Log.w("createIndex", e.toString());
			return e.toString();
		}
		return result.toString();
		
		
	}
}



