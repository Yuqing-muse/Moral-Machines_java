/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package ethicalengine;

import java.util.Random;

import ethicalengine.Character.BodyType;
import ethicalengine.Character.Gender;
import ethicalengine.Person.Profession;

/**
 * A generator that randomly generates scenes
 * @author Yuqing Chang
 *
 */
public class ScenarioGenerator {
	// Attributes of a ScenarioGenerator object
	private long seed;					// Seed of a Random object
	private int passengerMin = 0;
	private int passengerMax = 0;
	private int pedestrianMin = 0;
	private int pedestrianMax = 0;

	// Enum object of species
	public enum Species {DOG, CAT, BIRD, FERRET};

	private Random r;					// Random Object

	/**
	 * Constructor of ScenarioGenerator class
	 */
	public ScenarioGenerator() {
		// Random seed generation
		this.seed = System.currentTimeMillis();
		r = new Random(seed);

	}

	/**
	 * Constructor of ScenarioGenerator class
	 * @param seed{{@code long} is a seed of random r
	 */
	public ScenarioGenerator(long seed) {
		this.seed = seed;
		r = new Random(seed);
	}

	/**
	 * Constructor of ScenarioGenerator class
	 * @param seed {{@code long} is a seed of random r
	 * @param passengerCountMinimum {@code int} is the number of minimum passengers
	 * @param passengerCountMaximum {@code int} is the number of maximum passengers
	 * @param pedestrianCountMinimum {@code int} is the number of minimum pedestrians
	 * @param pedestrianCountMaximum {@code int} is the number of maximum pedestrians
	 */
	public ScenarioGenerator(long seed, int passengerCountMinimum, int passengerCountMaximum,
			int pedestrianCountMinimum, int pedestrianCountMaximum) {
		this.seed = seed;
		this.passengerMin = passengerCountMinimum;
		this.passengerMax = passengerCountMaximum;
		this.pedestrianMin = pedestrianCountMinimum;
		this.pedestrianMax = pedestrianCountMaximum;
	}

	/**
	 * Set the number of minimum passengers
	 * @param min {@code int} is the number of minimum passengers
	 */
	public void setPassengerCountMin(int min) {
		this.passengerMin = min;
	}

	/**
	 * Set the number of maximum passengers
	 * @param max {@code int} is the number of maximum passengers
	 */
	public void setPassengerCountMax(int max) {
		this.passengerMax = max;
	}

	/**
	 * Set the number of minimum pedestrians
	 * @param min {@code int} is the number of minimum pedestrians
	 */
	public void setPedestrianCountMin(int min) {
		this.pedestrianMin = min;
	}

	/**
	 * Set the number of maximum pedestrians
	 * @param max {@code int} is the number of maximum pedestrians
	 */
	public void setPedestrianCountMax(int max) {
		this.pedestrianMax = max;
	}

	/**
	 * Generate a Person object randomly
	 * @return {@code Person} is a Person object
	 */
	public Person getRandomPerson() {
		// Use pseudo-random numbers to randomly generate each object attribute
		int age = r.nextInt(100);
		int p = r.nextInt(6);
		int g = r.nextInt(3);
		int b = r.nextInt(BodyType.values().length);
		int pre = r.nextInt(2);
		boolean preg;
		if (pre == 0) {
			preg = true;
		} else {
			preg = false;
		}

		Person person = new Person(age, Profession.values()[p], Gender.values()[g], BodyType.values()[b], preg);
		return person;
	}

	/**
	 * Generate an Animal object randomly
	 * @return {@code Animal} is an Animal object
	 */
	public Animal getRandomAnimal() {
		// Use pseudo-random numbers to randomly generate each object attribute
		int age = r.nextInt(100);
		int g = r.nextInt(Gender.values().length);
		int b = r.nextInt(BodyType.values().length);
		int s = r.nextInt(Species.values().length);
		int ispet = r.nextInt(2);
		boolean pet;
		if (ispet == 0) {
			pet = true;
		} else {
			pet = false;
		}
		int pre = r.nextInt(2);
		boolean preg;
		if (pre == 0) {
			preg = true;
		} else {
			preg = false;
		}
		Animal ani = new Animal(Species.values()[s].toString().toLowerCase());
		ani.setAge(age);
		ani.setGender(Gender.values()[g]);
		ani.setBodyType(BodyType.values()[b]);
		ani.setPet(pet);
		ani.setPregnant(preg);
		return ani;
	}

	/**
	 * Generate a scenario object randomly
	 * @return {@code Scenario} is a Scenario object
	 */
	public Scenario generate() {
		if (this.passengerMax == 0) {
			setPassengerCountMax(5);
		}
		if (this.passengerMin == 0) {
			setPassengerCountMin(1);
		}
		if (this.pedestrianMax == 0) {
			setPedestrianCountMax(5);
		}
		if (this.pedestrianMin == 0) {
			setPedestrianCountMin(1);
		}
		int pas = r.nextInt(passengerMax - passengerMin) + passengerMin;
		int ped = r.nextInt(pedestrianMax - pedestrianMin) + pedestrianMin;
		Character passenger[] = new Character[pas];
		Character pedestrian[] = new Character[ped];
		int youPlace = 0;
		for (int i = 0; i < pas; i++) {
			// Set the probability of being a passenger
			int choose = r.nextInt(2);
			if (choose == 0) {
				passenger[i] = getRandomPerson();
				/*
				 * Check if an array has you before
				 */
				if ((checkYou(passenger) == false) && (checkYou(pedestrian) == false)) {
					// There is no one in the passenger
					int you = r.nextInt(2);
					if (you == 0) {
						((Person) passenger[i]).setAsYou(true);
						youPlace = 1;			// You are in the car
					} else {
						((Person) passenger[i]).setAsYou(false);
					}
				}
			} else
				passenger[i] = getRandomAnimal();
		}

		for (int j = 0; j < ped; j++) {
			int choose = r.nextInt(2);
			if (choose == 0) {
				pedestrian[j] = getRandomPerson();
				if ((checkYou(passenger) == false) && (checkYou(pedestrian) == false)) {
					int you = r.nextInt(2);
					if (you == 0) {
						((Person) pedestrian[j]).setAsYou(true);
						youPlace = 2;			// You are in a lane
					} else {
						((Person) pedestrian[j]).setAsYou(false);
					}
				}
			} else
				pedestrian[j] = getRandomAnimal();
		}

		int crossing = r.nextInt(2);
		boolean isLegalCrossing;
		if (crossing == 0) {
			isLegalCrossing = true;
		} else {
			isLegalCrossing = false;
		}
		Scenario scen = new Scenario(passenger, pedestrian, isLegalCrossing);
		if (youPlace == 1) {
			// you are in car
			scen.setYouInCar(true);
			scen.setYouInLane(false);
		} else if (youPlace == 2) {
			// You are on the road
			scen.setYouInLane(true);
			scen.setYouInCar(false);
		} else {
			scen.setYouInLane(false);
			scen.setYouInCar(false);
		}
		return scen;
	}

	/**
	 * Check if you are in the current array
	 * @param array{@code Character[]} is an array of characters
	 * @return {@code boolean} is {@code true} if an array has you
	 */
	public boolean checkYou(Character[] array) {
		if (array == null)
			return false;
		for (int i = 0; i < array.length; i++) {
			try {
				if (array[i].getClass() == Person.class) {
					if (((Person) array[i]).isYou() == true)
						return true;
				}
			} catch (NullPointerException e) {

			}
		}
		return false;
	}
}
