/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
package ethicalengine;

import ethicalengine.Character.BodyType;
import ethicalengine.Character.Gender;
import ethicalengine.Person.Profession;

/**
 * Subclasses of the Character class, defining variables of human
 * @author Yuqing Chang
 *
 */
public class Person extends Character {
	// Enum objects
	public enum Profession {DOCTOR, CEO, CRIMINAL, HOMELESS, UNEMPLOYED, UNKNOWN, NONE};
	public enum AgeCategory {BABY, CHILD, ADULT, SENIOR};

	private Profession profession;			// Professions
	private boolean isPregnant;
	private boolean isYou;

	/**
	 * Constructor of Person class
	 */
	public Person() {
		super();
		this.profession = Profession.NONE;
		this.isPregnant = false;

	}

	/**
	 * Constructor of Person class
	 * @param age {@code int} is the age of a person
	 * @param profession {@code Profession} is the profession of a person
	 * @param gender {@code Gender} is the gender of a person
	 * @param bodytype{@code BodyType} is the body type of a person
	 * @param isPregnant {@code boolean} is {@code true} if a person is a pregnant
	 */
	public Person(int age, Profession profession, Gender gender, BodyType bodytype, boolean isPregnant) {
		super(age, gender, bodytype);
		/*
		 * Only adults have professions
		 */
		if (this.getAgeCategory() == AgeCategory.ADULT) {
			this.profession = profession;
		} else
			profession = Profession.NONE;
		if (gender != Gender.FEMALE) {
			this.isPregnant = false;
		} else
			this.isPregnant = isPregnant;
	}

	/**
	 * Constructor of Person class
	 * @param age {@code int} is the age of a person
	 * @param gender {@code Gender} is the gender of a person
	 * @param bodytype{@code BodyType} is the body type of a person
	 */
	public Person(int age, Gender gender, BodyType bodytype) {
		super(age, gender, bodytype);
		if (this.getAgeCategory() == AgeCategory.ADULT) {
			profession = Profession.UNKNOWN;
		} else
			profession = Profession.NONE;
		this.isPregnant = false;
	}

	/**
	 * Constructor of Person class
	 * @param otherPerson {@code Person} is the object of a person
	 */
	public Person(Person otherPerson) {
		super(otherPerson);
		if (otherPerson.getAge() >= 17 && otherPerson.getAge() <= 68) {
			this.profession = otherPerson.profession;
		} else
			this.profession = Profession.NONE;
		if (otherPerson.getGender() != Gender.FEMALE) {
			this.isPregnant = false;
		} else
			this.isPregnant = otherPerson.isPregnant;
	}

	/**
	 * Get age category of a person
	 * @return {@code AgeCategory} is the category of age
	 */
	public AgeCategory getAgeCategory() {
		int age = this.getAge();
		if (age <= 4) {
			return AgeCategory.BABY;
		} else if (age <= 16) {
			return AgeCategory.CHILD;
		} else if (age <= 68) {
			return AgeCategory.ADULT;
		} else {
			return AgeCategory.SENIOR;
		}
	}

	/**
	 * Get profession of a person
	 * @return {@code Profession} is the profession of a person
	 */
	public Profession getProfession() {
		if (this.getAgeCategory() == AgeCategory.ADULT) {
			return this.profession;
		} else
			return Profession.NONE;
	}

	/**
	 * Get information about whether the person is pregnant
	 * @return {@code boolean} is {@code true} if a person is a pregnant
	 */
	public boolean isPregnant() {
		if (this.getGender() != Gender.FEMALE) {
			return false;
		} else
			return this.isPregnant;
	}

	/**
	 * Set information about whether the person is pregnant
	 * @param pregnant {@code boolean} is {@code true} if a person is a pregnant
	 */
	public void setPregnant(boolean pregnant) {
		if (this.getGender() == Gender.FEMALE) {
			this.isPregnant = pregnant;
		} else
			this.isPregnant = false;
	}

	/**
	 * Get information about whether the person is you
	 * @return {@code boolean} is {@code true} if a person is you
	 */
	public boolean isYou() {
		return isYou;
	}

	/**
	 * Set information about whether the person is you
	 * @param isYou {@code boolean} is {@code true} if a person is you
	 */
	public void setAsYou(boolean isYou) {
		this.isYou = isYou;
	}

	@Override
	/**
	 * Organize person information into a string and return
	 * @author yuqchang
	 * @return {@code String} to store person information
	 */
	public String toString() {
		String body = this.getBodyType().toString().toLowerCase();
		String age = this.getAgeCategory().toString().toLowerCase();
		String gender = this.getGender().toString().toLowerCase();
		String out;
		if (this.isYou == false) {
			if (this.profession == null) {
				if (this.isPregnant == false) {

					out = body + " " + age + " " + gender;
				} else {
					String s = body + " " + age + " " + gender;
					out = s + " pregnant";
				}
			} else {
				String pro = this.profession.toString().toLowerCase();
				String s = body + " " + age + " " + pro + " " + gender;
				if (this.isPregnant == false) {
					out = s;
				} else {
					out = s + " pregnant";
				}
			}
		} else {
			if (this.profession == null) {
				String s = "you " + body + " " + age + " " + gender;
				if (this.isPregnant == false) {
					out = s;
				} else {
					out = s + " pregnant";
				}
			} else {
				String pro = this.profession.toString().toLowerCase();
				String s = "you " + body + " " + age + " " + pro + " " + gender;
				if (this.isPregnant == false) {
					out = s;
				} else {
					out = s + " pregnant";
				}
			}
		}
		return out;
	}

}
