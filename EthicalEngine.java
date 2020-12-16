/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
import java.io.*;
import java.util.*;
import ethicalengine.*;
import ethicalengine.Character;
import ethicalengine.Character.BodyType;
import ethicalengine.Character.Gender;
import ethicalengine.Person.Profession;

/**
 * Including the main method and the method of determining the survivor
 * @author Yuqing Chang
 *
 */
public class EthicalEngine {
	public static Scanner scan = new Scanner(System.in);

	public enum Decision {PEDESTRIANS, PASSENGERS};

	private final static int MAX = 120;		// Maximum number of scenes Read from file
	private final static int GREEN = 1;		// Crossing the street legally
	private final static int RED = 2;		// Illegal crossing the road
	// Save scenes starting from 1, the corresponding index is passengers and
	// pedestrians in the same scene
	private static ArrayList[] passenger = new ArrayList[MAX];
	private static ArrayList[] pedestrian = new ArrayList[MAX];
	private static int[] color = new int[MAX];// green 1 red 2
	private static Scenario[] scens = null;
	public static boolean consent = false;;
	public static String outputPath = null;

	/**
	 * Main method
	 * @param args
	 * @throws IOException when can't find files
	 */
	public static void main(String[] args) throws IOException {
		// a welcome message
		String welcomePath = "welcome.ascii";
		File welcome = new File(welcomePath);
		if (welcome.exists()) {
			FileReader input = null;
			BufferedReader reader = null;
			try {
				input = new FileReader(welcome);
				reader = new BufferedReader(input);
				String line = null;
				// Read line by line
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (null != reader) {
					try {
						reader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (null != input) {
					try {
						input.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		while (true) {
			System.out.println("Do you consent to have your decisions saved to a file? (yes/no)");
			String answer = scan.nextLine();
			try {
				if (answer.contentEquals("yes")) {
					consent = true;
					break;
				} else if (answer.contentEquals("no")) {
					consent = false;
					break;
				} else
					throw new InvalidInputException();
			} catch (InvalidInputException e) {
				System.out.print("Invalid response.");
				continue;
			}

		}
		
		/**
		 * Parsing command line parameters
		 */
		try {
			String command = args[0];
			if (command == null) {
				random();
			}
			
			/*
			 * Interaction model
			 */
			if (command.contentEquals("--interactive") || command.contentEquals("-i")) {
				boolean has = false;
				int index = 0;
				if (args.length > 1) {
					// Command contains -i and -c
					for (int i = 0; i < args.length; i++) {
						if (args[i].contentEquals("-c")) {
							has = true;
							index = i;
						}
					}
					if (has == true) {
						// Judge the index of "-c"
						if (index == 1) {
							// Record whether the user wants to continue
							boolean conti = true;
							File fil = new File(args[2]);
							readFile(fil, args[2]);
							Audit aud = new Audit(scens);
							aud.setAuditType("User");
							/*
							 * Judge whether still has remaining scenes
							 */
							while (conti == true) {
								boolean remain = aud.runUser();
								aud.printStatistic();
								if (remain == false) {
									System.out.println("That's all. Press Enter to quit.");
									if (scan.nextLine().contains("")) {
										conti = false;
										exit();
									}
								}
								System.out.println("Would you like to continue? (yes/no)");
								String inputs = scan.nextLine();
								try {
									if (inputs.contentEquals("no")) {
										conti = false;
										exit();
									}
									if (inputs.contentEquals("yes")) {
										conti = true;
									} else
										throw new InvalidInputException();
								} catch (InvalidInputException e) {
									System.out.println("Invalid response.");
									continue;
								}
							}
						} else if (index == 2) {
							// Record whether the user wants to continue
							boolean conti = true;
							File fil = new File(args[1]);
							readFile(fil, args[1]);
							Audit aud = new Audit(scens);
							aud.setAuditType("User");
							/*
							 * Judge whether still has remaining scenes
							 */
							while (conti == true) {
								boolean remain = aud.runUser();
								aud.printStatistic();
								if (remain == false) {
									System.out.println("That's all. Press Enter to quit.");
									if (scan.nextLine().contains("")) {
										conti = false;
										exit();
									}
								}
								System.out.println("Would you like to continue? (yes/no)");
								String inputs = scan.nextLine();
								try {
									if (inputs.contentEquals("no")) {
										conti = false;
										exit();
									}
									if (inputs.contentEquals("yes")) {
										conti = true;
									} else
										throw new InvalidInputException();
								} catch (InvalidInputException e) {
									System.out.println("Invalid response.");
									continue;
								}
							}
						}
					}
				}
				// There's only "-i" in command, so we should randomly generate scenes
				if (args.length == 1)
					random();
			}
			
			/*
			 * Help mode
			 */
			if ((command.contentEquals("--help")) || (command.contentEquals("-h"))
					|| (command.contentEquals("--config") && args[1] == null)) {
				help();
			}
			
			/*
			 * Read scene data from file
			 */
			if (command.contentEquals("--config") || command.contentEquals("-c")) {
				if (args.length == 1)
					help();
				else {
					// check whether the file is located at the specified location
					File fil = new File(args[1]);
					readFile(fil, args[1]);
					Audit aud = new Audit(scens);
					aud.setAuditType("Algorithm");
					aud.run();
					aud.printStatistic();
				}
				
			}
			
			/*
			 * Change the path to save the file
			 */
			if (command.contentEquals("--results") || command.contentEquals("-r")) {
				if (args.length > 1)
					outputPath = args[1];
				else {
					System.out.println("ERROR: could not print results. Target directory does not exist.");
					exit();
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: could not find config file.");
		}
	}
	
	/**
	 * Show the help information
	 */
	public static void help() {
		String out = "EthicalEngine - COMP90041 - Final Project\n\n";
		out = out + "Usage: java EthicalEngine [arguments]\n\n";
		out = out + "Arguments:\n" + "  -c or --config Optional: path to config file\n";
		out = out + "  -h or --help Print Help (this message) and exit\n";
		out = out + "  -r or --results Optional: path to results log file\n";
		out = out + "  -i or --interactive Optional: launches interactive mode\n";
		System.out.print(out);
	}

	/**
	 * Choose whom to save for any scenario
	 * @param scenario{@code scenario} is a scenario
	 * @return {@code Decision} is the outcome of saving
	 */
	public static Decision decide(Scenario scenario) {
		/* First determine whether the pedestrian is crossing the road legally
		 * If one side is full of animals, then the other side is alive
		 * If there are animals on both sides, a large number of survivors (compare the number)
		 * If there are people on both sides, compare the age of the people
		 * First prepare the information in the scene
		 */
		Character[] passenger = scenario.getPassengers();
		Character[] pedestrian = scenario.getPedestrians();
		boolean isLegal = scenario.isLegalCrossing();

		int passengers = passenger.length;
		int pedestrians = pedestrian.length;
		int[] count = count(passenger);
		int[] count2 = count(pedestrian);
		/* Statistics derived information pedestrians. Count number of passengers in total +
		 * people + the elderly / infants / pregnant women /children
		 */

		if (isLegal == true) {
			if (count[2] != 0 || count[3] != 0 || count[4] != 0 || count[5] != 0) {
				if (count2[2] != 0 || count2[3] != 0 || count2[4] != 0 || count2[5] != 0) {
					// There are special people in the car and pedestrians
					if (passengers < pedestrians)
						return Decision.PEDESTRIANS;
					// There are many people in the car, or as many people
					else if (count[5] < count2[5]) {
						return Decision.PEDESTRIANS;
					} else if ((count[2] < count2[2]) || (count[3] < count2[3])) {
						return Decision.PEDESTRIANS;
					} else if (count[4] < count2[4])
						return Decision.PEDESTRIANS;
					else
						return Decision.PASSENGERS;
				} else {// Pedestrian without special person
					return Decision.PASSENGERS;
				}
			}
			// There are no special people in the car
			return Decision.PEDESTRIANS;
		} else {
			if (count2[2] != 0 || count2[3] != 0 || count2[4] != 0 || count2[5] != 0) {
				if (count[2] != 0 || count[3] != 0 || count[4] != 0 || count[5] != 0) {
					if (passengers < pedestrians)
						return Decision.PEDESTRIANS;
					else {
						if (count[5] < count2[5]) {
							return Decision.PEDESTRIANS;
						} else if ((count[2] < count2[2]) || (count2[3] < count[3])) {
							return Decision.PEDESTRIANS;
						} else if (count[4] < count2[4])
							return Decision.PEDESTRIANS;
						else
							return Decision.PASSENGERS;
					}
				} else
					return Decision.PEDESTRIANS;
			} else {
				if (count[2] != 0 || count[3] != 0 || count[4] != 0 || count[5] != 0) {
					return Decision.PASSENGERS;
				} else {// There are no special people in the car
					if (passengers < pedestrians)
						return Decision.PEDESTRIANS;
					else
						return Decision.PASSENGERS;
				}
			}
		}
	}

	/**
	 * Generate scenes randomly
	 * 
	 * @throws IOException when can't find file
	 */
	public static void random() throws IOException {
		// Record whether the user wants to continue
		boolean conti = true;
		Audit au = new Audit();
		au.setAuditType("User");
		
		/*
		 * Judge whether still has remaining scenes
		 */
		while (conti == true) {
			boolean remain = au.runUser(10);
			au.printStatistic();
			if (remain == false) {
				System.out.println("That's all. Press Enter to quit.");
				if (scan.nextLine().contains("")) {
					conti = false;
					exit();
				}
			}
			System.out.println("Would you like to continue? (yes/no)");
			String inputs = scan.nextLine();
			try {
				if (inputs.contentEquals("no")) {
					conti = false;
					exit();
				}
				if (inputs.contentEquals("yes")) {
					conti = true;
				} else
					throw new InvalidInputException();
			} catch (InvalidInputException e) {
				System.out.println("Invalid response.");
				continue;
			}
		}
	}

	/**
	 * Read from a file
	 * 
	 * @param fil {@code File} a file 
	 * @param path {@code String} is path of file
	 * @throws FileNotFoundException when can't find file
	 */
	public static void readFile(File fil, String path) throws FileNotFoundException {
		// Create EnumSet for each enum object
		EnumSet<Profession> profession = EnumSet.range(Profession.DOCTOR, Profession.UNKNOWN);
		EnumSet<Gender> gender = EnumSet.range(Gender.FEMALE, Gender.UNKNOWN);
		EnumSet<BodyType> body = EnumSet.range(BodyType.AVERAGE, BodyType.UNSPECIFIED);
		
		/*
		 * Read from file
		 */
		if (fil.exists()) {
			BufferedReader csvReader = new BufferedReader(new FileReader(path));
			String row;
			int iteration = 0;					// Identify index of different scenes in the array
			int lines = 0;						// The number of current lines
			// Store pedestrians and passengers in each scene
			ArrayList<Character> pas = null;
			ArrayList<Character> ped = null;

			try {
				while ((row = csvReader.readLine()) != null) {
					// Skip the header of the first line of the file
					if (lines == 0) {
						lines++;
						continue;
					}

					String[] data = row.split(",");
					/*
					 * Determine whether it is the beginning of a new scene
					 */
					if (data[0].contentEquals("scenario:green")) {
						iteration = iteration + 1;
						// Record the traffic lights of the current scene
						color[iteration] = GREEN;
						// Update the ArrayList every new scene
						pas = new ArrayList<Character>();
						ped = new ArrayList<Character>();
						lines++;
						continue;
					} else if (data[0].contentEquals("scenario:red")) {
						iteration = iteration + 1;
						// Record the traffic lights of the current scene
						color[iteration] = RED;
						// Update the ArrayList every new scene
						pas = new ArrayList<Character>();
						ped = new ArrayList<Character>();
						lines++;
						continue;
					}
					
					/*
					 * Invalid data format in config file in line
					 */
					try {
						if (data.length != 10) {
							throw new InvalidDataFormatException();
						}
					} catch (InvalidDataFormatException e) {
						System.out.println("WARNING: invalid data format in config file in line " + lines);
						lines++;
						continue;
					}
					
					/**
					 * Green light
					 */
					if (color[iteration] == GREEN) {
						/*
						 * It's a person, store the information
						 */
						if (data[0].contentEquals("person")) {
							Profession pro;
							Gender ge;
							BodyType bo;
							Object a = data[2];
							int age;
							
							/*
							 * Handle exception of each data and read data
							 */
							try {
								if (data[4] != null && profession.contains(Profession.valueOf(data[4].toUpperCase())))
									pro = Profession.valueOf(data[4].toUpperCase());
								else if (data[4].contentEquals("none"))
									pro = Profession.NONE;
								else
									throw new InvalidCharacteristicException(lines + 1);
							} catch (InvalidCharacteristicException e) {
								pro = Profession.UNKNOWN;
							} catch (IllegalArgumentException e2) {
								pro = Profession.UNKNOWN;
							}

							try {
								if (data[1] != null && gender.contains(Gender.valueOf(data[1].toUpperCase())))
									ge = Gender.valueOf(data[1].toUpperCase());
								else
									throw new InvalidCharacteristicException(lines + 1);

							} catch (InvalidCharacteristicException e) {
								ge = Gender.UNKNOWN;
							} catch (IllegalArgumentException e2) {
								ge = Gender.UNKNOWN;
							}
							try {
								if (data[3] != null && body.contains(BodyType.valueOf(data[3].toUpperCase())))
									bo = BodyType.valueOf(data[3].toUpperCase());
								else
									throw new InvalidCharacteristicException(lines + 1);
							} catch (InvalidCharacteristicException e) {
								bo = BodyType.UNSPECIFIED;
							} catch (IllegalArgumentException e2) {
								bo = BodyType.UNSPECIFIED;
							}
							boolean pre, you;
							if (data[5].contentEquals("TRUE") || data[5].contentEquals("true"))
								pre = true;
							else
								pre = false;

							try {
								if (data[2] != null)
									age = Integer.parseInt(data[2]);
								else
									throw new NumberFormatException();
							} catch (NumberFormatException e) {
								System.out.println("WARNING: invalid number format in config file in line " + lines);
								age = 0;
							}
							Person p = new Person(age, pro, ge, bo, pre);
							if (data[6].contentEquals("TRUE") || data[6].contentEquals("true"))
								you = true;
							else
								you = false;
							p.setAsYou(you);
							
							// Add this object to the list
							if (data[9].contentEquals("passenger"))
								pas.add(p);
							else
								ped.add(p);
						}
						
						// It's an animal, store the information of each attribute
						if (data[0].contentEquals("animal")) {
							Object a = data[2];
							int age;
							Gender ge;
							BodyType bo;
							
							/*
							 * Handle exception of each data and read data
							 */
							try {
								if (data[2] != null)
									age = Integer.parseInt(data[2]);
								else
									throw new NumberFormatException();
							} catch (NumberFormatException e) {
								System.out.println("WARNING: invalid number format inconfig file in line " + lines + 1);
								age = 0;
							}
							Animal an = new Animal(data[7]);
							try {
								if (data[1] != null && gender.contains(Gender.valueOf(data[1].toUpperCase())))
									ge = Gender.valueOf(data[1].toUpperCase());
								else
									throw new InvalidCharacteristicException(lines + 1);

							} catch (InvalidCharacteristicException e) {
								ge = Gender.UNKNOWN;
							} catch (IllegalArgumentException e2) {
								ge = Gender.UNKNOWN;
							}
							try {
								if (data[3] != null && body.contains(BodyType.valueOf(data[3].toUpperCase().trim())))
									bo = BodyType.valueOf(data[3].toUpperCase());
								else
									throw new InvalidCharacteristicException(lines + 1);
							} catch (InvalidCharacteristicException e) {
								bo = BodyType.UNSPECIFIED;
							} catch (IllegalArgumentException e2) {
								bo = BodyType.UNSPECIFIED;
							}
							boolean pre, isPet;
							if (data[5].contentEquals("TRUE") || data[5].contentEquals("true"))
								pre = true;
							else
								pre = false;
							if (data[8].contentEquals("TRUE") || data[8].contentEquals("true"))
								isPet = true;
							else
								isPet = false;
							an.setAge(age);
							an.setBodyType(bo);
							an.setGender(ge);
							an.setPregnant(pre);
							an.setPet(isPet);
							
							// Add this object to the list
							if (data[9].contentEquals("passenger"))
								pas.add(an);
							else
								ped.add(an);
						}

					}
					
					/**
					 * Red light
					 */
					if (color[iteration] == RED) {

						if (data[0].contentEquals("person")) {
							Profession pro = null;
							Gender ge = null;
							BodyType bo = null;
							
							/*
							 * Handle exception of each data
							 */
							try {
								if (data[4] != null && profession.contains(Profession.valueOf(data[4].toUpperCase())))
									pro = Profession.valueOf(data[4].toUpperCase());
								else if (data[4].contentEquals("none"))
									pro = Profession.NONE;
								else
									throw new InvalidCharacteristicException(lines + 1);
							} catch (InvalidCharacteristicException e) {
								pro = Profession.UNKNOWN;
							} catch (IllegalArgumentException e2) {
								pro = Profession.UNKNOWN;
							}

							try {
								if (data[1] != null && gender.contains(Gender.valueOf(data[1].toUpperCase())))
									ge = Gender.valueOf(data[1].toUpperCase());
								else
									throw new InvalidCharacteristicException(lines + 1);

							} catch (InvalidCharacteristicException e) {
								ge = Gender.UNKNOWN;
							} catch (IllegalArgumentException e2) {
								ge = Gender.UNKNOWN;
							}
							try {
								if (data[3] != null && body.contains(BodyType.valueOf(data[3].toUpperCase())))
									bo = BodyType.valueOf(data[3].toUpperCase());
								else
									throw new InvalidCharacteristicException(lines + 1);
							} catch (InvalidCharacteristicException e) {
								bo = BodyType.UNSPECIFIED;
							} catch (IllegalArgumentException e2) {
								bo = BodyType.UNSPECIFIED;
							}

							boolean pre, you;
							if (data[5].contentEquals("TRUE") || data[5].contentEquals("true"))
								pre = true;
							else
								pre = false;
							int age;
							try {
								if (data[2] != null)
									age = Integer.parseInt(data[2]);
								else
									throw new NumberFormatException();
							} catch (NumberFormatException e) {
								System.out.println("WARNING: invalid number format inconfig file in line " + lines + 1);
								age = 0;
							}
							Person p = new Person(age, pro, ge, bo, pre);
							if (data[6].contentEquals("TRUE") || data[6].contentEquals("true"))
								you = true;
							else
								you = false;
							p.setAsYou(you);
							if (data[9].contentEquals("passenger"))
								pas.add(p);
							else
								ped.add(p);
						}
						if (data[0].contentEquals("animal")) {
							Object a = data[2];
							int age;
							Gender ge;
							BodyType bo;
							try {
								if (data[2] != null)
									age = Integer.parseInt(data[2]);
								else
									throw new NumberFormatException();
							} catch (NumberFormatException e) {
								System.out.println("WARNING: invalid number format inconfig file in line " + lines + 1);
								age = 0;
							}
							
							try {
								if (data[1] != null && gender.contains(Gender.valueOf(data[1].toUpperCase())))
									ge = Gender.valueOf(data[1].toUpperCase());
								else
									throw new InvalidCharacteristicException(lines + 1);

							} catch (InvalidCharacteristicException e) {
								ge = Gender.UNKNOWN;
							}
							try {
								if (data[3] != null && body.contains(BodyType.valueOf(data[3].toUpperCase())))
									bo = BodyType.valueOf(data[3].toUpperCase());
								else
									throw new InvalidCharacteristicException(lines + 1);
							} catch (InvalidCharacteristicException e) {
								bo = BodyType.UNSPECIFIED;
							} catch (IllegalArgumentException e2) {
								bo = BodyType.UNSPECIFIED;
							}
							boolean pre, isPet;
							if (data[5].contentEquals("TRUE") || data[5].contentEquals("true"))
								pre = true;
							else
								pre = false;
							if (data[8].contentEquals("TRUE") || data[8].contentEquals("true"))
								isPet = true;
							else
								isPet = false;
							Animal an = new Animal(data[7]);
							an.setAge(age);
							an.setBodyType(bo);
							an.setGender(ge);
							an.setPregnant(pre);
							an.setPet(isPet);
							
							// Add this object to the list
							if (data[9].contentEquals("passenger"))
								pas.add(an);
							else
								ped.add(an);
						}
					}
					
					// Store passengers and pedestrians in a scene in an array with the same index
					if (iteration != 0) {
						passenger[iteration] = pas;
						pedestrian[iteration] = ped;
					}
					lines++;
				}
				csvReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// no file
			throw new FileNotFoundException();
		}
		
		/*
		 * Count the number of real scenes
		 */
		int length = 0;
		for (int i = 0; i < passenger.length; i++) {
			if (passenger[i] != null) {
				length = length + 1;
			}
		}
		scens = new Scenario[length];
		
		boolean legal = false;
		for (int i = 1; i < length + 1; i++) {
			Character[] pa = new Character[passenger[i].size()];
			Character[] pe = new Character[pedestrian[i].size()];
			if (color[i] == GREEN)
				legal = true;
			if (color[i] == RED)
				legal = false;
			/*
			 * Install the passenger list into the passenger array
			 */
			for (int j = 0; j < pa.length; j++) {
				pa[j] = (Character) passenger[i].get(j);
			}

			/*
			 * Install the pedestrian list into the pedestrian array
			 */
			for (int m = 0; m < pe.length; m++) {
				pe[m] = (Character) pedestrian[i].get(m);
			}
			
			scens[i - 1] = new Scenario(pa, pe, legal);
		}
	}

	/**
	 * Count the number of objects with certain attributes in the scene
	 * Statistics the attributes that determine the survivor
	 * @param p {@code Character[]} is an array of characters
	 * @return {@code int[]} stores the number of each character
	 */
	public static int[] count(Character[] p) {
		// There are six attributes that affect the outcome of the survivor
		int[] count = new int[6];
		int person = 0;
		int animal = 0;
		int baby = 0;
		int child = 0;
		int senior = 0;
		int pregnant = 0;
		for (int i = 0; i < p.length; i++) {
			if (p[i].getClass() == Person.class) {
				person = person + 1;
				if (((Person) p[i]).getAgeCategory().toString().contentEquals("BABY"))
					baby = baby + 1;
				else if (((Person) p[i]).getAgeCategory().toString().contentEquals("CHILD"))
					child = child + 1;
				else if (((Person) p[i]).getAgeCategory().toString().contentEquals("SENIOR"))
					senior = senior + 1;

				if (((Person) p[i]).isPregnant() == true) {
					pregnant = pregnant + 1;
				}
			}
		}

		animal = p.length - person;
		count[0] = person;
		count[1] = animal;
		count[2] = baby;
		count[3] = child;
		count[4] = senior;
		count[5] = pregnant;
		return count;
	}

	/**
	 * Exit the program
	 */
	public static void exit() {
		System.exit(0);
	}
}
