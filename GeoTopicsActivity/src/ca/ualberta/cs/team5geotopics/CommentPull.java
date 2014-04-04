package ca.ualberta.cs.team5geotopics;


import io.searchbox.client.JestClient;
import com.google.gson.Gson;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import android.util.Log;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class CommentPull {
	private JestClient client;
	private Gson gson;
	private JestResult lastResult;
	private CommentModel commentModel;

	public JestClient getClient() {
		return client;
	}

	public void setClient(JestClient client) {
		this.client = client;
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}

	public JestResult getLastResult() {
		return lastResult;
	}

	public void setLastResult(JestResult lastResult) {
		this.lastResult = lastResult;
	}

	public CommentModel getCommentModel() {
		return commentModel;
	}

	public void setCommentModel(CommentModel commentModel) {
		this.commentModel = commentModel;
	}

	/**
	 * This method will pull a single comment and set the commentModel field of this CommentSearch object to be equal to the 0th element in an array list of the sources of the hits. If the number of hits is greater than one, then the commentModel field is not updated.
	 * @param esID  the elastic search id of the comment
	 * @param index  the index in which the comment is found
	 * @return
	 */
	public Thread pullComment(final String esID, final String index,
			final String query) {
		
		Thread thread = new Thread() {
			public void run() {
				final Search search = (Search) new Search.Builder(query)
						.addIndex(index).build();
				try {
					lastResult = client.execute(search);
					Log.w("CommentSearch",
							"result json string = "
									+ lastResult.getJsonString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				client.shutdownClient();
				Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<CommentModel>>() {
				}.getType();
				String lastResultJsonString = lastResult.getJsonString();
				final ElasticSearchSearchResponse<CommentModel> esResponse = gson
						.fromJson(lastResultJsonString,
								elasticSearchSearchResponseType);
				final ArrayList<CommentModel> acm = new ArrayList<CommentModel>();
				acm.addAll((ArrayList<CommentModel>) esResponse.getSources());
				if (acm.size() == 1) {
					commentModel = acm.get(0);
				}
			}
		};
		thread.start();
		return thread;
	}

	/**
	 * Pulls the data from ElasticSearchResponse and sets it up for sorting on a new thread.
	 * @param topLevelActivity  An instance of the activity showing top-level comments.
	 * @param queryDSL  The query to be searched.
	 * @param index  The index of the comment.
	 * @param commentID  The ID of the comment.
	 * @return  thread The thread which the search will be ran on.
	 */
	public Thread pull(final BrowseActivity topLevelActivity,
			final String queryDSL, final String index, final String commentID,
			final CommentListModel browseModel, final Cache mCache) {
		Thread thread = new Thread() {
			public void run() {
				final Search search = (Search) new Search.Builder(queryDSL)
						.addIndex(index).build();
				try {
					lastResult = client.execute(search);
					Log.w("CommentSearch",
							"result json string = "
									+ lastResult.getJsonString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				client.shutdownClient();
				Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<CommentModel>>() {
				}.getType();
				String lastResultJsonString = lastResult.getJsonString();
				final ElasticSearchSearchResponse<CommentModel> esResponse = gson
						.fromJson(lastResultJsonString,
								elasticSearchSearchResponseType);
				try {
					Log.w("CommentSearch", "we have this many responses: "
							+ Integer.valueOf(esResponse.getSources().size())
									.toString());
				} catch (NullPointerException e) {
					e.printStackTrace();
					Log.w("CommentSearch", "the hits are null");
				}
				Runnable updateModel = new Runnable() {
					@Override
					public void run() {
						try {
							ArrayList<CommentModel> acm = new ArrayList<CommentModel>();
							acm.addAll((ArrayList<CommentModel>) esResponse
									.getSources());
							browseModel.addNew(acm);
							Log.w("Cache", "4!");
							mCache.updateCache(acm, commentID);
						} catch (NullPointerException e) {
							Log.w("CommentSearch", "new comments are null");
						}
					}
				};
				topLevelActivity.runOnUiThread(updateModel);
			}
		};
		thread.start();
		return thread;
	}
}