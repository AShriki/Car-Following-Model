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
	double timeUnit;
	private double tau;
	int index;
	int leaderActive;
	public CarNode (int pos, int vel, int accel, double rTime, double safeGap, double timeUnit ,CarNode inFront){
		
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
					
				this.setAccel((goalVelocity - this.getVel()) / (timer*timeUnit));
				this.setVel(this.getVel() + timeUnit*this.getAccel());
										
			}
			else{
				this.setAccel(0);
				this.setPos(this.getPos() + timeUnit*this.getVel()); // update position
				this.setVel(this.getVel());
			}// end acceleration setter
			
			
			this.setPos(this.getPos() + timeUnit*this.getVel()); // update position
								
			timer -= 1;
			
		}
		else{// is follower
			// this is the time when a person can react to the car in front
				//save the velocity of the car in front and set the timer unless there is no change in velocity
				// if there is a change in velocity set the timer
				if(timer <= 0){
					goalVelocity = next.getVel();
					this.timer = this.getRxnTime();
				}
				
				if(next.getPos()- safeGap < (this.getPos()) + safeGap){ // if the car in front is located within the safety gap of the car of interest
						this.setAccel((1/((tau())))*(goalVelocity - this.getVel()));
				}
				else if(next.getPos()- safeGap > (this.getPos()) + safeGap){ // the front car is located too far in front of the safety gap
						this.setAccel((1/(tau()))*(goalVelocity - this.getVel())); // accelerate at 1.5 the normal rate
				}
				else{
					// normal acceleration change
					this.setAccel((1/tau())*(goalVelocity - this.getVel()));
				}
							
				this.setVel(this.getVel() + timeUnit*this.getAccel());

				this.setPos(this.getPos()+timeUnit*this.getVel()); // update position
								
				timer -= timeUnit;
		}
	}
	private double tau(){
		
		
		
		return 0.0;
	}
	private int leaderActivity(){
		int sum = 0;
		for(int i = 0; i < tp.length; i++)
			sum+=tp[i];
		return sum;
	}
}
