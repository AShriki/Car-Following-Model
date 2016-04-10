package structures;

public class CarNode {

	private int positionX;
	private int positionY;
	private double velocity;
	private double acceleration;
	private boolean isLeader = false;
	private int rxnTime;
	private double vUse;
	private int timer;
	
	public CarNode (int posX, int posY, int vel, int accel, int rTime ){
		positionX = posX;
		positionY = posY;
		velocity = vel;
		acceleration = accel;
		rxnTime = rTime;
		vUse = 0;
		timer = 0;
	}
	
	public boolean isLeader(){
		return isLeader;
	}
	
	public void makeLeader(){
		isLeader = true; 
	}
	
	public void makeFollower() {
		isLeader = false;	
	}
	public void setPosX(int x) {
		 positionX = x;
	}
	
	public void setPosY(int y) {
		positionY = y;
	}
	public int getPosX() {
		return positionX;
	}
	
	public int getPosY() {
		return positionY;
	}
	
	public double getVel() {
		return velocity;
	}
	
	public double getAccel() {
		return acceleration;
	}
	public double setVel() {
		return velocity;
	}
	public double setAccel() {
		return acceleration;
	}
	public int getRxnTime(){
		return rxnTime;
	}
	public double getvUse(){
		return vUse;
	}
	public boolean incrementTimer(int tStep){
		
		boolean alarm = false;
		
		timer -= tStep;
		
		if(timer <= 0){
			
			timer = rxnTime;
			return true;
		}
		
		return alarm;
	}

}
