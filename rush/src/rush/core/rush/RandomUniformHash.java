/*
 * @author : Kamesh
 */

package rush.core.rush;

import java.util.Random;

public class RandomUniformHash {
	private static final Long LARGE_PRIME = 948701839L;
	private static final Long LARGE_PRIME2 = 6920451961L;

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(i + " -> " + hash(i, 2, 0));
		}
	}


	public static long mod(long l, long m) {
		if(m!=0)
		   return (l % m);
		else
			return l;
	}

	
	public static long generatePrime(long z) {
		long prime;
		boolean bool = true;
		long rand = new Random(z * z << 3).nextInt();
		long initVal = z + 1 * rand;
		initVal = initVal < 0 ? -initVal : initVal;
		if (initVal % 2 == 0) {
			initVal = initVal + 1;
		}
		for (long i = initVal;; i = i + 2) {
			for (int j = 3; j < Math.sqrt(i); j++) {
				if (i % j == 0) {
					bool = false;
					break;
				}
			}
			if (bool == false) {
				bool = true;
			} else {
				prime = i;
				break;
			}
		}
		return prime;
	}

	
	
	/*
	 * Random Number generator with hash
	 */
	public static int hash(int ObjectID, long clusterID, double i) {
		// Spread out values
		long scaled = (long) ObjectID * LARGE_PRIME;

		// Fill in the lower bits
		long shifted = scaled + LARGE_PRIME2;

		// Add to the lower 32 bits the upper bits which would be lost in
		// the conversion to an int.
		long filled = shifted + ((shifted & 0xFFFFFFFF00000000L) >> 32);

		// Pare it down to 31 bits in this case. Replace 7 with F if you
		// want negative numbers or leave off the `& mask` part entirely.
		int masked = (int) (filled & 0x7FFFFFFF);
		return masked;
	}

	public static int hash(long x, long clusterID, double i, int k) {
		// Spread out values
		long scaled = (long) x * LARGE_PRIME;

		// Fill in the lower bits
		long shifted = scaled + LARGE_PRIME2;

		// Add to the lower 32 bits the upper bits which would be lost in
		// the conversion to an int.
		long filled = shifted + ((shifted & 0xFFFFFFFF00000000L) >> 32);

		// Pare it down to 31 bits in this case. Replace 7 with F if you
		// want negative numbers or leave off the `& mask` part entirely.
		int masked = (int) (filled & 0x7FFFFFFF);
		return masked;
	}
}
