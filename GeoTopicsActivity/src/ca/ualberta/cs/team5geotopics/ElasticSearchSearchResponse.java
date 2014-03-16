package ca.ualberta.cs.team5geotopics;

import java.util.ArrayList;
import java.util.Collection;
//taken from the following URL. Author chenlei
//https://github.com/slmyers/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/Hits.java

/**
 * Responsible for getting the hits to a reference to a comment 
 * and gets the source of each comment from the CommentListController.
 */
public class ElasticSearchSearchResponse<T> {
	int took;
    boolean timed_out;
    transient Object _shards;
    Hits<T> hits;
    boolean exists;    
    
    /**
     * Gets the Hits value of hits from a ElasticSearch Collection.
     * @return hits.getHits The hits from an ElasticSearch Collection.
     */
    public Collection<ElasticSearchResponse<T>> getHits() {
        return hits.getHits();        
    }
    
    /**
     * Gets the sources from ElasticSearch. Sources being the raw data.
     * @return out A Collection of a new ArrayList.
     */
    public Collection<T> getSources() {
        Collection<T> out = new ArrayList<T>();
        for (ElasticSearchResponse<T> essrt : getHits()) {
            out.add( essrt.getSource() );
        }
        return out;
    }
    
    /**
     * Creates and returns a string containing data about the ElasticSearch response.
     * @return (super.toString() + ":" + took + "," + _shards + "," + exists + ","  + hits) The string containing information about the ElasticSearch response.
     */
    public String toString() {
        return (super.toString() + ":" + took + "," + _shards + "," + exists + ","  + hits);     
    }
}
