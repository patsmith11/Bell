/*Tone/Main class. 
 * PROJECT BASED OFF OF KYLE SCOVILL's SOLUTION https://github.com/kscovill/BellChoir 
 * 
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Tone {
	private static Member[] mems = new Member[14];
	private static ArrayList<BellNote> notesToPlay = new ArrayList<BellNote>();
	final static AudioFormat af = new AudioFormat(Note.SAMPLE_RATE, 8, 1, true,
			false);
	static boolean validInput = true;

	/**
	 * 
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		Tone mainTone = new Tone(af);
		cueSong(args[0]);
		if (notesToPlay.contains(null)) {
			System.exit(0);
		}

		makeMembers();
		mainTone.playSong(notesToPlay);

		for (int idx = 0; idx < mems.length; idx++) {
			mems[idx].songOver();
		}
		for (int idx = 0; idx < mems.length; idx++) {
			mems[idx].stopThread();
		}
	}

	/**
	 * Creates Members and populates array
	 */

	private static void makeMembers() {

		int idx = 0;
		for (Note note : Note.values()) {
			// System.out.println("making thread " + idx);
			mems[idx] = new Member(note, af);
			idx++;
		}
	}

	/**
	 * reads the file, and translates to notes
	 * 
	 * @param in
	 *            - file name being passed in
	 */
	private static void cueSong(String in) {
		final File f = new File(in);
		if (f.exists()) {
			try (FileReader fr = new FileReader(f);
					BufferedReader br = new BufferedReader(fr)) {
				String readNote = null;
				int count = 0;
				while ((readNote = br.readLine()) != null) {
					count++;
					BellNote note = getNote(readNote);
					if (note != null) {
						notesToPlay.add(note);
					} else {
						System.out.println("Error in song file. On line: "
								+ count + ". Reading :" + readNote);
						notesToPlay.add(note);
					}
				}
			} catch (Exception ignored) {

			}
		} else {
			System.out.println("Could not find the file: " + in
					+ ". Please check you file name and location.");
		}
	}

	private final AudioFormat audioForm;

	Tone(AudioFormat af) {
		this.audioForm = af;
	}

	/**
	 * plays song
	 * 
	 * @param notes
	 *            - list of notes to play
	 * @throws LineUnavailableException
	 */
	private void playSong(ArrayList<BellNote> notes)
			throws LineUnavailableException {
		try (final SourceDataLine l = AudioSystem.getSourceDataLine(audioForm)) {
			l.open();
			l.start();
			int position = 0;
			for (BellNote note : notes) {
				// System.out.println("Playing note:" + position);
				try {
					manageMembers(note, position);
				} catch (InterruptedException ignored) {

				}
				position++;
			}
		}

	}

	/**
	 * tells members who and when to play
	 * 
	 * @param note
	 *            - BellNote to be played
	 * @param pos
	 *            - position in the notesToPlay array list
	 * @throws InterruptedException
	 */
	private synchronized void manageMembers(BellNote note, int pos)
			throws InterruptedException {

		switch (note.note) {
		case REST:
			mems[0].ringBell(notesToPlay.get(pos).length);
			break;
		case A4:
			mems[1].ringBell(notesToPlay.get(pos).length);
			break;
		case A4S:
			mems[2].ringBell(notesToPlay.get(pos).length);
			break;
		case A5:
			mems[13].ringBell(notesToPlay.get(pos).length);
			break;
		case B4:
			mems[3].ringBell(notesToPlay.get(pos).length);
			break;
		case C4:
			mems[4].ringBell(notesToPlay.get(pos).length);
			break;
		case C4S:
			mems[5].ringBell(notesToPlay.get(pos).length);
			break;
		case D4:
			mems[6].ringBell(notesToPlay.get(pos).length);
			break;
		case D4S:
			mems[7].ringBell(notesToPlay.get(pos).length);
			break;
		case E4:
			mems[8].ringBell(notesToPlay.get(pos).length);
			break;
		case F4:
			mems[9].ringBell(notesToPlay.get(pos).length);
			break;
		case F4S:
			mems[10].ringBell(notesToPlay.get(pos).length);
			break;
		case G4:
			mems[11].ringBell(notesToPlay.get(pos).length);
			break;
		case G4S:
			mems[12].ringBell(notesToPlay.get(pos).length);
			break;
		}
	}

	/**
	 * 
	 * 
	 * @param in
	 *            -Line currently being parsed
	 * @return
	 */
	private static BellNote getNote(String in) {
		// System.out.println("Reading File");
		String[] parts = in.split("\\s+");
		NoteLength l = null;
		Note n = null;
		if (parts.length == 2) {

			switch (parts[0]) {
			case "REST":
				n = Note.REST;
				// System.out.println("Assigned Rest");
				break;
			case "A4":
				n = Note.A4;
				// System.out.println("Assigned a4");
				break;
			case "A4S":
				n = Note.A4S;
				// System.out.println("Assigned a4s");
				break;
			case "A5":
				n = Note.A5;
				// System.out.println("Assigned a5");
				break;
			case "B4":
				n = Note.B4;
				// System.out.println("Assigned b4");
				break;
			case "C4":
				n = Note.C4;
				// System.out.println("Assigned c4");
				break;
			case "C4S":
				n = Note.C4S;
				// System.out.println("Assigned c4s");
				break;
			case "D4":
				n = Note.D4;
				// System.out.println("Assigned d4");
				break;
			case "D4S":
				n = Note.D4S;
				// System.out.println("Assigned d4s");
				break;
			case "E4":
				n = Note.E4;
				// System.out.println("Assigned e4");
				break;
			case "F4":
				n = Note.F4;
				// System.out.println("Assigned f4");
				break;
			case "F4S":
				n = Note.F4S;
				// System.out.println("Assigned f4s");
				break;
			case "G4":
				n = Note.G4;
				// System.out.println("Assigned g4");
				break;
			case "G4S":
				n = Note.G4S;
				// System.out.println("Assigned g4s");
				break;
			default:
				System.out.println("Invalid Note");
				validInput = false;
				return null;
			}
			// not a valid note

			switch (parts[1]) {
			case "1":
				l = NoteLength.WHOLE;
				// System.out.println("Assigned WHOLE");
				break;
			case "2":
				l = NoteLength.HALF;
				// System.out.println("Assigned HALF");
				break;
			case "4":
				l = NoteLength.QUARTER;
				// System.out.println("Assigned QUATER");
				break;
			case "8":
				l = NoteLength.EIGTH;
				// System.out.println("Assigned EIGTH");
				break;
			default:
				// not a valid length
				System.out.println("Invalid Length");
				validInput = false;
				return null;
			}

		}
		if (l != null && n != null) {
			BellNote noteToReturn = new BellNote(n, l);
			return noteToReturn;
		} else {
			return null;
		}
	}
}

