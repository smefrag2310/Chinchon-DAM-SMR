package chinchon.dominio;

import java.util.Arrays;

public enum Value {
	UNO(1,1),DOS(2,2),TRES(3,3),CUATRO(4,4),CINCO(5,5),SEIS(6,6),SIETE(7,7),DIEZ(10,8),ONCE(11,9),DOCE(12,10);
	
	private int value;
	private int order;
	
	Value(int value,int order){
		this.value=value;
		this.order=order;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getOrder() {
		return order;
	}
	
	public static Value fromOrder(int order) {
		
		for(Value v: Value.values()) {
			if(v.getOrder() == order) {
				return v;
			}
		}
		return null;
		
	}
}
