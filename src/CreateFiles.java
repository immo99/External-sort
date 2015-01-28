import java.io.*;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
* Basic external sort for a file of unsorted integers.  Results in a sorted text file. Can be made more general for any comparable data type.  Note, 
* to change the amount of numbers written to a temp file, change the numIntegers variable.  Default is 1000.  Also,  to
* run on your machine you will have to change the file path locations for the TempFiles.
*
* @author Mike Hoyt
*/

public class CreateFiles {
	private LinkedList<File> fileList;
	private final int numIntegers = 1000;
	private int mergeCount;

	public CreateFiles(String filename) {
		fileList = new LinkedList<File>();
		readWriteFile(filename);
		mergeCount = 0;
		merge("FinalOutput");
		System.out.println("Finished!");
	}

	/**
	* reads a finite amount of integers from the main file, sorts them, then writes them to a seperate, temporary file.
    * @param filename: The file to read from.
	*/
	private void readWriteFile(String filename) {
		int[] arr = new int[numIntegers];
		boolean hasSorted = false;
		int currentCount = 0;
		DataInputStream dIn = null;
		try {
			dIn = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(filename))));


			while( true ) {
				currentCount = 0;
				hasSorted = false;
				for( int i = 0; i < numIntegers; ++i ) {
					arr[i] = dIn.readInt();
					currentCount++;
				}
				quicksort(arr, 0, currentCount - 1);
				fileList.add(writeToFile(arr));
				hasSorted = true;

			}
		}
		catch (EOFException e) {
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println(fileList.size());
		if( !hasSorted ) {
			quicksort(arr, 0, currentCount - 1);
			fileList.add(writeToFile(arr));
		}

		//check that it hasn't sorted.
	}

	/**
	*  Basic quicksort implementation.
	*/
	private void quicksort(int[] arr, int left, int right) {
		int index = partition(arr, left, right);

		if(left < index - 1) 
			quicksort(arr, left, index - 1);
		if( right > index )
			quicksort(arr, index, right);
	}

	/**
    *  Partition for quicksort implementation.
	*/
	private static int partition(int[] arr, int left, int right) {
		int pivot = arr[(left+right)/2];
		int temp;

		while( left <= right ) {
			while( arr[left] < pivot )
				left++;
			while( arr[right] > pivot )
				right--;

			if( left <= right ) {
				temp = arr[left];
				arr[left] = arr[right];
				arr[right] = temp;
				left++;
				right--;
			}
		}
		return left;
	}

	/**
	*  Writes array to temporary file.  Note:  You can change the file path to work on your own machine.
	*/
	private File writeToFile(int[] arr) {
		File temp = null;
		try {
			temp = File.createTempFile("tmp", null, new File("/home/mike/Documents/Practice/ExternalSort/TempFiles"));
			temp.deleteOnExit();

			DataOutputStream dOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(temp)));

			for(int i = 0; i < numIntegers; ++i) {
				dOut.writeInt(arr[i]);
			}

			dOut.flush();
			dOut.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return temp;
	}


		/**
		*  Used to write the actual numbers to a file, rather than binary representation.
		*/
		private File writeToFileTEXT(int[] arr) {
		File temp = null;
		try {
			temp = File.createTempFile("tmp", null, new File("/home/mike/Documents/Practice/ExternalSort/TempFiles"));
			temp.deleteOnExit();

			BufferedWriter bW  = new BufferedWriter(new FileWriter(temp));
			for(int i = 0; i < numIntegers; ++i) {
				String tempLine = String.valueOf(arr[i]) + " ";
				bW.write(tempLine, 0, tempLine.length() );
			}

			bW.flush();
			bW.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return temp;
	}


	/**
	*  Final output in real number format.
	*/
	private void writeToFileFinish() {
		try {
			FileWrapper wrapper = new FileWrapper(fileList.getFirst());
			BufferedWriter bW = new BufferedWriter(new FileWriter((new File("ReadableOutput"))));

			while(wrapper.hasNext()) {
				String tempLine = String.valueOf(wrapper.next()) + " ";
				bW.write(tempLine, 0, tempLine.length() );
			}
			bW.flush();
			bW.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	/**
    *  Merges all of the temporary files together.
	*/
	private void merge(String endFileName) {
		DataOutputStream dOut = null;
		File temp = null;
		boolean returnFlag = false;

		try{
			if( fileList.size() == 1 ) {
				File endFile = new File(endFileName);
				FileWrapper wrapper = new FileWrapper(fileList.removeFirst());
				dOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(endFile)));
				returnFlag = true;
				int count = 0;
				while(wrapper.hasNext()) {
					count++;
					dOut.writeInt(wrapper.next());
				}
				System.out.println("count "+ count);
				dOut.flush();
				dOut.close();
				fileList.add(endFile);
				return;
			}
			else if( fileList.size() == 2 ) {
				temp = new File(endFileName);
				returnFlag = true;
			}
			else {
				temp = File.createTempFile("tmp", null, new File("/home/mike/Documents/Practice/ExternalSort/TempFiles"));
			}
			
			FileWrapper wrapper1 = new FileWrapper(fileList.removeFirst());
			FileWrapper wrapper2 = new FileWrapper(fileList.removeFirst());
			dOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(temp)));
			int count = 0;
			int one = wrapper1.next();
			int two = wrapper2.next();


			while(one != -1 && two != -1) {
				count++;

				if( one >= two ) {
					dOut.writeInt(two);
					two = wrapper2.next();
				}
				else {
					dOut.writeInt(one);
					one = wrapper1.next();

				}
			}

			if( two == -1 ) {
				dOut.writeInt(one);
				while( wrapper1.hasNext() ) {
					count++;
					dOut.writeInt(wrapper1.next() );
				}
			}
			else {
				dOut.writeInt(two);
				while( wrapper2.hasNext() ) {
					count++;
					dOut.writeInt(wrapper2.next() );
				}
			}
			if(returnFlag)
				System.out.println("count " + count);
			dOut.flush();
			dOut.close();
			fileList.add(temp);
		}
		catch (EOFException e) {
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		mergeCount++;
		if(!returnFlag) {
			merge(endFileName);
		}
	}

	public static void main(String[] args) {
		new CreateFiles("/home/mike/Documents/Practice/ExternalSort/inputFiles/million");
	}
}