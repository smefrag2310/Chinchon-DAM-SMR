package chinchon.dominio;

/**
 * Represents the numeric values of the Spanish deck used in the Chinchón game.
 * <p>
 * Each value stores:
 * <ul>
 *   <li>{@code value}: the actual numeric value of the card (1, 2, 3, ..., 12)</li>
 *   <li>{@code order}: the sequential order used for detecting runs (1–10)</li>
 * </ul>
 * The order is necessary because the Spanish deck omits 8 and 9, so the
 * sequence jumps from 7 → 10 → 11 → 12. The {@code order} field normalizes
 * this progression for ladder detection.
 */

public enum Value {
	ONE(1,1),TWO(2,2),THREE(3,3),FOUR(4,4),FIVE(5,5),SIX(6,6),SEVEN(7,7),JACK(10,8),HORSE(11,9),KING(12,10);
	
	private int value;
	private int order;
	
	/**
     * Creates a new card value with its numeric value and run order.
     *
     * @param value the actual numeric value of the card
     * @param order the sequential order used for ladder detection
     */
	
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
	
	/**
     * Returns the {@link Value} corresponding to the given order.
     * <p>
     * This is used to determine missing cards in potential ladders.
     *
     * @param order the run order to search for
     * @return the matching {@code Value}, or {@code null} if none matches
     */
	
	public static Value fromOrder(int order) {
		
		for(Value v: Value.values()) {
			if(v.getOrder() == order) {
				return v;
			}
		}
		return null;
		
	}
}
