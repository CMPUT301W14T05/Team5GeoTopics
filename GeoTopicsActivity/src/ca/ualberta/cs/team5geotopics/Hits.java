package ca.ualberta.cs.team5geotopics;

import java.util.Collection;
// taken from the following URL. Author chenlei
//https://github.com/slmyers/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/Hits.java

/*
 * Hits returns the score and hits from an ElasticSearch response.
 */

public class Hits <T>{
	int total;
    double max_score;
    Collection<ElasticSearchResponse<T>> hits;
    public Collection<ElasticSearchResponse<T>> getHits() {
        return hits;
    }
    public String toString() {
        return (super.toString()+","+total+","+max_score+","+hits);
    }
}
