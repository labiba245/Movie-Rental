
/*
* Class: ItemFileReader
* Description: This class reads data from the file and load them to the 
* item array including the hiring history for each item.
* Author: Labiba Islam - 3694372
*/
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ItemFileReader {

	private static String MAIN_DELIM = "\\|";
	private static String HIRING_DELIM = ":";
	private static String PLATFORMS_DELIM = ":";
	private static String DATE_DELIM = "-";

	/*
	 * BEGIN 
	 * read items.dat file if not found, read items.dat_backup file if
	 * backup file not found, then throw exception otherwise, read each line and
	 * parse the line value and populate the item objects 
	 * END
	 */
	public Item[] loadItems(Item[] items) throws IOException, IdException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("items.dat"));
		} catch (FileNotFoundException ffe) {
			try {
				reader = new BufferedReader(new FileReader("items.dat_backup"));
				System.out.println("Loading from the backup file...");
			} catch (FileNotFoundException e) {
				throw new FileNotFoundException(
						"Error - Could not locate file to load");
			}
		}
		String line = reader.readLine();
		int index = 0;
		while (line != null) {
			items[index++] = parseLine(line);
			line = reader.readLine();
		}
		reader.close();
		return items;
	}

	private Item parseLine(String line) throws IdException {
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(MAIN_DELIM);
		String itemType = scanner.next();

		if (itemType.startsWith("M_")) {
			String id = itemType.substring(2);

			String title = scanner.next();
			String genre = scanner.next();
			String desc = scanner.next();
			String isNewRelStr = scanner.next();

			Movie movie = new Movie(id, title, genre, desc,
					new Boolean(isNewRelStr));
			addHiringRecords(scanner, movie);
			return movie;
		} else {
			String id = itemType.substring(2);
			Game game = new Game(id, scanner.next(), scanner.next(),
					scanner.next(), getPlatforms(scanner.next()));
			game.setExtended(new Boolean(scanner.next()));

			addHiringRecords(scanner, game);
			return game;
		}
	}

	private String[] getPlatforms(String platforms) {
		String[] p = platforms.split(PLATFORMS_DELIM);
		return p;
	}

	private void addHiringRecords(Scanner scanner, Item item) {
		DateTime returnDT = null;
		double lateFee = 0.0;
		boolean returnExist = false;
		String[] hrArray = new String[100];
		int i = 0;

		if (scanner.hasNext()) {
			// That means hiring records exist
			String record = scanner.next();
			while (record != null) {
				hrArray[i] = record;
				i++;
				if (scanner.hasNext()) {
					record = scanner.next();
				} else {
					record = null;
				}
			}
			scanner.close();

			i = i - 1;

			while (i >= 0) {
				returnExist = false;
				Scanner sc = new Scanner(hrArray[i]);
				sc.useDelimiter(HIRING_DELIM);
				String hiringId = sc.next();

				String[] hIds = hiringId.split("_");
				String itemId = hIds[0] + "_" + hIds[1];

				double rental = sc.nextDouble();
				DateTime borrowDT = DateTime.getDateTime(sc.next(), DATE_DELIM);

				if (sc.hasNext()) {
					returnDT = DateTime.getDateTime(sc.next(), DATE_DELIM);
					lateFee = sc.nextDouble();
					returnExist = true;
				}

				HiringRecord hiringRecord = new HiringRecord(itemId, hIds[2],
						rental, borrowDT);

				if (returnExist) {
					hiringRecord.returnItem(returnDT, lateFee);
					item.setCurrentlyBorrowed(null);
				} else {
					item.setCurrentlyBorrowed(hiringRecord);
				}

				item.addToHiringHistory(hiringRecord);
				sc.close();
				i--;
			}
		}
	}
}