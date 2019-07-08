
/*
* Class: MovieMaster
* Description: this class uses an array to store and manage items which
* includes movies and games.
* Author: Labiba Islam - 3694372
*/
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class MovieMaster {
	Scanner input = new Scanner(System.in);
	String userInput, itemId, title, genre, description, newRelease, itemType;
	String memberId;
	int returnDays, rentalFee;
	int advanceBorrowDays;
	DateTime currentDate = new DateTime();
	boolean exitingProgram = false;

	Item[] items = new Item[100];

	public MovieMaster() {

		try {
			new ItemFileReader().loadItems(items);
		} catch (IOException e) {
			System.out.println("No item data was loaded.");
		} catch (IdException e) {
			System.out.println("Error - " + e.getMessage());
		}
		menuSystem();
	}

	public void menuSystem() {

		do {
			System.out.println("\n***Movie Master System Menu***");
			System.out.printf("%-25s%s\n", "Add Item", "A");
			System.out.printf("%-25s%s\n", "Borrow Item", "B");
			System.out.printf("%-25s%s\n", "Return Item", "C");
			System.out.printf("%-25s%s\n", "Display Item", "D");
			System.out.printf("%-25s%s\n", "Seed Data", "E");
			System.out.printf("%-25s%s\n", "Exit Program", "X");
			System.out.println("Enter selection: ");
			userInput = input.nextLine();

			switch (userInput) {
			case "A":
			case "a":
				addItem();
				break;
			case "B":
			case "b":
				borrowItem();
				break;
			case "C":
			case "c":
				returnItem();
				break;
			case "D":
			case "d":
				getDetail();
				break;
			case "E":
			case "e":
				if (items[0] == null) {
					seedData();
				} else {
					System.out.println("Error- items already exists!");
				}
				break;
			case "X":
			case "x":
				if (items[0] != null) {
					System.out.println("Saving data to disk...");
					try {
						ItemFileWriter ifw = new ItemFileWriter();
						ifw.persistToFile(items);
					} catch (IOException io) {
						System.out.println("Error - " + io.getMessage());
					}
				}
				System.out.println("Exiting from program...");
				exitingProgram = true;
				break;
			}
		} while (exitingProgram == false);
	}

	public void seedData() {
		DateTime dt = new DateTime();
		try {
			items[0] = new Movie("AQP", "A Quiet Place", "Horror",
					"If they hear you they hunt you", false);
			items[1] = new Movie("FMG", "Forever My Girl", "Romance",
					"Find your way back home", false);
			items[1].borrow("AAA", dt);
			items[2] = new Movie("WWM", "Wonder Woman", "Action",
					"The future of justice begins with her", false);
			items[2].borrow("BBB", dt);
			items[2].returnItem(new DateTime(5));
			items[3] = new Movie("BBD", "Baby Driver", "Music",
					"All you need is one killer track", false);
			items[3].borrow("CCC", dt);
			items[3].returnItem(new DateTime(10));
			items[4] = new Movie("RMG", "Rampage", "Science fiction",
					"Big meets bigger", false);
			items[4].borrow("DDD", dt);
			items[4].returnItem(new DateTime(10));
			items[4].borrow("EEE", dt);
			items[5] = new Movie("AIW", "Avengers:Infinity War",
					"Science Fiction", "Infinity", true);
			items[6] = new Movie("LLL", "La La Land", "Romance",
					"Here's to the fools who dream", true);
			items[6].borrow("FFF", dt);
			items[7] = new Movie("DRS", "Doctor Strange", "Action",
					"The impossibilities are endless", true);
			items[7].borrow("GGG", dt);
			items[7].returnItem(new DateTime(1));
			items[8] = new Movie("TFP", "The Florida Project", "Drama",
					"Glorius and Gorgeous", true);
			items[8].borrow("HHH", dt);
			items[8].returnItem(new DateTime(3));
			items[9] = new Movie("BLk", "Blockers", "Comedy",
					"Parents can be such", true);
			items[9].borrow("III", dt);
			items[9].returnItem(new DateTime(3));
			items[10] = new Game("IGA", "Injustice Gods Among Us", "Fighting",
					"What if our heros?", new String[] { "Xbox 360", "PS4" });
			items[11] = new Game("FNT", "Fortnite", "Survival",
					"Build, Trap, Defend, Win",
					new String[] { "Xbox 360, PS4 , PC" });
			items[11].borrow("JJJ", dt);
			items[12] = new Game("COD", "Call of Duty", "First person shooter",
					"Modern Warefare", new String[] { "Xbox 360, PS4" });
			items[12].borrow("KKK", dt);
			items[12].returnItem(new DateTime(19));
			items[13] = new Game("WIT", "Witcher", "Action-RPG",
					"Hunt monsters with swords",
					new String[] { "Xbox 360, PS4, PC" });
			items[13].borrow("MMM", dt);
			items[13].returnItem(new DateTime(32));
		} catch (IdException ie) {
			System.out.println("Error - " + ie.getMessage());
		} catch (BorrowException be) {
			System.out.println("Error - " + be.getMessage());
		}

	}// seed data ends

	public void getDetail() {
		for (int p = 0; p < items.length; p++) {
			if (items[p] != null) {
				System.out.println(items[p].getDetails());
			} else {
				break;
			}
		}
	} // getDetail ends

	public void addItem() {

		boolean correctIdEntered = true;
		int p = 0;

		System.out.println("Enter id: ");
		itemId = input.nextLine();

		for (p = 0; p < items.length; p++) {
			if (items[p] != null) {
				String updaterdItemId = "";

				if (items[p] instanceof Movie) {
					updaterdItemId = "M_" + itemId;
				} else {
					updaterdItemId = "G_" + itemId;
				}

				if (items[p].getId().equals(updaterdItemId)) {
					System.out.println("Error - Id for " + updaterdItemId
							+ " already exists in the system.");
					correctIdEntered = false;
					break;
				}
			} else {
				break;
			}
		}

		if (correctIdEntered == true) {
			System.out.println("Enter title: ");
			title = input.nextLine();
			System.out.println("Enter genre: ");
			genre = input.nextLine();
			System.out.println("Enter description: ");
			description = input.nextLine();

			System.out.println("Movie or Game (M/G): ");
			itemType = input.nextLine();

			if (itemType.equals("M")) {

				System.out.println(
						"Is the movie a new release? Enter Y for yes and N for no: ");
				newRelease = input.nextLine();

				do {
					if (newRelease.isEmpty()) { //hit enter to exit to menu
						return;
					}
					if (!newRelease.equals("Y") && !newRelease.equals("N")) {
						System.out.println("Error -  You must enter Y or N");
						newRelease = input.nextLine();
					}

				} while (!newRelease.equals("Y") && !newRelease.equals("N"));

				try {
					items[p] = new Movie(itemId, title, genre, description,
							newRelease.equals("Y") ? true : false);
					System.out.println(
							"New movie added successfully for the movie entitled: "
									+ title);
				} catch (IdException ie) {
					System.out.println("Error - " + ie.getMessage());
				}

			} else if (itemType.equals("G")) {
				System.out.println("Enter Game Platforms: ");
				String platforms = input.nextLine();
				int tokenIdx = 0;
				String[] platformArray = new String[100];

				StringTokenizer platformTokens = new StringTokenizer(platforms,
						",");
				while (platformTokens != null
						&& platformTokens.hasMoreTokens()) {
					platformArray[tokenIdx++] = platformTokens.nextToken()
							.trim();
				}

				try {
					items[p] = new Game(itemId, title, genre, description,
							platformArray);
					System.out.println(
							"New game added successfully for the game entitled: "
									+ title);
				} catch (IdException ie) {
					System.out.println("Error - " + ie.getMessage());
				}

			} else {
				System.out.println(
						"Incorrect selection for Movie or Game (M/G). No item will be added!");
			}
		}

	} // addItem ends
	
	/*BEGIN
	 * Take in item id
	 * check if it is a movie or game
	 * if id is not found, print id is not found
	 * if id is found and it is a movie, ask for number of advance borrow days and show fee
	 * if it is game, ask for extended borrowing and show the fee
	 * update the required properites to make the object borrowed
	 * END
	 */
	public void borrowItem() {
		System.out.println("Enter id: ");
		itemId = input.nextLine();

		int p = 0;
		String updatedId = "";
		boolean idNotFound = false;
		double rentalFee = 0.0;

		while (items[p] != null) {

			if (items[p] instanceof Movie) {
				updatedId = "M_" + itemId;
			} else {
				updatedId = "G_" + itemId;
			}

			if (items[p].getId().equals(updatedId)) {
				idNotFound = false;
				break;
			} else {
				idNotFound = true;
			}
			p++;
		}

		if (idNotFound == true) {
			System.out.println("The item with id " + itemId + ", not found");
		} else {

			System.out.println("Enter member id: ");
			memberId = input.nextLine();
			System.out.println("Advance borrow (days): ");
			String advanceBorrowDays = input.nextLine();

			DateTime borrowDate = null;
			if (Integer.parseInt(advanceBorrowDays) > 0) {
				borrowDate = new DateTime(Integer.parseInt(advanceBorrowDays));
			} else {
				borrowDate = new DateTime();
			}

			DateTime dueDate = null;

			if (items[p] instanceof Movie) {
				dueDate = new DateTime(borrowDate,
						((Movie) items[p]).getIsNewRelease() ? 2 : 7);
			} else {
				System.out.println("Extened borrowing (Y/N)?: ");
				String extendedBorrowingOption = input.nextLine();

				((Game) items[p]).setExtended(
						extendedBorrowingOption.equalsIgnoreCase("Y") ? true
								: false);
				dueDate = new DateTime(borrowDate,
						((Game) items[p]).getRentalPeriod());
			}

			try {
				rentalFee = items[p].borrow(memberId, borrowDate);
				System.out.println("The item " + items[p].getTitle()
						+ " costs $" + rentalFee + " and is due on "
						+ dueDate.getFormattedDate());
			} catch (BorrowException be) {
				System.out.println("ERROR - " + be.getMessage());
			} catch (Exception e) {
				System.out.println("UNEXPECTED ERROR - " + e.getMessage());
			}
		}
	} // borrowItem ends here

	public void returnItem() {
		System.out.println("Enter id: ");
		itemId = input.nextLine();
		// to check if mvId exists

		int p = 0;
		String updatedId = "";
		boolean idNotFound = false;

		while (items[p] != null) {
			if (items[p] instanceof Movie) {
				updatedId = "M_" + itemId;
			} else {
				updatedId = "G_" + itemId;
			}

			if (items[p].getId().equals(updatedId)) {
				idNotFound = false;
				break;
			} else {
				idNotFound = true;
			}
			p++;
		}
		if (idNotFound == true) {
			System.out.println("The item with id " + itemId + ", not found");
		} else {
			if (items[p].getCurrentlyBorrowed() == null) {
				System.out.println("The item with id " + itemId
						+ " is NOT currently on loan");
			} else {

				String nodInLoanString; //no. of days in loan

				System.out.println("Enter number of days on loan: ");
				nodInLoanString = input.nextLine();
				DateTime returnDate = new DateTime(
						Integer.parseInt(nodInLoanString));

				double lateFee = items[p].returnItem(returnDate);

				if (lateFee != Double.NaN) {
					System.out.println("The total fee payable is $" + lateFee);
				} else {
					System.out.println("Error in returning. Check program..");
				}
			}
		}
	}// end of returnItem
}
