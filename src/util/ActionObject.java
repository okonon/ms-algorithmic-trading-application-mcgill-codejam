package util;

class ActionObject{
	private String type;
	private int time;
	private Strategy strategy;

	public ActionObject(String actionType, Strategy strategyType, int timeOfAction){
		this.type = actionType;
		this.strategy = strategyType;
		this.time = timeOfAction;
	}
	public String getType(){
		return type;
	}
	public Strategy getStrategy(){
		return strategy;
	}
	public int getTime(){
		return time;
	}
}


