package structures;

public class CarNode {

	private int positionX;
	private int positionY;
	private double velocity;
	private double acceleration;
	private boolean isLeader = false;
	private int rxnTime;
	private double vRxn;
	private int timer;
	private double savedVelocity;
	double[] vp; // velocity profile
	int[] tp; // number of time steps each action will last for
	CarNode next;
	
	public CarNode (int posX, int posY, int vel, int accel, int rTime, CarNode inFront){
		positionX = posX;
		positionY = posY;
		velocity = vel;
		acceleration = accel;
		rxnTime = rTime;
		vRxn = 0;
		timer = 0;
		next = inFront;
		savedVelocity = 0;
	}
	
	public boolean isLeader(){
		return isLeader;
	}
	
	public void setPosX(int x) {
		 positionX = x;
	}
	
	public void setLeader(double prof[], int tprof[]){
		if(prof != null && tprof != null){
			isLeader = true;
			tp = tprof;
			vp = prof;
		}
	}
	
	public void setFollower(){
		isLeader = false;
		tp = null;
		vp= null;
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
	
	public double setVrxn(){
		return vRxn;
	}

	public void print(){
		System.out.println("X: "+ this.getPosX() + " Y: " + this.getPosY() + " V: " + this.getVel() + " A: " + this.getAccel());
	}
	
	public void update(int timeStep){// reformat to minimize statements
		if(next != null){
			if(timer == 0){
				//perform update from ap
				// if there is no update from ap, do nothing (i.e. maintain velocity)
			}
			else{ // timer is in countdown
				// continue to change based on velocity profile
			}
		}
		else{
			if(timer == 0){
				//save the velocity of the car in front and set the timer unless there is no change in velocity
				// if there is a change in velocity set the timer
			}
			else{// timer is in countdown
				//continue acceleration to try to reach car velocity
				//try to maintain a safe distance from the car in front (this means some kind of compensation based on the distance to the car in front)
			}
		}
	}
	
	public void update(){
		update(1);
	}

}
