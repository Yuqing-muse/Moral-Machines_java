/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package ethicalengine;

/**
 * Define the variables and basic methods of the scene
 * @author Yuqing Chang
 *
 */
public class Scenario {
	// Attributes of a Scenario object
	private boolean isLegalCrossing;
	private boolean hasYouInCar;
	private boolean hasYouInLane;
	private Character[] passenger;
	private Character[] pedestrian;

	/**
	 * Constructor of Scenario class
	 * @param passengers {@code Character[]} is an array of passengers
	 * @param pedestrians {@code Character[]} is an array of pedestrians
	 * @param isLegalCrossing {@code boolean} is {@code true} if pedestrians are crossing road legally
	 */
	public Scenario(Character[] passengers, Character[] pedestrians, boolean isLegalCrossing) {
		this.passenger = passengers;
		this.pedestrian = pedestrians;
		this.isLegalCrossing = isLegalCrossing;
		for (int i = 0; i < passenger.length; i++) {
			if (passenger[i].getClass() == Person.class) {
				if (((Person) passenger[i]).isYou() == true) {
					hasYouInCar = true;
					hasYouInLane = false;
				} else {
					hasYouInCar = false;
				}
			}
		}
		for (int i = 0; i < pedestrian.length; i++) {
			if (pedestrian[i].getClass() == Person.class) {
				if (((Person) pedestrian[i]).isYou() == true) {
					hasYouInCar = false;
					hasYouInLane = true;
				} else {
					hasYouInLane = false;
				}
			}
		}
	}

	/**
	 * Get information about whether you're in car
	 * @return {@code boolean} is {@code true} if you are in car
	 */
	public boolean hasYouInCar() {
		return this.hasYouInCar;
	}

	/**
	 * Get information about whether you're in lane
	 * @return {@code boolean} is {@code true} if you are in lane
	 */
	public boolean hasYouInLane() {
		return this.hasYouInLane;
	}

	/**
	 * Set information about whether you're in car
	 * @param car {@code boolean} is {@code true} if you are in car
	 */
	public void setYouInCar(boolean car) {
		this.hasYouInCar = car;
	}

	/**
	 * Set information about whether you're in lane
	 * @param lane {@code boolean} is {@code true} if you are in lane
	 */
	public void setYouInLane(boolean lane) {
		this.hasYouInLane = lane;
	}

	/**
	 * Get an array of passengers
	 * @return {@code Character[]} is an array of passengers
	 */
	public Character[] getPassengers() {
		return passenger;
	}

	/**
	 * Get an array of pedestrian
	 * @return {@code Character[]} is an array of pedestrians
	 */
	public Character[] getPedestrians() {
		return pedestrian;
	}

	/**
	 * Get information about whether the person is legally crossing
	 * @return {@code boolean} is {@code true} if pedestrians are crossing road legally
	 */
	public boolean isLegalCrossing() {
		return this.isLegalCrossing;
	}

	/**
	 * Set information about whether the person is legally crossing
	 * @param isLegalCrossing {@code boolean} is {@code true} if pedestrians are crossing road legally
	 */
	public void setLegalCrossing(boolean isLegalCrossing) {
		this.isLegalCrossing = isLegalCrossing;
	}

	/**
	 * Get the number of passengers
	 * @return {@code int} the number of passengers
	 */
	public int getPassengerCount() {
		return this.passenger.length;
	}

	/**
	 * Get the number of pedestrians
	 * @return {@code int} the number of pedestrians
	 */
	public int getPedestrianCount() {
		return this.pedestrian.length;
	}

	/**
	 * Organize Scenario information into a string and return
	 * @return {@code String} to store scenario information
	 */
	public String toString() {
		String legal;
		if (this.isLegalCrossing == true)
			legal = "yes";
		else
			legal = "no";
		String out = "======================================\n";
		out = out + "# Scenario\n";
		out = out + "======================================\n";

		out = out + "Legal Crossing: " + legal + "\n";
		out = out + "Passengers (" + this.getPassengerCount() + ")\n";
		for (int i = 0; i < this.getPassengerCount(); i++) {
			out = out + "- " + this.passenger[i] + "\n";
		}
		out = out + "Pedestrians (" + this.getPedestrianCount() + ")\n";
		for (int i = 0; i < this.getPedestrianCount(); i++) {
			out = out + "- " + this.pedestrian[i] + "\n";
		}
		out = out.substring(0, out.length() - 1);
		return out;
	}
}
