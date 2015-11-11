import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;


public class Main {


	public static void main(String[] args){
		String line;
		BufferedReader in;



		ArrayList<Paquet> packages = new ArrayList<Paquet>();
		ArrayList<String> result = new ArrayList<String>();

		try {
			in = new BufferedReader(new FileReader("fragments.txt"));
			line = in.readLine();

			while(line != null)
			{
				//REMOVE ALL SPACES FOR PROCESSING
				line = line.replace(" ", "");

				/*
				 * EXTRACT IP VERSION
				 */
				int position = 0;
				int subLength = 1;
				int ipVersion = extractValue(position, subLength, line);
				//System.out.println("IP VERSION: " + ipVersion);


				/*
				 * EXTRACT TOTAL LENGTH
				 */
				position = 4; 
				subLength = 4;
				int totalLength = extractValue(position, subLength, line);
				//System.out.println("Total Length: " + totalLength);

				/*
				 * EXTRACT IDENTIFICATION
				 */
				position = 8;
				subLength = 4;
				int identification = extractValue(position, subLength, line);
				//System.out.println("Identification: " + identification);


				/*
				 * EXTRACT "MORE FRAGMENTS" FLAG
				 */
				position = 12;
				subLength = 1;
				int flag = extractValue(position, subLength, line);
				int binaryLength = 4;
				int flagPosition = 1; // starts at 0
				boolean moreFragmentsFlag = extractFlag(flag, binaryLength, flagPosition);

				/*
				 * EXTRACT FRAGMENT OFFSET
				 */
				position = 12;
				subLength = 4;
				int bitStartPosition = 3;
				int fragmentOffset = extractValue(position, subLength, line, bitStartPosition);
				//System.out.println("Fragment Offset: " + fragmentOffset);

				/*
				 * EXTRACT MESSAGE
				 */
				position = 40;
				subLength = (totalLength - 20) * 2;
				String message = extractMessage(position, subLength, line);
				//System.out.println("Message: " + message);



				Message msg = new Message(fragmentOffset, totalLength, identification, message, moreFragmentsFlag);
				//System.out.println("startIndex: " + msg.getStartIndex());
				//System.out.println("endIndex: " + msg.getEndIndex());

				Paquet packageHoldingMsg = addMessageToPackageList(msg, packages);
				boolean isPkgComplete = packageHoldingMsg.isPackageComplete();

				if(isPkgComplete)
				{
					System.out.println("Detected and reassembled a complete package");
					String packageToWrite = packageHoldingMsg.reassemblePackage() + "\n";
					result.add(packageToWrite);
				}

				line = in.readLine();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		if(result.size() >0){
			try {
				PrintWriter out = new PrintWriter("data.txt");
				for(int i=0; i<result.size(); i++)
				{
					String newStr = addSpace(4,result.get(i));
					out.println(newStr);

				}
				System.out.println("Contents written to data.txt file in program directory.");
				out.close();
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("An error occured.. Contents not written to file.");

				// TODO Auto-generated catch block
				e.printStackTrace();
			}



		}

	}

	private static String addSpace(int interval, String str){
		String temp="";
		for(int i=0; i<str.length();i++){
			if((interval+1)%4 == 0){
				temp+=str.charAt(i)+" ";
			}
			else{
				temp+=str.charAt(i);
			}
		}
		return temp;
	}
	private static Paquet addMessageToPackageList(Message msg, ArrayList<Paquet> packages) {
		boolean found = false;
		int index = 0;

		//Check if the corresponding package already exists in the list
		for (int i=0; i<packages.size(); i++)
		{
			if(packages.get(i).getIdentification() == msg.getIdentification())
			{
				found = true;
				index = i;
				break;
			}
		}

		//if doesn't exist, create a new package, and add to the list (with the new message)
		if(!found)
		{
			Paquet pkg = new Paquet(msg.getIdentification());
			pkg.addMessage(msg);
			packages.add(pkg);
			return pkg;	
		}

		//if the package associated with the msg already exists, add the message to that package
		else 
		{
			return packages.get(index).addMessage(msg);			
		}
	}

	private static String extractMessage(int position, int subLength,
			String line) {

		String ss = line.substring(position, position + subLength);
		//System.out.println("SS" + ss);

		//System.out.println("VERSION: " + result);
		return ss;
	}

	private static int extractValue(int position, int subLength, String line,
			int bitStartPosition) {
		int iValue = extractValue(position, subLength, line);

		int binaryLength = subLength * 4;
		String strBinary = String.format("%" + binaryLength +"s", Integer.toBinaryString(iValue)).replace(" ", "0");

		String v = strBinary.substring(bitStartPosition);
		//System.out.println("SS" + ss);
		int result = Integer.parseInt(v, 2);
		//System.out.println("Fragment Offset: " + result+ " FRAGOFFSET RAW BITS: " + v);
		return result;

	}

	private static boolean extractFlag(int iValue, int binaryLength, int flagPosition) {
		String strBool = String.format("%" + binaryLength +"s", Integer.toBinaryString(iValue)).replace(" ", "0");
		//System.out.println("RESULT: " + strBool + "Flag: " + iValue);
		boolean result = false;
		try {
			result = convertCharToBool(strBool.charAt(flagPosition));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;	
	}

	private static boolean convertCharToBool(char c) throws Exception {
		if(c == '0')
			return false;
		else if (c == '1')
			return true;
		else
			throw new Exception("cannot convert " + c + " to boolean");

	}

	private static int extractValue(int position, int subLength, String line) {
		String ss = line.substring(position, position + subLength);
		//System.out.println("SS" + ss);
		int result = Integer.parseInt(ss, 16);
		//System.out.println("VERSION: " + result);
		return result;
	}
}
