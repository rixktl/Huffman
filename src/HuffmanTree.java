import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * HuffmanTree that construct the tree by given data along with write and decode
 * functionalities
 */
public class HuffmanTree {

	private HuffmanNode root;
	private static final int NO_FREQUENCY = -1;
	private static final int NO_VALUE = -1;

	/**
	 * Node class for making tree
	 */
	private class HuffmanNode implements Comparable<HuffmanNode> {
		int count;
		int value;
		HuffmanNode left;
		HuffmanNode right;

		/**
		 * Construct the node
		 * 
		 * @param left
		 *            the left subtree
		 * @param right
		 *            the right subtree
		 * @param value
		 *            the containing value
		 * @param count
		 *            the frequency
		 */
		public HuffmanNode(HuffmanNode left, HuffmanNode right, int value, int count) {
			this.left = left;
			this.right = right;
			this.value = value;
			this.count = count;
		}

		/**
		 * Compare with other node
		 * 
		 * @param n
		 *            the other node to compare to
		 */
		@Override
		public int compareTo(HuffmanNode n) {
			// Check if frequency is less, equals or greater than
			if (this.count < n.count)
				return -1;
			else if (this.count > n.count)
				return 1;
			else
				return 0;
		}

	}

	/**
	 * Construct tree by an array
	 * 
	 * @param count
	 *            the array for constructing tree
	 */
	public HuffmanTree(int[] count) {
		PriorityQueue<HuffmanNode> p = new PriorityQueue<HuffmanNode>();

		// Loop for all values
		for (int i = 0; i < count.length; i++) {
			// Check if frequency greater than zero
			if (count[i] > 0)
				// Put into queue
				p.offer(new HuffmanNode(null, null, i, count[i]));
		}

		// End of file
		p.offer(new HuffmanNode(null, null, 256, 1));

		// Loop until queue has one node only
		while (p.size() != 1) {
			// Pop two nodes (smallest frequency)
			HuffmanNode f = p.poll();
			HuffmanNode s = p.poll();
			// Combine with one new node
			HuffmanNode n = new HuffmanNode(f, s, NO_VALUE, f.count + s.count);
			// Put back to queue
			p.offer(n);
		}

		// Get the only node in queue
		root = p.poll();

	}

	/**
	 * Write tree as output format
	 * 
	 * @param output
	 *            the stream to output to
	 */
	public void write(PrintStream output) {
		subWrite(output, root, "");
	}

	/**
	 * Write subtree as output format
	 * 
	 * @param output
	 *            the stream to output to
	 * @param n
	 *            the subtree
	 * @param order
	 *            the string for determining tree's flow
	 */
	private void subWrite(PrintStream output, HuffmanNode n, String order) {
		if (n != null && n.value > 0) {
			// Reached the leaf
			output.println(n.value);
			output.println(order);

		} else if (n != null) {
			// Recursion to subtree
			subWrite(output, n.left, order + "0");
			subWrite(output, n.right, order + "1");
		}
	}

	/**
	 * Construct tree by scanning data
	 * 
	 * @param codeInput
	 *            the scanner for scanning data
	 */
	public HuffmanTree(Scanner codeInput) {
		// Initialize root as a new node
		root = new HuffmanNode(null, null, NO_VALUE, NO_FREQUENCY);
		// Loop for all line of data
		while (codeInput.hasNextLine()) {
			// Extract data
			int n = Integer.parseInt(codeInput.nextLine());
			String code = codeInput.nextLine();
			construct(root, n, code);
		}
	}

	/**
	 * Construct node with specified location and structure
	 * 
	 * @param n
	 *            the node to begin with
	 * @param value
	 *            the value to be set
	 * @param code
	 *            the code to determine tree's flow
	 */
	private void construct(HuffmanNode n, int value, String code) {
		// Check if reached the specified location
		if (code.isEmpty()) {
			// Change value of the targeted node
			n.value = value;

		} else {
			// Check the first character to determine flow
			if (code.substring(0, 1).equals("0")) {
				// Check if node exist
				if (n.left == null) {
					// Make new node
					n.left = new HuffmanNode(null, null, NO_VALUE, NO_FREQUENCY);
				}

				// Recursion until reached the specified location
				construct(n.left, value, code.substring(1, code.length()));

			} else {
				// Check if node exist
				if (n.right == null) {
					// Make new node
					n.right = new HuffmanNode(null, null, NO_VALUE, NO_FREQUENCY);
				}

				// Recursion until reached the specified location
				construct(n.right, value, code.substring(1, code.length()));
			}
		}
	}

	/**
	 * Decode given tree format input and write into output
	 * 
	 * @param input
	 *            the source of tree formatted data
	 * @param output
	 *            the output to write to
	 * @param eof
	 *            the special value for determining end of file
	 */
	public void decode(BitInputStream input, PrintStream output, int eof) {
		HuffmanNode n = root;
		int r = input.readBit();

		// Loop until read through entire file
		while (n.value != eof && r != -1) {
			// Check if value is valid
			if (n.value != NO_VALUE) {
				// Write decoded value to output
				output.write(n.value);
				// Start from top of tree for next
				n = root;
			}

			// Check the direction of next node
			if (r == 0) {
				n = n.left;
			} else {
				n = n.right;
			}

			// Read next bit
			r = input.readBit();
		}

	}

}
