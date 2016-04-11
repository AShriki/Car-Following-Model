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
	double[] ap; // acceleration profile
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
			ap = prof;
		}
	}
	
	public void setFollower(){
		isLeader = false;
		tp = null;
		ap= null;
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

	private boolean incrementTimer(int timeStep){
				
		timer -= timeStep;
		
		if(isLeader){ 
		} 
		else if(timer <= 0){
			
			timer = rxnTime;
			return true;
		}
		
		return false;
	}
	public void print(){
		System.out.println("X: "+ this.getPosX() + " Y: " + this.getPosY() + " V: " + this.getVel() + " A: " + this.getAccel());
	}
	public void update(int timeStep){
		if(next != null){
			
		}
		else{
			
		}
	}
	public void update(){
		update(1);
	}

}
