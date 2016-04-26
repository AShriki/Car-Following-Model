package structures;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
	
	private static final String p =  System.getProperty( "line.separator" );

	// variable for ease of changing

	private static boolean RRX = false;
	public static double alpha = 5; // set alpha
	public static boolean adaptiveModel = false; // use the adaptive model
	private static double tStep = 10/1000.0; // amount of time per step 
	public static double l = 0.2;
	public static double m = -0.2;
	private static double rxnTime = 0.25;
	
	
	private static double[] vp = {10,5,20}; // acceleration profile
	private static int[] tp = {10,10,10}; // number of time steps per velocity change

	public static void main(String[] args) {
		int t=3;// t is the number of cars
		CarNode[] cars = new CarNode[t];
		
		if(!RRX){
			tStep = 0.25; 
		}
		
		cars = populateCars(t, vp, tp, tStep);
		
		int timeSteps = calcTimeSteps(tp,cars); // worst case, max timesteps that might be necessary
		
		workLoopFO(timeSteps,cars);

	}
	private static void workLoopFO(int timeSteps, CarNode cars[]){
		
		String s = new String();
		
		File output = makeFile(cars.length);
		
		try{
			FileWriter fwriter = new FileWriter(output);
			initializeHeader(fwriter,cars.length);

			for(int i=0; i < timeSteps; i++){ // work loop
				s = Integer.toString(i);
				for(CarNode z : cars){
					double ds = z.getGap();
					s += (',' + Double.toString(z.getGap())+','+Double.toString(z.getVel()))+','+Double.toString(z.getAccel());
					z.update(i);// time step
				}
				
				s+= p;
				fwriter.write(s, 0, s.length());
			}
			fwriter.close();
		} catch (IOException x) {
			System.err.format("IOException: %s%n", x);
		}
		
	}
	private static void initializeHeader(FileWriter fw, int t){
		
		String s = new String();
		
		String header = "Gap,Speed,Acceleration";
		
		try {
			
			s = genHeader(t) + p;
			fw.write(s, 0, s.length());
			s = genHeader(t,header) + p;
			fw.write(s, 0, s.length());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private static File makeFile(int t){
	
	String param = "";
	if(RRX)
		param+="RRXN-";
	if(adaptiveModel)
		param+="ADAP-";
	
	File file = new File("Sim" + "-" + "alpha" + param + Double.toString(alpha) + "C" + t +".csv"); 
	
	int j = 1;
	try {	
		while (!file.createNewFile()){
			file = new File("Sim" + "-" + "alpha" + param +Double.toString(alpha) + "-" + Integer.toString(j++) + "C" + t + ".csv"); 
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	return file;		
}
	private static String genHeader(int t, String header){
	String cars = "Time step";
	for(int i = 0; i < t; i++){
		cars += (',' + header);
	}
	cars += '\n';
	return cars;
}
	private static String genHeader(int t){
	String cars = "";
	for(int i = 1; i <= t; i++){
		cars += (",Car" + i + ",,");
	}
	cars += '\n';
	return cars;
}
	
	private static CarNode[] populateCars(int numCars,double ap[],int tp[], double tStep){
		
		int carLength = 3; // car length estimated in meters
		int safetyGap = carLength; // safety gap is one car length at 0 speed
		
		if(numCars <= 0)
			System.out.println("Not Enough Cars!");
		else{
			
			CarNode[] cars = new CarNode[numCars];
			
			Random rnd = new Random();
			
			if(RRX){
				rxnTime = (rnd.nextInt(100)+1)*10;// rxn time from 10ms to 1000ms
			}

			cars[0] = new CarNode(0,0, 0, 0, safetyGap,tStep,carLength,null);
			
			for(int i = 1; i < numCars; i++){
				if(RRX){
					rxnTime = (rnd.nextInt(100)+1)*10;
				}
				cars[i] = new CarNode(-(i*carLength+i*safetyGap),0, 0, rxnTime,safetyGap,tStep,carLength,cars[i-1]);
			}
			cars[0].setLeader(ap, tp);
			
			return cars;
		}
		return null;
	}
	
	private static int calcTimeSteps(int []tp, CarNode[] cars){
		int sum = 0;
		for(int z : tp)
			sum+=z;
		
		sum+=cars.length*10;
		
		return sum;
	}
}
