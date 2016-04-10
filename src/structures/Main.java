package structures;

import java.util.Random;

public class Main {
	public static void main(String[] args) {
		int t=5;// t is the number of cars
		CarNode[] cars = new CarNode[t];
		double[] ap = {8,7,6,2,9,100,60,23};
		int[] tp = {7,6,2,9,100,60,23};
		
		cars = populateCars(t);
		
		int timeSteps = calcTimeSteps(tp,cars);
		
		
		for(int i=0; i<t; i++){
			update();
		}
	}
	
	public static void print(CarNode car){
		System.out.println("X: "+ car.getPosX() + " Y: " + car.getPosY() + " V: " + car.getVel() + " A: " + car.getAccel());
	}
	
	private static CarNode[] populateCars(int numCars){
		
		int carLength = 3; // car length estimated in meters
		int safetyGap = carLength; // safety gap is one car length at 0 speed
		
		if(numCars <= 0)
			System.out.println("Not Enough Cars!");
		else{
			
			CarNode[] cars = new CarNode[numCars];
			
			Random rnd = new Random();
			int rxnTime;
			
			for(int i = 0; i < numCars; i++){
				rxnTime = rnd.nextInt(50);
				cars[i] = new CarNode(0,i*carLength+i*safetyGap, 0, 0, rxnTime);
			}
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
