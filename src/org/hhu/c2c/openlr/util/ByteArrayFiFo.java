package org.hhu.c2c.openlr.util;

/**
 * <code>ByteArrayFiFo</code> can be used to access a byte array as if it were a
 * linked list, following the first in, first out principle. It supports various
 * reading operations. The default operation would be {@link #pop()}, which
 * returns the value of the byte array and moves the marker one position to the
 * right. In contrast {@link #peek()} returns the value but without changing the
 * marrker position, allowing multiple operations on the same byte (array).
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class ByteArrayFiFo {

	/** Holds the byte array */
	final private byte[] array;

	/** Hols the current position */
	private int position;

	/**
	 * Constructs a new {@link ByteArrayFiFo}
	 * 
	 * @param array
	 *            the byte array that sould be accessed
	 */
	public ByteArrayFiFo(final byte[] array) {
		if (array == null) {
			throw new IllegalArgumentException("Byte array mustn't be null."); //$NON-NLS-1$
		}

		this.array = array.clone();
		position = 0;
	}

	/**
	 * Returns the number of bytes that can still by obtained by calling
	 * {@link #pop()}
	 * 
	 * @return the number of bytes that can still by obtained by calling
	 *         {@link #pop()}
	 */
	public int capacity() {
		return size() - position;
	}

	/**
	 * Peeks at current position in the byte array, returning the value but not
	 * changing the position of the marker, allowing for multiple operations on
	 * the same byte.
	 * 
	 * @see #peek(int)
	 * 
	 * @return the byte at the current position
	 */
	public byte peek() {
		return array[position];
	}

	/**
	 * Peeks at the next <code>size</code> bytes of the byte array, returning
	 * them as a new byte array but not changing the position of the marker,
	 * allowing for multiple operations on the same bytes.
	 * 
	 * @see #peek()
	 * 
	 * @param size
	 *            the number of bytes to peek at
	 * @return the byte at the current position
	 * @throws IndexOutOfBoundsException
	 *             if the size is bigger than the remaining {@link #capacity()}
	 */
	public byte[] peek(final int size) {
		if (size() - size < position) {
			throw new IndexOutOfBoundsException();
		}

		byte[] buffer = new byte[size];
		System.arraycopy(array, position, buffer, 0, size);
		return buffer;
	}

	/**
	 * Returns the byte value at the current position of the byte array and
	 * increments the position counter.
	 * 
	 * @see #pop(int)
	 * 
	 * @return the byte at the current position, incrementing counter
	 * @throws IndexOutOfBoundsException
	 *             if you try to call {@link #pop()} when the end of the array
	 *             is reached
	 */
	public byte pop() {
		if (position < size()) {
			position++;
			return array[position - 1];
		}
		throw new IndexOutOfBoundsException();
	}

	/**
	 * Returns the given number of bytes as a byte array and increments the
	 * position counter witht the given size
	 * 
	 * @see #pop()
	 * 
	 * @param size
	 *            the number of bytes to pop
	 * @return the byte array starting at the current position, ending at
	 *         current position + size, incrementing the position
	 * @throws IndexOutOfBoundsException
	 *             if the size is bigger than the remaining {@link #capacity()}
	 */
	public byte[] pop(final int size) {
		if (size() - size < position) {
			throw new IndexOutOfBoundsException();
		}

		byte[] buffer = new byte[size];
		System.arraycopy(array, position, buffer, 0, size);
		position += size;
		return buffer;
	}

	/**
	 * Returns the length of the original byte array
	 * 
	 * @return the length of the original byte array
	 */
	public int size() {
		return array.length;
	}
}
