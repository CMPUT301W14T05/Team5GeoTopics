package ca.ualberta.cs.team5geotopics;
// taken from the following url. Author: chenlei
//https://github.com/slmyers/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/ElasticSearchResponse.java

/**
 * The ElasticSearchResponse helps in getting the correct source data for each comment. 
 */

public class ElasticSearchResponse<T> {
	String _index;
    String _type;
    String _id;
    int _version;
    boolean exists;
    T _source;
    double max_score;
    public T getSource() {
        return _source;
    }
}
