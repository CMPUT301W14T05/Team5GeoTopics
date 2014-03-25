package ca.ualberta.cs.team5geotopics;

public class queryDSLFactory {
	private String sortType;
	
	
	
	public queryDSLFactory(String sort) {
		this.sortType = sort;
	}






	public void setSort(String sort) {
		this.sortType = sort;
	}



	public String createDSL(){
		if(sortType.equals("time")){
			return 	"\"sort\" : [\n" +
					"{\"epochTime\" : {\"order\" : \"asc\", \"mode\" : \"avg\"}}\n" +
					"],\n";
		}
		
		
		return null;
	}
	
	
}
