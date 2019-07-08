/*
* Class: Game
* Description: The use will be able to rent a game and return it. 
* The class calculates the fees when a game is borrowed or returned 
* Author: Labiba Islam - 3694372
*/
public class Game extends Item {
	private String[] platforms;
	private boolean extended;
	private static final int RENTAL_PERIOD = 14;
	private static final int RENTAL_FEE = 20;

	public Game(String id, String title, String genre, String description,
			String[] platforms) throws IdException {
		super("G_" + id, title, genre, description, RENTAL_FEE);
		this.platforms = platforms;
		extended = false;
	}

	public String[] getPlatforms() {
		return this.platforms;
	}

	public boolean getExtended() {
		return extended;
	}

	public void setExtended(boolean extended) {
		this.extended = extended;
	}

	public int getRentalPeriod() {
		return RENTAL_PERIOD;
	}

	@Override
	public double borrow(String memberId, DateTime borrowDate)
			throws BorrowException {

		double rentalFee = 0.0;

		try {
			rentalFee = super.borrow(memberId, borrowDate);
		} catch (BorrowException be) {
			throw new BorrowException(
					"The game cannot be borrowed since it is already borrowed.");
		}
		return rentalFee;

	}

	/*BEGIN
	 * take id and check if the game has been borrowed or not
	 * if game is not currently borrowed, print message accordingly
	 * if game is currently borrowed,calculate the difference in days
	 * if difference in days is less than 0, print suitable error message
	 * if the game is currently borrowed and difference in days is greater than zero,
	 * check if difference in days is more than rental period,
	 * if so, show the late fee
	 * END
	 */
	@Override
	public double returnItem(DateTime returnDate) {
		double lateFee = 0;
		int diffDays = 0;
		int weeks = 0;
		HiringRecord currentlyBorrowed = super.getCurrentlyBorrowed();

		if (currentlyBorrowed == null) {
			System.out.println("The game is not on loan.");
		} else {
			diffDays = DateTime.diffDays(returnDate,
					currentlyBorrowed.getBorrowDate());

			if (diffDays < 0) {
				System.out.println(
						"Borrow date should be equal or less than return date.");
			}
		}

		if (currentlyBorrowed == null || diffDays < 0) {
			return Double.NaN;
		}
		// calculating late fee if game is borrowed
		weeks = (int) (diffDays) / 7;

		if (diffDays > RENTAL_PERIOD) {
			if (diffDays - RENTAL_PERIOD < 7) {
				lateFee = diffDays - RENTAL_PERIOD;
			} else {
				lateFee = (diffDays - 14) + (weeks - 2) * 5;

			}
			if (extended) {
				lateFee /= 2;
			}

		}

		currentlyBorrowed.returnItem(returnDate, lateFee);
		super.setCurrentlyBorrowed(null);
		this.extended = false;
		return lateFee;
	}

	private String getPlatformsInString() {
		String platformsInString = "";

		for (int i = 0; i < platforms.length; i++) {
			if (platforms[i] == null) {
				break;
			}

			platformsInString += platforms[i] + ", ";

		}
		return platformsInString.substring(0, platformsInString.length() - 2);
	}

	@Override
	public String getDetails() {
		String onLoan;

		if (super.getCurrentlyBorrowed() != null && extended == false) {
			onLoan = "YES";
		} else if (extended) {
			onLoan = "EXTENDED";
		} else {
			onLoan = "NO";
		}

		String details = new String();
		details += super.getDetails();

		details += String.format("%-25s%s\n", "Platforms: ",
				getPlatformsInString());
		details += String.format("%-25s%s\n", "Rental Period: ",
				RENTAL_PERIOD + " days");
		details += String.format("%-25s%s\n", "On loan: ", onLoan);

		HiringRecord[] hireHistory = super.getHireHistory();

		if (hireHistory[0] != null) {
			details += String.format("BORROWING RECORD\n");
			details += String
					.format("-----------------------------------------\n");

			for (int i = 0; i < hireHistory.length; i++) {
				if (hireHistory[i] == null) {
					break;
				} else {
					details += hireHistory[i].getDetails();
					details += String.format(
							"-----------------------------------------\n");
				}
			}
		}

		return details.toString();

	}

	public String toString() {
		String onLoan = getCurrentlyBorrowed() != null ? "Y"
				: extended ? "E" : "N";
		return super.toString() + ":" + 20 + ":" + getPlatformsInString() + ":"
				+ onLoan;
	}

}
