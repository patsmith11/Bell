/*Member class, threads
 * PROJECT BASED OFF OF KYLE SCOVILL's SOLUTION https://github.com/kscovill/BellChoir 
 */

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class Member implements Runnable {
	private AudioFormat af;
	private boolean play;
	private boolean runThread;
	private Note assignedNote;
	private final Thread t;
	private NoteLength currentLength;

	/**
	 * Constructor for a member
	 * 
	 * @param assigned
	 *            -the note that this assiged player can play.
	 * @param audio
	 *            -AudioFormat for playing the note
	 */
	Member(Note assigned, AudioFormat audio) {
		af = audio;
		play = false;
		runThread = true;
		assignedNote = assigned;

		t = new Thread(this);
		t.start();

	}

	/**
	 * run method for thread
	 */
	@Override
	public void run() {
		while (runThread) {
			try {
				acquire();
				// System.out.println("Got Permission");
			} catch (Exception ignoed) {

			}

			if (play) {
				// System.out.println("Really playing");
				try (final SourceDataLine line = AudioSystem
						.getSourceDataLine(af)) {
					line.open();
					line.start();

					playNote(line, new BellNote(assignedNote, currentLength));

					line.drain();
					release();
				} catch (Exception ignored) {

				}
			}
		}

	}

	/**
	 * lets the member play assigned note for how long told to
	 * 
	 * @param length
	 *            - how long to play the note
	 * @throws InterruptedException
	 */
	public synchronized void ringBell(NoteLength length)
			throws InterruptedException {
		currentLength = length;
		play = true;
		notifyAll();
		while (play) {
			wait();
		}
	}

	/**
	 * stops threads from running
	 */
	public void songOver() {
		runThread = false;
		release();
	}

	/**
	 * joins threads
	 */
	public void stopThread() {
		try {
			t.join();
		} catch (Exception ignored) {

		}
	}

	/**
	 * to prevent overlap of notes playing, the Member class has a mutex, so
	 * only one member can have play be true at once
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void acquire() throws InterruptedException {
		if (!play) {
			wait();
		}
	}

	/**
	 * changes play to false and notify other threads, so they can play
	 */
	public synchronized void release() {
		play = false;
		notifyAll();
	}

	/**
	 * Mostly taken from Nate Williams, added synchronized so only one thread
	 * can play at once, and now passed sourceline and bellnote to play
	 * 
	 * @param line
	 * @param bn
	 */
	public synchronized void playNote(SourceDataLine line, BellNote bn) {

		final int ms = Math.min(bn.length.timeMs(),
				Note.MEASURE_LENGTH_SEC * 1000);
		final int length = Note.SAMPLE_RATE * ms / 1000;
		line.write(bn.note.sample(), 0, length);
		line.write(Note.REST.sample(), 0, 100);
	}
}
