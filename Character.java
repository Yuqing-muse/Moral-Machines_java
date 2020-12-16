/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package ethicalengine;

/**
 * Abstract class
 * Define character basic attributes
 * @author Yuqing Chang
 *
 */
public abstract class Character {
	public enum Gender {FEMALE, MALE, UNKNOWN};

	public enum BodyType {AVERAGE, ATHLETIC, OVERWEIGHT, UNSPECIFIED};

	// Attributes of a Character object
	private int age;
	private Gender gender;
	private BodyType bodytype;

	/**
	 * Constructor of Character class
	 */
	public Character() {
		gender = Gender.UNKNOWN;
		bodytype = BodyType.UNSPECIFIED;
		age = 0;
	}

	/**
	 * Constructor of Character class
	 * @param age {@code int} is the age of a character
	 * @param gender {@code Gender} is the gender of a character
	 * @param bodytype{@code BodyType} is the body type of a character
	 */
	public Character(int age, Gender gender, BodyType bodytype) {
		if (age >= 0)
			this.age = age;
		else
			this.age = 0;
		this.gender = gender;
		this.bodytype = bodytype;
	}

	/**
	 * Constructor of Character class
	 * @param c {@code character} is an object of Character
	 */
	public Character(Character c) {
		if (c == null) {
			System.out.println("Error: null character.");
			System.exit(0);
		}
		gender = c.gender;
		bodytype = c.bodytype;
		if (c.age >= 0)
			age = c.age;
		else
			age = 0;
	}

	/**
	 * Get age of a character
	 * @return {@code int} is the age of a character
	 */
	public int getAge() {
		return this.age;
	}

	/**
	 * Get gender of a character
	 * @return {@code Gender} is the gender of a character
	 */
	public Gender getGender() {
		return this.gender;
	}

	/**
	 * Get body type of a character
	 * @return {@code BodyType} is the body type of a character
	 */
	public BodyType getBodyType() {
		return this.bodytype;
	}

	/**
	 * Set age of a character
	 * @param age {@code int} is the age of a character
	 */
	public void setAge(int age) {
		if (age >= 0)
			this.age = age;
		else
			this.age = 0;
	}

	/**
	 * Set gender of a character
	 * @param gender {@code Gender} is the gender of a character
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * Set body type of a character
	 * @param bodytype {@code BodyType} is the body type of a character
	 */
	public void setBodyType(BodyType bodytype) {
		this.bodytype = bodytype;
	}
}
