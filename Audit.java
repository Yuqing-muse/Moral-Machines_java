/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
import ethicalengine.*;
import ethicalengine.Character;
import java.util.Collections;
import java.util.Map.Entry;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
/**
 * Audit the outcome
 * An inspection of your algorithm with the goal of revealing inherent biases
 * @author Yuqing Chang
 *
 */

public class Audit {
	// A ArrayList to store scenarios
	private ArrayList<Scenario> list = new ArrayList<Scenario>();
	// A ArrayList to store decisions
	private ArrayList<String> saves = new ArrayList<String>();
	private String auditType = null;
	private String outs;
	private int runs = 0;
	private double averageAge = 0.0;
	private Scenario[] scenarios = null;
	private int m = 0;
	// The number of showing scenarios
	private final int MAX = 3;
	private final int DIGIT = 10;
	// Current number of scenes that need to be decided
	private int size = 0;

	/**
	 * Constructor of Audit class
	 */
	public Audit() {
		auditType = "Unspecified";
		this.runs = MAX;
	}
	
	/**
	 * Constructor of Audit class
	 * @param scenarios{{@code Scenario[]}is an array to store scenarios
	 */
	public Audit(Scenario[] scenarios) {
		this.scenarios = scenarios;
		//this.runs = scenarios.length;
		for (int i = 0; i < scenarios.length; i++) {
			list.add(scenarios[i]);
		}
	}

	/**
	 * Set the type of audit
	 * 
	 * @param name {{@code String} is type of audit
	 */
	public void setAuditType(String name) {
		this.auditType = name;
	}

	/**
	 * Get the type of audit
	 * 
	 * @return {@code String} 
	 */
	public String getAuditType() {
		if (this.auditType == null) {
			this.auditType = "Unspecified";
		}
		return this.auditType;
	}

	/**
	 * Judging the survivors in each scene in the array
	 */
	public void run() {
		size = scenarios.length;
		for (Scenario s : scenarios) {
			// Judge the scene and decide the survivor
			saves.add(EthicalEngine.decide(s).toString().toLowerCase());
		}
	}

	/**
	 * Randomly generate a specified number of scenes, and judge the survivors in
	 * each scene
	 * 
	 * @param runs the {@code int} to record the number of running times
	 */
	public void run(int runs) {
		size = runs;
		// Automatically generate scenes randomly
		ScenarioGenerator manager = new ScenarioGenerator();
		// Temporary objects of scenarios and survival results
		ArrayList<Scenario> temp = new ArrayList<Scenario>();
		ArrayList<String> savesTemp = new ArrayList<String>();

		/*
		 * Randomly generate a specified number of scenes, decide and store the scenes
		 * and results
		 */
		for (int i = 0; i < runs - 1; i++) {
			Scenario e = manager.generate();
			temp.add(e);
			savesTemp.add(EthicalEngine.decide(e).toString().toLowerCase());// Error Null Pointer
		}

		for (int m = 0; m < temp.size(); m++) {
			list.add(temp.get(m));
			saves.add(savesTemp.get(m));
		}
	}

