package client;

import java.nio.ByteBuffer;

/**
 * A class that represents a picture by saving bytes in a byte array. class for
 * handling and managing images in an efficient way. >>>>>>>
 * 392adf24998ad9b2d37bee17d5493acd5e49fc19
 *
 */
public class Image {
	private byte[] jpeg;
	private long timeStamp;

	/**
	 * Creates an instance of an image.
	 *
	 * @param jpeg
	 *            - The picture representation in bytes
	 * @param time
	 *            - The
	 */
	public Image(byte[] jpeg, byte[] time) {
		this.jpeg = jpeg;
		timeStamp = convertTime(time);
	}

	public byte[] getJPEG() {
		return jpeg;
	}

	/**
	 * Converts a byte[] to long.
	 * 
	 * @param time
	 *            - The byte[] value to be converted.
	 * @return the converted long value
	 */
	private long convertTime(byte[] time) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.put(time);
		buffer.flip();// need flip
		long imageTime = buffer.getLong();
		return imageTime;
	}

	public long getTime() {
		return timeStamp;
	}

	// för att testa synchronous!
	public void addTime(long time) {
		timeStamp += time;
	}
}
