import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class textAnalysis {
	 private static HashTable read_gram(String file, int n, HashTable H) {
		   Scanner sc2 = null;
		   //int n = 3;
		   String[] gram = new String[n];
		   int count = 0;
		   boolean isFirst = true;
		   
		   try {
		          sc2 = new Scanner(new File(file));
		   } 
		   catch (FileNotFoundException e) {
		          e.printStackTrace();  
		   }
		   while (sc2.hasNextLine()) {		//Read file line by line
		        Scanner s2 = new Scanner(sc2.nextLine());
		   
		        while(s2.hasNext()){
		        	String s = s2.next();
		        	 s = s.toLowerCase();
		             s = s.replaceAll("[*%$!?:.(),;]","");  
		             if(s.matches(".*[a-z].*")){
		            	 if(isFirst){
		            		 if(count < n){
			            		 gram[count] = s;
			            		 count++;
			            	 }
			            	 else{
			            		 //store old gram into string 
			            		 String newString = "";
			            		 for(int i=0; i < n; i++){ 
			            			 if(i == n-1)
			            				 newString += gram[i];
			            			 else
			            			 newString += gram[i]+" "; 
			            		 }
			            		 //System.out.println(newString);
			            		 H.insert_c(newString);   		//Store ngram in table
			            		 //and start new gram
			            		 String[] new_gram = new String[n];
			            		 System.arraycopy(gram, 1, new_gram, 0, n-1);  //Shift
			            		 gram = new_gram;
			            		 gram[n-1] = s;				//complete new gram
			            		 isFirst = false;
			            	 }
		            	 }
		            	 else{
		            		//store old gram into string 
		            		 String newString = "";
		            		 for(int i=0; i < n; i++){
		            			 if(i == n-1)
		            				 newString += gram[i];
		            			 else
		            			 newString += gram[i]+" "; 
		            		 }
		            		 //System.out.println(newString);
		            		 H.insert_c(newString);   		//Store ngram in table
		            		 //and start new gram
		            		 String[] new_gram = new String[n];
		            		 System.arraycopy(gram, 1, new_gram, 0, n-1);  //Shift
		            		 gram = new_gram;
		            		 gram[n-1] = s;				//complete new gram
		            	 }
		            	 
		             } 
		        }
		        //Do check for load factor
		        while(H.computeLoad() > 0.05)
		        	H = reSizeTable(H);
		   }
		   
		   return H;
	   }
		
		public static HashTable reSizeTable(HashTable Hash){
			int m = Hash.getSize();
			//Create new buffer table initialized to null
			HashTable new_H = new HashTable(2*m+1);
			//Now re-hash elements of H into new_H
			for(int i=0; i<m; i++){		//iterate through every element in array of nodes
				for(iNode t=Hash.H[i]; t!=null; t=t.next){  //iterate through every node in list
					new_H.insert_c(t.ngram);
					//System.out.println(t.ngram);
				}
			}
			
			return new_H;
		}
		
		public static void printKGrams(HashTable Hash, int k){
			System.out.printf("Below is a list of grams that appear %d times:\n", k);
			//1.- Traverse each list in the HashTable
			for(int i=0; i<Hash.getSize(); i++){
				//2.- for every string in list, compare the string count value to k
				for(iNode t=Hash.H[i]; t!=null; t=t.next){
					if(t.count == k)
//						*If equivalent, print the string
						System.out.println(t.ngram);
				}
			}	
		}
		
		public static void analyzeMyFile(String path, int n, int k){
			HashTable H = new HashTable(3011);
		
			H = read_gram(path, n, H);
			printKGrams(H, k);
			printStats(H);
			System.out.println("Table load is:  "+ H.computeLoad());
		}
		
		public static void printStats(HashTable Hash){
			System.out.printf("Longest list in table is %d grams long\n",Hash.computeLongest());
			System.out.printf("The table is %f%% empty\n",Hash.computePctEmpty());
			System.out.printf("The standard deviation of list lengths in table is:  %f\n",Hash.computeStd());
		}
}
