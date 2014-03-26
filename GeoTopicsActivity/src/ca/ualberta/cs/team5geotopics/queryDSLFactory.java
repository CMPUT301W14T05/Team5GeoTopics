package ca.ualberta.cs.team5geotopics;

public class queryDSLFactory {
	private String sortType;
	private String filterType;
	private String filterSpecifier;
	private int distance;
	
	public queryDSLFactory() {
		
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}

	public void setSort(String sort) {
		this.sortType = sort;
	}
	
	public void setFilter(String filter) {
		this.filterType = filter;
	}
	
	public void setFilterSpecifier(String filterSpecifier){
		this.filterSpecifier = filterSpecifier;
	}

	public String createMatchAll(){
		if(filterType != null){
			return "{\n" +
					"\"filtered\"\n" +
		  			"\"query\": {\n" +
		  			"\"match_all\": {}\n" +
		  			"}\n" +
		  			"},\n" + createFilterDSL();
		}
		if(filterType == null){
			return "{\n" +
		  			"\"query\": {\n" +
		  			"\"match_all\": {}\n" +
		  			"}\n" +
		  			"}";
		}
		return "ERROR";
	}

	public String createSortDSL(){
		if(sortType.equals("time")){
			return 	"\"sort\" : [\n" +
					"{\"epochTime\" : {\"order\" : \"asc\", \"mode\" : \"avg\"}}\n" +
					"]\n" +
					"}";
		}
		return "ERROR";
	}
	
	public String createFilterDSL(){
		if(filterSpecifier == null){
			return "Filter not specified Error";
		}
		if(filterType.equals("location")){
			if(sortType == null){
				return "\"filter\" : {\n" +
						"\"geo_distance\" : {\n" +
						"\"distance\" : \"" + Integer.valueOf(this.distance).toString() + "km\",\n" +
						"\"pin.location\" : \"" + filterSpecifier + "\"\n" +
						"}\n" +
						"}\n";
			}
			return 	"\"filter\" : {\n" +
					"\"geo_distance\" : {\n" +
					"\"distance\" : \"" + Integer.valueOf(this.distance).toString() + "km\",\n" +
					"\"pin.location\" : \"" + filterSpecifier + "\"\n" +
					"}\n" +
					"},\n" + createSortDSL();
		}
		if(filterType.equals("replies")){
			if(sortType == null){
				return  "{\n" + 					
						"\"filter\": {\n" + 
						"\"type\":{\n" + 
						"\"value\" : \"" + filterSpecifier +  "\"\n"  + 
						"}\n" +
						"}\n" +
						"}";
			}
			return 		"{\n" + 					
						"\"filter\": {\n" + 
						"\"type\":{\n" + 
						"\"value\" : \"" + filterSpecifier +  "\"\n"  + 
						"}\n" +
						"}\n" +
						"},\n" + createSortDSL();
		}
		
		return "ERROR";
	}
	

}