//following code written by Nate Williams
class BellNote {
	final Note note;
	final NoteLength length;

	/**
	 * 
	 * creates a bell note
	 * 
	 * @param note
	 * @param length
	 */
	BellNote(Note note, NoteLength length) {
		this.note = note;
		this.length = length;
	}
}

enum NoteLength {
	NONEXISTANT(0.0f), WHOLE(1.0f), HALF(0.5f), QUARTER(0.25f), EIGTH(0.125f);

	private final int timeMs;

	/**
	 * 
	 * creates a NoteLength
	 * 
	 * @param length
	 */
	private NoteLength(float length) {
		timeMs = (int) (length * Note.MEASURE_LENGTH_SEC * 1000);
	}

	/**
	 * 
	 * returns the note length in milliseconds
	 * 
	 * @return int
	 */
	public int timeMs() {
		return timeMs;
	}
}

enum Note {
	// REST Must be the first 'Note'
	REST, A4, A4S, B4, C4, C4S, D4, D4S, E4, F4, F4S, G4, G4S, A5;

	public static final int SAMPLE_RATE = 48 * 1024; // ~48KHz
	public static final int MEASURE_LENGTH_SEC = 1;

	// Circumference of a circle divided by # of samples
	private static final double step_alpha = (2.0d * Math.PI) / SAMPLE_RATE;

	private final double FREQUENCY_A_HZ = 440.0d;
	private final double MAX_VOLUME = 127.0d;

	private final byte[] sinSample = new byte[MEASURE_LENGTH_SEC * SAMPLE_RATE];

	/**
	 * creates a note
	 */
	private Note() {
		int n = this.ordinal();
		if (n > 0) {
			// Calculate the frequency!
			final double halfStepUpFromA = n - 1;
			final double exp = halfStepUpFromA / 12.0d;
			final double freq = FREQUENCY_A_HZ * Math.pow(2.0d, exp);

			// Create sinusoidal data sample for the desired frequency
			final double sinStep = freq * step_alpha;
			for (int i = 0; i < sinSample.length; i++) {
				sinSample[i] = (byte) (Math.sin(i * sinStep) * MAX_VOLUME);
			}
		}
	}

	/**
	 * 
	 * returns the byte array for playing the note
	 * 
	 * @return byte[]
	 */
	public byte[] sample() {
		return sinSample;
	}
}
