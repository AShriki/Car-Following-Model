package structures;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
	
	private static final String p =  System.getProperty( "line.separator" );
	
	
	private static int RRX = 0;
	
	
	public static void main(String[] args) {
		int t=3;// t is the number of cars
		CarNode[] cars = new CarNode[t];
		double tStep = 10/1000.0;;
		if(RRX==0){
			tStep = 0.25; 
		}
		
		double[] vp = {1,6}; // acceleration profile
		int[] tp = {4,10}; // number of time steps each action will last for
		
		cars = populateCars(t, vp, tp, tStep);
		
		int timeSteps = calcTimeSteps(tp,cars); // worst case, max timesteps that might be necessary
		
		String s = new String();

		File output = makeFile();
		
		try{
			FileWriter fwriter = new FileWriter(output);
			initializeHeader(fwriter,t);

			for(int i=0; i < timeSteps; i++){ // work loop
				s = Integer.toString(i);
				for(CarNode z : cars){
					s += (',' + Double.toString(z.getPos())+','+Double.toString(z.getVel())+','+Double.toString(z.getAccel()));
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
		
		String header = "Position,Velocity,Acceleration";
		
		try {
			
			s = genHeader(t) + p;
			fw.write(s, 0, s.length());
			s = genHeader(t,header) + p;
			fw.write(s, 0, s.length());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private static File makeFile(){
	
	String fileName = "Sim";
	File file = new File((fileName+".csv"));
	
	int j = 1;
	
	while (file.exists())
		file = new File(fileName + '-' + Integer.toString(j++) + ".csv"); 
	
	try {
		file.createNewFile();
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
	String cars = ",Car 1,";
	for(int i = 2; i <= t; i++){
		cars += (",," + "Car" + i + ",,");
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
			
			double rxnTime = 0;
			CarNode[] cars = new CarNode[numCars];
			
			Random rnd = new Random();
			
			if(RRX==1){
				rxnTime = (rnd.nextInt(100)+1)*10;// rxn time from 10ms to 1000ms
			}
			else
				rxnTime = 0.25;
			cars[0] = new CarNode(0,0, 0, 0, safetyGap,tStep,null);
			
			for(int i = 1; i < numCars; i++){
				if(RRX == 1){
					rxnTime = (rnd.nextInt(100)+1)*10;
				}
				cars[i] = new CarNode(-(i*carLength+i*safetyGap),0, 0, rxnTime,safetyGap,tStep,cars[i-1]);
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
