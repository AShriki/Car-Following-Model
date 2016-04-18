package structures;

import java.util.Random;

public class Main {
	public static void main(String[] args) {
		int t=1;// t is the number of cars
		
		double tStep = 10/1000.0;
		
		double[] vp = {6,7,-6,2,9,100,60,23}; // acceleration profile
		int[] tp = {80,60,20,90,20,40,20}; // number of time steps each action will last for
		// what happens if a profile has a zero in it?
		
		CarNode[] cars = new CarNode[t];
		
		cars = populateCars(t, vp, tp, tStep);
		
		int timeSteps = calcTimeSteps(tp,cars); // worst case, max timesteps that might be necessary
		
		for(int i=0; i < timeSteps; i++){ // work loop
			for(CarNode z : cars){
				z.update(i);// time step
			}
		}
	}
	
	private static CarNode[] populateCars(int numCars,double ap[],int tp[], double tStep){
		
		int carLength = 3; // car length estimated in meters
		int safetyGap = carLength; // safety gap is one car length at 0 speed
		
		if(numCars <= 0)
			System.out.println("Not Enough Cars!");
		else{
			
			CarNode[] cars = new CarNode[numCars];
			
			Random rnd = new Random();
			int rxnTime = (rnd.nextInt(10)+1)*10;// rxn time from 10ms to 100ms
			
			cars[0] = new CarNode(0,0, 0, 0, rxnTime, safetyGap,tStep,null);
			
			for(int i = 1; i < numCars; i++){
				rxnTime = (rnd.nextInt(10)+1)*10;
				cars[i] = new CarNode(0,-(i*carLength+i*safetyGap), 0, 0, rxnTime,carLength,tStep,cars[i-1]);
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
		
		for(CarNode z : cars)
			sum+=z.getRxnTime();
		
		return sum;
	}
	
}
