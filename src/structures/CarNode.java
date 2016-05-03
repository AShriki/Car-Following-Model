package structures;

public class CarNode {

	private double pos;
	private double velocity;
	private double acceleration;
	private double prevVelocity;
	
	private boolean isLeader = false;
	private double rxnTime;
	private double timer;
	private double goalVelocity;
	private double[] vp; // velocity profile
	private int[] tp; // number of time steps each action will last for
	private CarNode next;
	private double safeGap;
	private double timeUnit;
	private int index;
	private double carLength;
	private int leaderActive;
		
	public CarNode (int pos, int vel, int accel, double rTime, double safeGap, double timeUnit,double carLength ,CarNode inFront){
		
		this.pos = pos;
		velocity = vel;
		acceleration = accel;
		rxnTime = rTime;
		timer = rTime;
		next = inFront;
		goalVelocity = 0;
		this.safeGap = safeGap;
		this.timeUnit = timeUnit;
		index = 0;
		this.carLength = carLength;
		prevVelocity = 0;

	}
	private double getCarLength(){
		return carLength;
	}
	public double getTimeUnit(){
		return timeUnit;
	}
	private void setSafeGap(double s){
		safeGap = carLength*s/10;
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
	public void setVel(double vel) {
		velocity = vel;
	}
	public double getPrevVel() {
		return prevVelocity;
	}
	public void setPrevVel() {
		prevVelocity = this.getVel();
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
	public void setAccel(double accel) {
		acceleration = accel;
	}
	public double getRxnTime(){
		return rxnTime;
	}
	
	public void update(int curStep){
				
		if(isLeader){
			if(curStep < leaderActive){
				if(timer <= 0){
					this.setGoalVel(vp[index]);
					timer = tp[index];
					index++;
				}
					
				this.setAccel((this.getGoalVel() - this.getVel()) / (timer*this.getTimeUnit()));
				this.setVel(this.getVel() + this.getTimeUnit()*this.getAccel());
										
			}
			else{
				this.setAccel(0);
				this.setVel(this.getVel());
			}// end acceleration setter
			
			
			this.setPos(this.getPos() + this.getTimeUnit()*this.getVel()+0.5*this.getAccel()*Math.pow(this.getTimeUnit(),2));	

								
			timer -= 1; // the leader counts time steps
			
		}
		else{// is follower
				
				if(timer <= 0){
					this.setAccel(tau()*(this.getGoalVel() - this.getVel())); // update acceleration
					this.setGoalVel(next.getVel());
					timer = this.getRxnTime();
				}
								
				this.setPrevVel();
				
				this.setVel(this.getVel() + this.getTimeUnit()*this.getAccel()); // update velocity
								
				this.setPos(this.getPos() + this.getTimeUnit()*this.getVel()+0.5*this.getAccel()*Math.pow(this.getTimeUnit(),2));								
																
				this.setSafeGap(this.getVel());
				
				timer -= this.getTimeUnit(); // the follower counts reaction time
				
		}
	}
	
	private double tau(){ // sensitivity constant
		double sensitivityConst = 1; // output value
		
		// l and m take on 4 to -1 amd -2 to 2 respectively

		double gap = this.getGap();
		double l,m;
		if(this.getGoalVel()-this.getVel()>0){
			l = 1.6;
			m = 0.2;
		}
		else{//1,0.9
			l=2.5;
			m=0.7;
		}
		
		sensitivityConst = (Main.alpha*Math.pow(this.getGoalVel(),m))/(Math.pow(gap,l));
		
		return sensitivityConst;
	}
	
	private int leaderActivity(){
		int sum = 0;
		for(int i = 0; i < tp.length; i++)
			sum+=tp[i];
		return sum;
	}
	public double getGap() {
		if(next!= null)
			return (next.getPos()-next.getCarLength()-this.getPos());
		return 0;
	}
}