	/**
	 * In interactive mode, judging the survivors in each scene that read from .csv
	 * Return a boolean value to show this array whether has remaining
	 * 
	 * @return {@code true} if there's remaining scenes 
	 */
	public boolean runUser() {
		int r = scenarios.length;
		int num = 0;
		r = r - m;
		
		/*
		 * Interact with the user until the user has three interactions
		 */
		for (int i = m; i < scenarios.length; i++) {
			if (num < MAX) {
				System.out.println(scenarios[i]);
				System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2])");
				String ans = EthicalEngine.scan.nextLine();
				if (ans.contentEquals("passenger") || ans.contentEquals("1") || ans.contentEquals("passengers")) {
					num++;
					saves.add("passengers");
					continue;
				}
				if (ans.contentEquals("pedestrian") || ans.contentEquals("2") || ans.contentEquals("pedestrians")) {
					num++;
					saves.add("pedestrians");
					continue;
				}
			} else {
				// Determine whether the current scenes still remain
				if (r - num <= 0)
					return false;
				else {
					m = i;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * In interactive mode, randomly generate a specified number of scenes and judge
	 * the survivors in each scene Return a boolean value to show this array whether
	 * has remaining
	 * 
	 * @param r the {@code int} to record the number of running times
	 * @return {@code true} if there's remaining scenes 
	 */
	public boolean runUser(int r) {// Print out the scenes one by one
		int num = 0;
		r = r - m;
		size = MAX;
		ScenarioGenerator manager = new ScenarioGenerator();
		
		/*
		 * Randomly generate a specified number of scenes Interact with the user until
		 * the user has three interactions
		 */
		for (int i = m; i < r ; i++) {
			if (num < MAX) {
				Scenario e = manager.generate();

				System.out.println(e);
				System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2])");
				String ans = EthicalEngine.scan.nextLine();
				if (ans.contentEquals("passenger") || ans.contentEquals("1") || ans.contentEquals("passengers")) {
					num++;
					list.add(e);
					saves.add("passengers");
					continue;
				}
				if (ans.contentEquals("pedestrian") || ans.contentEquals("2") || ans.contentEquals("pedestrians")) {
					num++;
					list.add(e);
					saves.add("pedestrians");
					continue;
				}
			} else {
				if (r - num <= 0)
					return false;
				else {
					m = i;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Organize audit information into a string and return
	 * @return {@code String} to store audit information
	 */
	public String toString() {
		this.runs = saves.size();
		String out = "======================================\n";
		out = out + "# " + getAuditType() + " Audit\n";
		out = out + "======================================\n" + "- % SAVED AFTER " + this.runs + " RUNS\n";
		if (saves.size() == 0) {
			return "no audit available";
		}
		Map<String, Double> map = statistic(saves.size());
		// Sort by probability from largest to smallest
		Map<String, Double> hm = sortByValue(map);

		// Print the sorted hashmap
		for (Entry<String, Double> en : hm.entrySet()) {
				out = out + en.getKey() + ": " + ((double)((int)(en.getValue()*DIGIT)))/DIGIT + "\n";
		}
		out = out + "--\n" + "average age: " + ((double)((int)(averageAge*DIGIT)))/DIGIT + "\n";
		return out;
	}

	/**
	 * Print statistic into files in the specified path
	 * 
	 * @throws IOException when can't find files
	 */
	public void printStatistic() throws IOException {
		// Default storage path
		String result = "logs/results.log";
		this.outs = toString();
		System.out.print(outs);
		if (EthicalEngine.consent == true) {
			printToFile("logs/user.log");
			return;
		}
		if (EthicalEngine.outputPath != null)
			result = EthicalEngine.outputPath;
		printToFile(result);
	}

	/**
	 * Print statistic into files
	 * 
	 * @param filepath {@code String} is path of file
	 * @throws IOException when can't find files
	 */
	public void printToFile(String filepath) throws IOException {
		try {
			File fil = new File(filepath);
			if (!fil.exists()) {
				// The file does not exist. Create a new file and write the result
				fil.mkdirs();
				write_data(fil);
			} else {// This file already exists
				write_data(fil);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Write all current player data to .log file
	 * 
	 * @param f {@code File} is a file object
	 * @throws IOException when can't find files
	 */
	public void write_data(File f) throws IOException {
		try {
			FileWriter fileWriter = new FileWriter(f.getName(), true);
			BufferedWriter bw = new BufferedWriter(fileWriter);
			String[] str = this.outs.split("\n");// Split by line
			for (int i = 0; i < str.length; i++) {
				/*
				 * Write the information into file;
				 */
				bw.write(str[i] + "\n");

			}
			// Close BufferedWriter
			bw.close();
			fileWriter.close();
			outs = "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sort hashmap by values as descending order
	 * 
	 * @param hm {@code Map<String, Double>} is a key-value map 
	 * @return {@code HashMap<String, Double>} store key-value pairs as descending order
	 */
	public HashMap<String, Double> sortByValue(Map<String, Double> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	/**
	 * Calculate probabilities well into key-value pairs
	 * 
	 * @param l {@code int} is the number of scenario
	 * @return {@code Map<String, Double>}
	 */
	public Map<String, Double> statistic(int l) {
		Map<String, Double> map = new HashMap<>();
		// Count each value
		double baby = 0.0;
		double child = 0.0;
		double adult = 0.0;
		double senior = 0.0;
		double female = 0.0;
		double male = 0.0;
		double average = 0.0;
		double athletic = 0.0;
		double overweight = 0.0;
		double doctor = 0.0;
		double ceo = 0.0;
		double criminal = 0.0;
		double homeless = 0.0;
		double unemployed = 0.0;
		double unknownPro = 0.0;
		double pregnant = 0.0;
		double person = 0.0;
		double animal = 0.0;
		double dog = 0.0;
		double cat = 0.0;
		double bird = 0.0;
		double pets = 0.0;
		double you = 0.0;
		double ferret = 0.0;
		double red = 0.0;
		double green = 0.0;
		double ages = 0.0;
		double sum = 0;
		
		double[] count = new double[26];
		for (int i = 0; i < count.length; i++)
			count[i] = 0.0;
		/*
		 * Traverse each scene
		 */
		for (int j = 0; j < saves.size(); j++) {
			
			

			Character[] passenger = list.get(j).getPassengers();
			Character[] pedestrian = list.get(j).getPedestrians();
			// Count the number of occurrences of each attribute
			double[] temp = countValue(list.get(j), passenger, pedestrian);
			for (int m = 0; m < count.length; m++) {
				count[m] = count[m] + temp[m];
			}

			if (saves.get(j).contentEquals("passengers")) {
				// It was the passengers who survived
				sum = sum + passenger.length;
				if (list.get(j).isLegalCrossing() == true) {
					green = green + passenger.length;
				} else
					red = red + passenger.length;
				for (int i = 0; i < passenger.length; i++) {
					try {
						if (passenger[i].getClass() == Person.class) {
							// It's a person
							person = person + 1;
							ages = ages + passenger[i].getAge();
							if (((Person) passenger[i]).isYou() == true)
								you = you + 1;

							/*
							 * Count the number of occurrences of each attribute of survivors
							 */
							switch (((Person) passenger[i]).getAgeCategory()) {
							case BABY:
								baby = baby + 1;
								break;
							case CHILD:
								child = child + 1;
								break;
							case ADULT:
								adult = adult + 1;
								break;
							case SENIOR:
								senior = senior + 1;
								break;
							default:
								break;
							}
							switch (((Person) passenger[i]).getGender()) {
							case FEMALE:
								female=female+1;
								break;
							case MALE:
								male=male+1;
								break;
							default:
								break;
							}
							switch (((Person)passenger[i]).getBodyType()) {
							case AVERAGE:
								average=average+1;
								break;
							case ATHLETIC:
								athletic= athletic+1;
								break;
							case OVERWEIGHT:
								overweight=overweight+1;
								break;
							default:
								break;
							}
							switch (((Person) passenger[i]).getProfession()) {
							case DOCTOR:
								doctor=doctor+1;
								break;
							case CEO:
								ceo=ceo+1;
								break;
							case CRIMINAL:
								criminal=criminal+1;
								break;
							case HOMELESS:
								homeless=homeless+1;
								break;
							case UNEMPLOYED:
								unemployed=unemployed+1;
								break;
							case UNKNOWN:
								unknownPro=unknownPro+1;
								break;
							default:
								break;
							}
							if (((Person) passenger[i]).isPregnant())
								pregnant=pregnant+1;

						}
					} catch (NullPointerException e) {
					}
					try {
						if (passenger[i].getClass() == Animal.class) {
							// It's an animal
							animal=animal+1;
							if (((Animal) passenger[i]).isPet())
								pets=pets+1;
							switch (((Animal) passenger[i]).getSpecies()) {
							case "dog":
								dog=dog+1;
								break;
							case "cat":
								cat=cat+1;
								break;
							case "bird":
								bird=bird+1;
								break;
							case "ferret":
								ferret=ferret+1;
								break;
							default:
								break;
							}
						}
					} catch (NullPointerException e) {
					}
				}
			} else {
				// Pedestrians survive
				if (list.get(j).isLegalCrossing() == true) {
					green = green + pedestrian.length;
				} else
					red = red + pedestrian.length;
				sum = sum + pedestrian.length;
				for (int i = 0; i < pedestrian.length; i++) {
					try {
						if (pedestrian[i].getClass() == Person.class) {
							person = person + 1;
							ages = ages + pedestrian[i].getAge();
							if (((Person) pedestrian[i]).isYou() == true)
								you = you + 1;

							/*
							 * Count the number of occurrences of each attribute of survivors
							 */
							switch (((Person) pedestrian[i]).getAgeCategory()) {
							case BABY:
								baby = baby + 1;
								break;
							case CHILD:
								child = child + 1;
								break;
							case ADULT:
								adult = adult + 1;
								break;
							case SENIOR:
								senior = senior + 1;
								break;
							default:
								break;
							}
							switch (((Person) pedestrian[i]).getGender()) {
							case FEMALE:
								female=female+1;
								break;
							case MALE:
								male=male+1;
								break;
							default:
								break;
							}
							switch (((Person) pedestrian[i]).getBodyType()) {
							case AVERAGE:
								average=average+1;
								break;
							case ATHLETIC:
								athletic= athletic+1;
								break;
							case OVERWEIGHT:
								overweight=overweight+1;
								break;
							default:
								break;
							}
							switch (((Person) pedestrian[i]).getProfession()) {
							case DOCTOR:
								doctor=doctor+1;
								break;
							case CEO:
								ceo=ceo+1;
								break;
							case CRIMINAL:
								criminal=criminal+1;
								break;
							case HOMELESS:
								homeless=homeless+1;
								break;
							case UNEMPLOYED:
								unemployed=unemployed+1;
								break;
							case UNKNOWN:
								unknownPro=unknownPro+1;
								break;
							default:
								break;
							}
							if (((Person) pedestrian[i]).isPregnant())
								pregnant=pregnant+1;

						}
					} catch (NullPointerException e) {
					}
					try {
						if (pedestrian[i].getClass() == Animal.class) {
							// It's an animal
							animal=animal+1;
							if (((Animal) pedestrian[i]).isPet())
								pets=pets+1;
							switch (((Animal) pedestrian[i]).getSpecies()) {
							case "dog":
								dog=dog+1;
								break;
							case "cat":
								cat=cat+1;
								break;
							case "bird":
								bird=bird+1;
								break;
							case "ferret":
								ferret=ferret+1;
								break;
							default:
								break;
							}
						}
					} catch (NullPointerException e) {
					}
				}
			}
		}

		/*
		 * After traversing each scene, calculate the probability and put it into the
		 * map
		 */
		this.averageAge = ages / person;
		if (count[0] == 0.0)
			map.put("baby", (double) 0);
		else
			map.put("baby", baby / count[0]);

		if (count[1] == 0.0)
			map.put("child", (double) 0);
		else
			map.put("child", child / count[1]);

		if (count[2] == 0.0)
			map.put("adult", (double) 0);
		else
			map.put("adult", adult / count[2]);

		if (count[3] == 0.0)
			map.put("senior", (double) 0);
		else
			map.put("senior", senior / count[3]);

		if (count[4] == 0.0)
			map.put("female", (double) 0);
		else
			map.put("female", female / count[4]);

		if (count[5] == 0.0)
			map.put("male", (double) 0);
		else
			map.put("male", male / count[5]);

		if (count[6] == 0.0)
			map.put("average", (double) 0);
		else
			map.put("average", average / count[6]);

		if (count[7] == 0.0)
			map.put("athletic", (double) 0);
		else
			map.put("athletic", athletic / count[7]);

		if (count[8] == 0.0)
			map.put("overweight", (double) 0);
		else
			map.put("overweight", overweight / count[8]);

		if (count[9] == 0.0)
			map.put("doctor", (double) 0);
		else
			map.put("doctor", doctor / count[9]);

		if (count[10] == 0.0)
			map.put("ceo", (double) 0);
		else
			map.put("ceo", ceo / count[10]);

		if (count[11] == 0.0)
			map.put("criminal", (double) 0);
		else
			map.put("criminal", criminal / count[11]);

		if (count[12] == 0.0)
			map.put("homeless", (double) 0);
		else
			map.put("homeless", homeless / count[12]);

		if (count[13] == 0.0)
			map.put("unemployed", (double) 0);
		else
			map.put("unemployed", unemployed / count[13]);

		if (count[14] == 0.0)
			map.put("unknown", (double) 0);
		else
			map.put("unknown", unknownPro / count[14]);

		if (count[15] == 0.0)
			map.put("pregnant", (double) 0);
		else
			map.put("pregnant", pregnant / count[15]);

		if (count[16] == 0.0)
			map.put("person", (double) 0);
		else
			map.put("person", person / count[16]);

		if (count[17] == 0.0)
			map.put("animal", (double) 0);
		else
			map.put("animal", animal / count[17]);

		if (count[18] == 0.0)
			map.put("dog", (double) 0);
		else
			map.put("dog", dog / count[18]);

		if (count[19] == 0.0)
			map.put("cat", (double) 0);
		else
			map.put("cat", cat / count[19]);

		if (count[20] == 0.0)
			map.put("bird", (double) 0);
		else
			map.put("bird", bird / count[20]);

		if (count[21] == 0.0)
			map.put("pet", (double) 0);
		else
			map.put("pet", pets / count[21]);
		
		if (count[22] == 0.0)
			map.put("you", (double) 0);
		else
			map.put("you", you / count[22]);
	
		
		if (count[23] == 0.0)
			map.put("ferret", (double) 0);
		else
			map.put("ferret", ferret / count[23]);

		if (count[24] == 0.0)
			map.put("red", (double) 0);
		else
			map.put("red", red / count[24]);

		if (count[25] == 0.0)
			map.put("green", (double) 0);
		else
			map.put("green", green / count[25]);

		return map;
	}

	/**
	 * Statistics in each scene
	 * Count each value of objects in the scene
	 * Index of array is same as survivors' array 
	 * @param sc {@code Scenario} is a scenario
	 * @param passenger {@code Character[]} is an array of passengers
	 * @param pedestrian {@code Character[]} is an array of pedestrians
	 * @return {@code double[]}
	 */
	public double[] countValue(Scenario sc, Character[] passenger, Character[] pedestrian) {
		double[] count = new double[26];
		double[] pas, ped;
		count[24] = 0.0;					// Red light
		count[25] = 0.0;					// Green light

		pas = check(passenger);
		ped = check(pedestrian);
		for (int m = 0; m < count.length - 2; m++) {
			count[m] = pas[m] + ped[m];
		}
		if (sc.isLegalCrossing() == true)
			count[25] = passenger.length + pedestrian.length;
		if (sc.isLegalCrossing() == false)
			count[24] = passenger.length + pedestrian.length;

		return count;
	}

	/**
	 * Count the number of objects with certain attributes in the scene
	 *
	 * @param p {@code Character[]} is an array
	 * @return {@code double[]} stores the number of objects' attributes
	 */
	public double[] check(Character[] p) {
		double[] count = new double[24];
		for (int m = 0; m < count.length; m++)
			count[m] = 0.0;
		for (int i = 0; i < p.length; i++) {
			try {
				if (p[i].getClass() == Person.class) {
					count[16]++;
					switch (((Person) p[i]).getAgeCategory()) {
					case BABY:
						count[0]=count[0]+1;
						break;
					case CHILD:
						count[1]=count[1]+1;
						break;
					case ADULT:
						count[2]=count[2]+1;
						break;
					case SENIOR:
						count[3]=count[3]+1;
						break;
					default:
						break;
					}
					switch (((Person) p[i]).getGender()) {
					case FEMALE:
						count[4]=count[4]+1;
						break;
					case MALE:
						count[5]=count[5]+1;
						break;
					default:
						break;
					}
					switch (((Person) p[i]).getBodyType()) {
					case AVERAGE:
						count[6]=count[6]+1;
						break;
					case ATHLETIC:
						count[7]=count[7]+1;
						break;
					case OVERWEIGHT:
						count[8]=count[8]+1;
						break;
					default:
						break;
					}
					switch (((Person) p[i]).getProfession()) {
					case DOCTOR:
						count[9]=count[9]+1;
						break;
					case CEO:
						count[10]=count[10]+1;
						break;
					case CRIMINAL:
						count[11]=count[11]+1;
						break;
					case HOMELESS:
						count[12]=count[12]+1;
						break;
					case UNEMPLOYED:
						count[13]=count[13]+1;
						break;
					case UNKNOWN:
						count[14]=count[14]+1;
						break;
					default:
						break;
					}
					if (((Person) p[i]).isPregnant())
						count[15]=count[15]+1;
					if (((Person) p[i]).isYou())
						count[22]=count[22]+1;
				}
			} catch (NullPointerException e) {
			}
			try {
				if (p[i].getClass() == Animal.class) {
					count[17]=count[17]+1;
					if (((Animal) p[i]).isPet())
						count[21]=count[21]+1;
					switch (((Animal) p[i]).getSpecies()) {
					case "dog":
						count[18]=count[18]+1;
						break;
					case "cat":
						count[19]=count[19]+1;
						break;
					case "bird":
						count[20]=count[20]+1;
						break;
					case "ferret":
						count[23]=count[23]+1;
						break;
					default:
						break;
					}
				}
			} catch (NullPointerException e) {
			}

		}
		return count;
	}
}
