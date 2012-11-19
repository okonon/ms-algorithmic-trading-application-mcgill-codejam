package util;

public class ManagerSchedule {

	private static int[][] schedule = {{1,1,1,1,4,1,1,1,1,5,5,5,7,5,5,5,5,7},
										{1,1,1,1,4,4,4,4,5,4,4,4,4,5,5,5,5,6},
										{2,2,2,2,3,2,2,2,2,6,6,6,6,7,6,6,6,6},
										{2,3,3,3,3,2,3,3,3,3,7,7,7,7,6,7,7,7}};
	
	public static int getManager(int time, int strat){
		return schedule[strat][time/1800];
	}
}
