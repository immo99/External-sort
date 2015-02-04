import java.io.*;

/**
* Used to hide the details of getting the next integer in a file.  
*
* @author Mike Hoyt
*/

public class FileWrapper {
	private int next;
	private boolean hasNext;
	private boolean hasNextFlag;
	private DataInputStream dIn;


	/**
	* @param file: the File to connect to.
	*/
	public FileWrapper(File file) {
		next = -1;
		hasNext = false;
		dIn = null;
		hasNextFlag = false;

		connect(file);
		hasNext();
	}

	/**
	* Gets the next int in the file.
	*/
	public int next() {
		if( !hasNext )
			return -1;
		else {
			hasNextFlag = false;
			int toReturn = next;
			hasNext();
			return toReturn;
		}
	}

	/**
	* Returns if there is another int left in the file.
	*/
	public boolean hasNext() {
		if( hasNextFlag )
			return hasNext;
		else {
			try {
				next = dIn.readInt();
				hasNext = true;
			}
			catch(EOFException e) {
				hasNext = false;
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		hasNextFlag = true;
		return hasNext;
	}

	/**
	* Connects a DataInputStream to file f.
	*/
	private void connect(File f) {
		try {
			dIn = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
		}
		catch( IOException e ) {
			e.printStackTrace();
		}
	}
}