/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package ethicalengine;

/**
 * Subclasses of the Character class, defining variables of animals
 * @author Yuqing Chang
 *
 */
public class Animal extends Character {
	private String species;
	private boolean isPet;
	private boolean isPregnant;

	/**
	 * Constructor of Animal class
	 */
	public Animal() {
		super();
		this.species = null;
	}

	/**
	 * Constructor of Animal class
	 * @param species {@code String} is the species of an animal
	 */
	public Animal(String species) {
		super();
		this.species = species;
	}

	/**
	 * Constructor of Animal class
	 * @param otherAnimal {@code Animal} is an object of Animal class
	 */
	public Animal(Animal otherAnimal) {
		super(otherAnimal);
		this.species = otherAnimal.species;
	}

	/**
	 * Get species of an animal
	 * @return{@code String} is the species of an animal
	 */
	public String getSpecies() {
		return this.species;
	}

	/**
	 * Set species of an animal
	 * @param species {@code String} is the species of an animal
	 */
	public void setSpecies(String species) {
		this.species = species;
	}

	/**
	 * Set an animal whether it is a pet
	 * @param pet {@code boolean} is {@code true} if an animal is a pet 
	 */
	public void setPet(boolean pet) {
		this.isPet = pet;
	}

	/**
	 * Get an animal whether it is a pet
	 * @return {@code boolean} is {@code true} if an animal is a pet
	 */
	public boolean isPet() {
		return isPet;
	}

	/**
	 * Set information about whether the animal is pregnant
	 * @param isPregnant {@code boolean} is {@code true} if an animal is a pet
	 */
	public void setPregnant(boolean isPregnant) {
		if (this.getGender() != Gender.FEMALE) {
			this.isPregnant = false;
		} else
			this.isPregnant = isPregnant;
	}

	/**
	 * Organize animal information into a string and return
	 * @return {@code String} to store animal information
	 */
	public String toString() {
		String out;
		if (this.isPet == false) {
			out = this.species.toLowerCase();
		} else {
			out = this.species.toLowerCase() + " is pet";
		}
		return out;
	}
}
