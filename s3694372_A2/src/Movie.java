/*
* Class: Movie
* Description: The class calculates the fees when a movie is borrowed or returned 
* Author: Labiba Islam - 3694372
*/
public class Movie extends Item {
	private boolean isNewRelease;
	private final double NEW_RELEASE_SURCHARGE = 2.00;

	public Movie(String id, String title, String genre, String description,
			boolean isNewRelease) throws IdException {
		super("M_" + id, title, genre, description, isNewRelease ? 5.00 : 3.00);
		this.isNewRelease = isNewRelease;
	}

	public boolean getIsNewRelease() {
		return isNewRelease;
	}

	@Override
	public double borrow(String memberId, DateTime borrowDate)
			throws BorrowException {

		double rentalFee = 0.0;
		try {
			rentalFee = super.borrow(memberId, borrowDate);
		} catch (BorrowException be) {
			throw new BorrowException(
					"The movie cannot be borrowed since it is already borrowed.");
		}

		return rentalFee;
	}// end of borrow
	
	
	/*BEGIN
	 * take in id and check if it currently borrowed or not
	 * if it is not currently borrowed, print suitable error message
	 * if currently borrowed, check if difference in days is greater than 0
	 * if not, print suitable error message
	 * if currently borrowed and difference in days is greater than o,
	 * show late fee, if applicable, depending on if the movie is a new release or not
	 END*/
	@Override
	public double returnItem(DateTime returnDate) {

		HiringRecord currentlyBorrowed = super.getCurrentlyBorrowed();
		int diffDays = 0;

		if (currentlyBorrowed == null) {
			System.out.println("The movie is not on loan.");
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

		double totalFee = 0;
		double lateFee = 0;
		// late fee for a movie is 50% of the rental fee for every day past the
		// due date.
		if (isNewRelease == false) {
			//calculating late fee for weekly rentals			
			if (diffDays > 7) {
				lateFee = ((currentlyBorrowed.getRentalFee()) * 0.5
						* (diffDays - 7)); 
				totalFee = 3.00 + lateFee;
			} else {
				totalFee = 3.0;
			}
		} else {
			//calculating late fee for new release rentals
			if (diffDays > 2) {
				lateFee = ((currentlyBorrowed.getRentalFee()) * 0.5
						* (diffDays - 2)); 
											
				totalFee = 3.0 + NEW_RELEASE_SURCHARGE + lateFee;
			} else {
				totalFee = 3.0 + NEW_RELEASE_SURCHARGE;
			}
		}

		currentlyBorrowed.returnItem(returnDate, lateFee);
		super.setCurrentlyBorrowed(null);

		return lateFee;
	} // end of return

	@Override
	public String getDetails() {
		String details = new String();
		details += super.getDetails();
		details += String.format("%-25s%s\n\n", "On loan:",
				(super.getCurrentlyBorrowed() != null ? "YES" : "NO"));
		details += String.format("%-25s%s\n", "Movie Type:",
				isNewRelease ? "New Release" : "Weekly");
		details += String.format("%-25s%s\n\n", "Rental Period:",
				isNewRelease ? "2 days" : "7 days");

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
	} // end of get details

	public String toString() {
		String fee = isNewRelease ? "5.0" : "3.0";
		String type = isNewRelease ? "NR" : "WK";
		String loanStatus = null != getCurrentlyBorrowed() ? "Y" : "N";

		return super.toString() + ":" + fee + ":" + type + ":" + loanStatus;
	}
}
