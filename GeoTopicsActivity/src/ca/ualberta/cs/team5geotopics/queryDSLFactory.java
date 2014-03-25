package ca.ualberta.cs.team5geotopics;

public class queryDSLFactory {
	private String sortType;
	private String filterType;
	
	
	public queryDSLFactory() {
		
	}

	public void setSort(String sort) {
		this.sortType = sort;
	}
	
	public void setFilter(String filter) {
		this.filterType = filter;
	}

	public String createMatchAll(){
		return "{\n" +
	  			"\"query\": {\n" +
	  			"\"match_all\": {}\n" +
	  			"}\n" +
	  			"}";
	}

	public String createSortDSL(){
		if(sortType.equals("time")){
			return 	"\"sort\" : [\n" +
					"{\"epochTime\" : {\"order\" : \"asc\", \"mode\" : \"avg\"}}\n" +
					"],\n";
		}
		return "ERROR";
	}
	
	public String createFilterDSL(String specifier){
		if(filterType.equals("location")){
			return "\"filter\" : {\n" +
					"\"geo_distance\" : {\n" +
					"\"distance\" : \"12km\",\n" +
					"\"pin.location\" : \"" + specifier + "\"\n" +
					"}\n" +
					"}\n";
		}
		if(filterType.equals("replies")){
			return  "{\n" + 					
					"\"filter\": {\n" + 
					"\"type\":{\n" + 
					"\"value\" : \"" + specifier +  "\"\n"  + 
					"}\n" +
					"}\n" +
					"}";
		}
		
		return "ERROR";
	}
	

}
