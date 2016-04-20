package structures;

public class CarNode {

	private double pos;
	private double velocity;
	private double acceleration;
	private boolean isLeader = false;
	private double rxnTime;
	private double timer;
	private double goalVelocity;
	private double[] vp; // velocity profile
	private int[] tp; // number of time steps each action will last for
	private CarNode next;
	private double safeGap;
	private double timeUnit;
	private double tau;
	private int index;
	private double carLength;
	private int leaderActive;
	
	boolean adaptiveModel = true;
	
	public CarNode (int pos, int vel, int accel, double rTime, double safeGap, double timeUnit,double carLength ,CarNode inFront){
		
		this.pos = pos;
		velocity = vel;
		acceleration = accel;
		rxnTime = rTime;
		timer = rTime;
		next = inFront;
		goalVelocity = 0;
		tau = 2;
		this.safeGap = safeGap;
		this.timeUnit = timeUnit;
		index = 0;
		this.carLength = carLength;
			
	}
	private double getCarLength(){
		return carLength;
	}
	public double getTimeUnit(){
		return timeUnit;
	}
	private void setSafeGap(double s){
		safeGap = carLength*s;
	}
	
	public double getSafeGap() {
		return safeGap;
	}
	
	public boolean isLeader(){
		return isLeader;
	}
		
	public void setLeader(double prof[], int tprof[]){
		if(prof != null && tprof != null){
			isLeader = true;
			tp = tprof;
			vp = prof;
		}
		leaderActive = leaderActivity();
	}
	
	public void setFollower(){
		isLeader = false;
		tp = null;
		vp= null;
	}
	
	public void setPos(double d) {
		pos = d;
	}
		
	public double getPos() {
		return pos;
	}

	public double getVel() {
		return velocity;
	}
	
	public double getGoalVel() {
		return goalVelocity;
	}
	public void setGoalVel(double v) {
		goalVelocity = v;
	}
	
	public double getAccel() {
		return acceleration;
	}
	
	public void setVel(double vel) {
		velocity = vel;
	}
	
	public double getTau(){
		return tau;
	}
	
	public void setTau(double tau){
		this.tau = tau;
	}

	public void setAccel(double accel) {
		acceleration = accel;
	}
	
	public double getRxnTime(){
		return rxnTime;
	}

	public void update(int curStep){// time per iteration in the work loop
				
		if(isLeader){
			if(curStep < leaderActive){
				if(timer <= 0){
					goalVelocity = vp[index];
					timer = tp[index];
					index++;
				}
					
				this.setAccel((goalVelocity - this.getVel()) / (timer*this.getTimeUnit()));
				this.setVel(this.getVel() + this.getTimeUnit()*this.getAccel());
										
			}
			else{
				this.setAccel(0);
				this.setPos(this.getPos() + this.getTimeUnit()*this.getVel()); 
				this.setVel(this.getVel());
			}// end acceleration setter
			
			
			this.setPos(this.getPos() + this.getTimeUnit()*this.getVel()); // update position
								
			timer -= 1; // the leader counts time steps
			
		}
		else{// is follower
			// this is the time when a person can react to the car in front
				//save the velocity of the car in front and set the timer unless there is no change in velocity
				// if there is a change in velocity set the timer
				if(timer <= 0){
					this.setGoalVel(next.getVel());
					this.timer = this.getRxnTime();
				}
				
				this.setAccel(tau()*(this.getGoalVel() - this.getVel())); // update acceleration
							
				this.setVel(this.getVel() + this.getTimeUnit()*this.getAccel()); // update velocity
				
				this.setSafeGap(this.getVel());

				this.setPos(this.getPos() + this.getTimeUnit()*this.getVel());
								
				timer -= this.getTimeUnit(); // the follower counts reaction time
		}
	}
	private double tau(){ // sensitivity constant
		double sensitivityConst = 1; // output value
		
		double l = 0,m= 0,alpha = 13;
		// l and m take on 4 to -1 amd -2 to 2 respectively
		double gap = next.getPos()- this.getCarLength() - this.getPos(); // on the assumption that the origin is located at the front bumper of the car so we move it to the back with the translation of carLength
		double velocityDiff = next.getVel() - this.getVel();
		
		/*	Determination of l and m
		 * 	gap		vDiff	desired accel	alpha
		 * 
		 * small	small	medium
		 * small	large	large
		 * ~0		small	small
		 * ~0		large	large
		 * large	small	medium
		 * large	large	large
		 * */
		if(adaptiveModel){
			l = 0;
			m= 0;
		}
		else{
			
		}
		sensitivityConst = (alpha*Math.pow(this.getVel(),m))/(Math.pow(gap,l));
		
		return sensitivityConst;
	}
	private int leaderActivity(){
		int sum = 0;
		for(int i = 0; i < tp.length; i++)
			sum+=tp[i];
		return sum;
	}
}
