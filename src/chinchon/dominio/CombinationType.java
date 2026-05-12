package chinchon.dominio;

public enum CombinationType {
	LADDER("Escalera"),TRIPLE("Triple"),CHINCHON("Chinchón");
	
	private String name; 
	
	CombinationType(String name){
		this.name=name;
	}
	
	public String getName() {
		return name; 
	}
}
