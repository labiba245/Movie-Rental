/*
* Class: Item
* Description: The class represents items that can be borrowed and returned
* Author: Labiba Islam - 3694372
*/
abstract public class Item {
	private String id;
	private String title;
	private String description;
	private String genre;
	private double rentalFee;
	private int count = 0;
	private HiringRecord[] hireHistory;
	private HiringRecord currentlyBorrowed;

	public Item(String id, String title, String genre, String description,
			double rentalFee) throws IdException {

		String[] idTokens = id.split("_");

		if (idTokens[1] == null || (idTokens[1]).length() != 3) {
			throw new IdException(
					"The id you provided is invalid. Please provide a 3 digit id.");
		}

		this.id = id;
		this.title = title;
		this.description = description;
		this.genre = genre;
		this.rentalFee = rentalFee;
		this.hireHistory = new HiringRecord[10]; // only the last 10 records
		currentlyBorrowed = null;
	}

	/*
	 * BEGIN check if movie is currently borrowed or not if currently borrowed,
	 * throw borrow exception if not, add to hire history END
	 */
	public double borrow(String memberId, DateTime borrowDate)
			throws BorrowException {

		if (currentlyBorrowed != null) {
			throw new BorrowException(
					"The item cannot be borrowed since it is already borrowed.");
		} else {

			currentlyBorrowed = new HiringRecord(id, memberId, this.rentalFee,
					borrowDate);
			addToHiringHistory(currentlyBorrowed);
		}
		return this.rentalFee;

	}

	public abstract double returnItem(DateTime returnDate);

	public void addToHiringHistory(HiringRecord hiringRecord) {

		if (hireHistory[0] == null) {
			hireHistory[0] = hiringRecord;
		} else {
			int last = 0;
			int i;
			int movePos;

			while (last < hireHistory.length && hireHistory[last] != null) {
				last++;
			}
			// the array is full
			if (last == hireHistory.length) {
				movePos = last - 1;
			}
			// array not full
			else {
				movePos = last;
			}

			for (i = movePos; i > 0; i--) {
				hireHistory[i] = hireHistory[i - 1];
			}
			hireHistory[0] = hiringRecord;
		}
	}

	public HiringRecord getCurrentlyBorrowed() {
		return currentlyBorrowed;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getGenre() {
		return genre;
	}

	public String getDescription() {
		return description;
	}

	public HiringRecord[] getHireHistory() {
		return hireHistory;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setCurrentlyBorrowed(HiringRecord currentlyBorrowed) {
		this.currentlyBorrowed = currentlyBorrowed;
	}

	public String getDetails() {
		String details = new String();
		details += String.format("%-25s%s\n", "ID:", this.id);
		details += String.format("%-25s%s\n", "Title:", this.title);
		details += String.format("%-25s%s\n", "Genre:", this.genre);
		details += String.format("%-25s%s\n", "Description:", this.description);
		details += String.format("%-25s%s%s\n", "Standard Fee: ", "$",
				this.rentalFee);

		return details.toString();
	}

}