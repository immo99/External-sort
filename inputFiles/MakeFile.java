import java.io.*;

public class MakeFile {

	public MakeFile(int num) {
		try {
		DataOutputStream dOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File("million"))));

		for( int i = 0; i < 1000000; ++i ) {
			int temp = (int)(Math.random() * Integer.MAX_VALUE);
			//String tempLine = String.valueOf(temp) + " ";
			//bOut.write(tempLine, 0, tempLine.length());
			dOut.writeInt(temp);
		}
		dOut.flush();
		dOut.close();
		} 
		catch( IOException e ) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new MakeFile(1000000);
	}
}