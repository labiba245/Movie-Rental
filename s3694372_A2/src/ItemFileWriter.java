/*
* Class: ItemFileWriter
* Description: Write with filewriter including borrowing records in the file
* Author: Labiba Islam - 3694372
*/
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class ItemFileWriter {

	public ItemFileWriter() {

	}
	/*BEGIN
	 * create two files called items.dat and items.dat_backup
	 * read items from  the array and persist to the file
	 * if the item is an instance of movie, persist movies
	 * if the item is an instance of game, persist games
	 END*/
	public void persistToFile(Item[] items) throws FileNotFoundException {

		PrintWriter pw1 = new PrintWriter(
				new FileOutputStream("items.dat", false));
		PrintWriter pw2 = new PrintWriter(
				new FileOutputStream("items.dat_backup", false));
		String record = null;
		int i = 0;

		while (items[i] != null) {

			Item item = items[i];
			if (item instanceof Movie) {
				record = persistMovie((Movie) item);
			} else if (item instanceof Game) {
				record = persistGame((Game) item);
			} else {
				System.out.println(item.getClass());
			}
			pw1.write(record);
			pw2.write(record);
			pw1.println();
			pw2.println();
			i++;
		}
		pw1.flush();
		pw2.flush();
		pw1.close();
		pw2.close();
	}

	private String persistMovie(Movie item) {

		StringBuffer sb = new StringBuffer();
		sb.append(item.getId() + "|");
		sb.append(item.getTitle() + "|");
		sb.append(item.getGenre() + "|");
		sb.append(item.getDescription() + "|");
		sb.append(item.getIsNewRelease() + "|");
		String hiringRecords = getHiringRecords(item);

		if (hiringRecords != null) {
			sb.append(hiringRecords);
		}
		return sb.toString();
	}

	private String persistGame(Game item) {
		StringBuffer sb = new StringBuffer();
		sb.append(item.getId() + "|");
		sb.append(item.getTitle() + "|");
		sb.append(item.getGenre() + "|");
		sb.append(item.getDescription() + "|");
		StringBuffer pb = new StringBuffer();

		String[] p = item.getPlatforms();
		int i = 0;
		for (i = 0; i < p.length; i++) {
			if (p[i] != null) {
				pb.append(p[i] + ":");
			} else {
				break;
			}
		}

		sb.append(pb.substring(0, pb.length() - 1) + "|");
		sb.append(item.getExtended() + "|");

		String hiringRecords = getHiringRecords(item);

		if (hiringRecords != null) {
			sb.append(hiringRecords);
		}

		return sb.toString();
	}

	private String getHiringRecords(Item item) {
		HiringRecord[] hiringRecords = item.getHireHistory();

		if (hiringRecords[0] == null) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hiringRecords.length; i++) {
			if (hiringRecords[i] == null) {
				break;
			}
			createHireRecord(sb, hiringRecords[i]);
			sb.append("|");
		}
		return sb.toString();
	}

	private StringBuffer createHireRecord(StringBuffer sb,
			HiringRecord hiringRecord) {
		sb.append(hiringRecord.getId() + ":");
		sb.append(hiringRecord.getRentalFee() + ":");
		sb.append(hiringRecord.getBorrowDate());

		if (hiringRecord.getReturnDate() != null) {
			sb.append(":" + hiringRecord.getReturnDate() + ":");
			sb.append(hiringRecord.getLateFee());
		}
		return sb;
	}
}