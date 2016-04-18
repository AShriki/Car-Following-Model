package structures;

public class CarNode {

	private double posX;
	private double posY;
	private double velocity;
	private double acceleration;
	private boolean isLeader = false;
	private int rxnTime;
	private double vRxn;
	private int timer;
	private double goalVelocity;
	private double[] vp; // velocity profile
	private int[] tp; // number of time steps each action will last for
	private int vStep = 0; // index of vp and tp
	private CarNode next;
	private double safeGap;
	public double tau;
	
	public CarNode (int posX, int posY, int vel, int accel, int rTime, double safeGap ,CarNode inFront){
		this.posX = posX;
		this.posY = posY;
		velocity = vel;
		acceleration = accel;
		rxnTime = rTime;
		vRxn = 0;
		timer = 0;
		next = inFront;
		goalVelocity = 0;
		tau = 1;
		this.safeGap = safeGap;
	}
	
	public boolean isLeader(){
		return isLeader;
	}
	
	public void setPosX(double x) {
		 posX = x;
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
	
	public void setPosY(double d) {
		posY = d;
	}
	
	public double getPosX() {
		return posX;
	}
	
	public double getPosY() {
		return posY;
	}

	public double getVel() {
		return velocity;
	}
	
	public double getAccel() {
		return acceleration;
	}
	
	public void setVel(double vel) {
		velocity = vel;
	}
	
	public void setAccel(double accel) {
		acceleration = accel;
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
	
	public void update(double timeUnit){// time per iteration in the work loop
		if(isLeader){
			if(timer == 0){
				if(vp.length > vStep ){
					
					
					
					goalVelocity = vp[vStep];
					timer = tp[vStep++];
					
					this.setAccel((goalVelocity - this.getVel()) / (timer*timeUnit));
					this.setVel(this.getVel() + timeUnit*this.getAccel());
					
					timer--;
				}
				else{
					this.setAccel(0);
					this.setVel(this.getVel());
					this.setPosY(this.getPosY()+timeUnit*velocity);
				}// end acceleration setter
			}
			
			else{ // timer is in countdown, update the velocity and timer;
			
				this.setVel(this.getVel() + timeUnit*this.getAccel());
				
				timer--;
			}
		}
		else{// is follower
			if(timer == 0){ // this is the time when a person can react to the car in front
				//save the velocity of the car in front and set the timer unless there is no change in velocity
				// if there is a change in velocity set the timer
				
				goalVelocity = next.getVel();
				if(next.getPosY()- safeGap < (this.getPosY()) + safeGap){ // if the car in front is located within the safety gap of the car of interest
					if(goalVelocity > velocity){
						//accelerate at half or quarter speed until safety gap is restored
						this.setAccel((1/(2*tau))*(goalVelocity - this.getVel()));
					}
					else if(goalVelocity < velocity){
						// neg. acceleration should be multplied by a negative 2*  the calculated acceleration
						this.setAccel(-(2/(tau))*(goalVelocity - this.getVel()));
					}
				}
				else{
					// normal acceleration change
					this.setAccel((1/tau)*(goalVelocity - this.getVel()));
				}

				this.setVel(this.getVel() + timeUnit*this.getAccel());

				timer--;
			}
			else{// timer is in countdown
				//continue acceleration to try to reach car velocity
				this.setVel(this.getVel() + timeUnit*this.getAccel());
				timer--;
			}
		}
	}
}
