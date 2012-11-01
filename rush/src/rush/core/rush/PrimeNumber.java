/*
 * @author : AnilKumar
 */

package rush.core.rush;

import java.util.Random;

public class PrimeNumber {

	private long[] primePool = new long[] {948701839L, 6920451961L,325837999L, 1237202969L,700204117L};
	
	public static void main(String[] args) {
		
		long a = 1000;
		long rand = new Random(a*a<<3).nextInt();
		boolean bool = true;
		long initVal = a+1*rand;
		if(initVal%2 == 0){
			initVal = initVal +1;
		}
		for(long i = initVal  ; ; i=i+2 ){
			 for (int j = 3 ; j< Math.sqrt(i) ; j++ ){
				 if(i%j == 0){
					 bool = false; 
					 break;
				 }
			 }
			if(bool == false){
				 bool = true ;
			 } else {
				 System.out.println(i);
				 break;
			 }				 
		}
	}
}
