/**
 * Program to test hashtables.
 */
class HashDemo {
	/**
	 * Main testing method.
	 * @param args Type of hashtable
	 */
	public static void main(String[] args) {
		if (args.length==1 && (args[0].equals("1") || args[0].equals("2"))) {
			demoProgram(args[0].equals("1"));
			return;
		}
		else {
			System.out.println("Usage: java HashDemo [1|2]\n1 - Use Open Addressing with Linear Probing\n2 - Use Separate Chaining");
		}
	}

	/**
	 * Demo program.
	 * @param useTable1 Type of table.
	 */
	public static void demoProgram(boolean useTable1) {
		try(java.util.Scanner input = new java.util.Scanner(System.in)) {
			OpenAddress<String,String> table1 = new OpenAddress<>(2);
			SeparateChain<String,String> table2 = new SeparateChain<>(2);
			
			System.out.println("\nThis is a demo interactive program for your hash table. Be aware that both the keys and values in the table are Strings, so if you enter 1 as your key, you get the string \"1\" not the integer 1.");
			
			while(true) {
				System.out.println("\nOptions:\n\t1. Add/Replace a Key-Value Pair\n\t2. Get the value associated with a key\n\t3. Remove a key\n\t4. Resize the table\n\t5. Display the table\n\t6. Quit");
				
				//get user selection
				int choice = forceIntChoice(input, "Enter a menu choice: ", 1, 6);
				
				//menu actions
				String key, value;
				int size;
				
				switch(choice) {
					case 1: //put
						System.out.println("----------Adding/Updating a Key-Value Pair----------");
						
						System.out.print("Enter a key: ");
						key = input.nextLine();
						
						System.out.print("Enter a value: ");
						value = input.nextLine();
						
						size = useTable1 ? table1.size() : table2.size();
						if(useTable1) table1.put(key, value);
						else table2.put(key, value);
						
						System.out.println(((size == (useTable1 ? table1.size() : table2.size())) ? "Updated" : "Added") + " value at key.");
						pauseForUser(input);
						
						break;
					case 2: //get
						System.out.println("----------Getting a Value by Key----------");
						
						System.out.print("Enter a key: ");
						key = input.nextLine();
						
						value = useTable1 ? table1.get(key) : table2.get(key);
						System.out.println((value == null) ? "No such key" : "Associated value is " + value);
						pauseForUser(input);
						
						break;
					case 3: //remove
						System.out.println("----------Removing a Key-Value Pair----------");
						
						System.out.print("Enter a key: ");
						key = input.nextLine();
						
						
						value = useTable1 ? table1.remove(key) : table2.remove(key);
						
						System.out.println((value == null) ? "No such key" : "Removed pair was (" + key + "," + value + ")");
						pauseForUser(input);
						
						break;
					case 4: //resize
						System.out.println("----------Resizing the Table----------");
						
						size = forceIntChoice(input, "Enter a new size: ", Integer.MIN_VALUE, Integer.MAX_VALUE);
						boolean done = useTable1 ? table1.rehash(size) : table2.rehash(size);
						
						System.out.println(done ? "Resized table" : "Unable to resize table to requested size");
						pauseForUser(input);
						
						break;
					case 5: //display
						break;
					case 6: //quit
					default:
						return;
				}
				
				System.out.println("******************************************");
				size = useTable1 ? table1.size() : table2.size();
				int capacity = useTable1 ? table1.getCapacity() : table2.getCapacity();
				System.out.println("Table Size: " + size + ", Capacity: " + capacity);
				System.out.println(useTable1 ? table1.toStringDebug() : table2.toStringDebug());
				System.out.println("******************************************");
				pauseForUser(input);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Pause for user.
	 * @param input User input
	 */
	private static void pauseForUser(java.util.Scanner input) {
		System.out.println("(Hit <Enter> to Continue)");
		input.nextLine();
	}

	/**
	 * Forces int requirements.
	 * @param input Input
	 * @param prompt Prompt
	 * @param min Min
	 * @param max Max
	 * @return Integer
	 */
	private static int forceIntChoice(java.util.Scanner input, String prompt, int min, int max) {
		int choice = -1;
		while(choice == -1) {
			try {
				System.out.print(prompt);
				choice = Integer.parseInt(input.nextLine());
				if(choice >= min && choice <= max) {
					return choice;
				}
				System.out.println("You must enter an integer between "+min+" and "+max+".");
			}
			catch(RuntimeException e) { }
			System.out.println("You must enter a valid integer.");
		}
		return choice;
	}
}