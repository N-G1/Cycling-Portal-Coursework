package cycling;

import java.time.LocalTime;
import java.util.*;

/**
 * Result class used to store the results of riders as objects, most functions to return a value related in some way to checkpoint are handled here or in race
 * @author Nathan
 *
 */
public class Result {
	int stageID, riderID;
	public static ArrayList<Result> allResultList = new ArrayList<Result>();
	LocalTime checkpoints[];
	public Result(int stageId, int riderId, LocalTime... checkpoints) throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
	InvalidStageStateException{ /* registerRiderResultsInStage */
		if (Stage.stageIdExists(stageId) == false) {
    		throw new IDNotRecognisedException("Stage id does not exists in the system"); /* ID not recognised check for stages */
    	}
		else if (Rider.riderIdExists(riderId) == false) {
			throw new IDNotRecognisedException("Rider id does not exists in the system"); /* ID not recognised check for riders */
		}
		else if (resultExists(stageId, riderId) == true) {
			throw new DuplicatedResultException("Result for this rider and stage already registered"); /* Duplicate result check */
		}
		else if (Stage.getStageObj(stageId).getConcluded() == false) {
			throw new InvalidStageStateException("Stage has not been concluded and results cannot be stored for it until done so"); /* stage state check */
		}
		this.stageID = stageId;
		this.riderID = riderId;
		this.checkpoints = checkpoints;
		allResultList.add(this);
	}
	
	public static LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		LocalTime[] result;
		try {
			result = getResultObj(stageId, riderId, allResultList).getRiderResults();
		} 
		catch (NullPointerException exception) { /* exception handling for if the array is empty */
			result = new LocalTime[]{};
		}

		return result;
	}
	
	public static int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		Stage.calculatePoints(stageId);
		ArrayList<Integer> ridersPoints = new ArrayList<Integer>();
		int riderPointArr[];
		Stage temp;
		temp = Stage.getStageObj(stageId);
		Hashtable<Integer, Integer> tempTable = temp.getRiderPointsDictionary(); /* gets Hashtable that stores the stages rider points */
		
		Set<Integer> intKeys = tempTable.keySet();
		Set<String> keys = new HashSet<String>(tempTable.size());
		for (Integer integer : intKeys) {
			keys.add(integer.toString()); /* converts integer keys to strings allowing for iteration */
		}
			
		for(String key: keys){
			ridersPoints.add(tempTable.get(Integer.parseInt(key))); /* iterates through and adds keys to riderpoints ArrayList */
		}
		riderPointArr = new int[ridersPoints.size()];
		for(int i = 0; i < ridersPoints.size(); i ++) {
			riderPointArr[i] = ridersPoints.get(i); /* stores values within ArrayList in correct return format */
		}
		return riderPointArr;
	}
	
	public static int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		Stage temp;
		temp = Stage.getStageObj(stageId);
		Stage.calculateMountainPoints(stageId);
		int riderPointArr[];
		ArrayList<Integer> ridersMountainPoints = new ArrayList<Integer>();
		Hashtable<Integer, Integer> tempTable = temp.getRiderMountainPointsDictionary();
		Set<Integer> intKeys = tempTable.keySet();
		Set<String> keys = new HashSet<String>(tempTable.size());
		for (Integer integer : intKeys) {
			keys.add(integer.toString());
		}
			
		for(String key: keys){
			ridersMountainPoints.add(tempTable.get(Integer.parseInt(key)));
		}
		riderPointArr = new int[ridersMountainPoints.size()];
		for(int i = 0; i < ridersMountainPoints.size(); i ++) {
			riderPointArr[i] = ridersMountainPoints.get(i);
		}
		return riderPointArr; /*will be returned int the order of the ranks of the riders for the first mountain checkpoint */
	}
	
	public static LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		ArrayList<Result> allRiderResults= getStageResultList(stageId);
		LocalTime[] riderTimes = getRiderResultsInStage(stageId, riderId);
		LocalTime nullExcep = null;
		
		if(riderTimes.length == 0) {
			return nullExcep;		/* returns nothing in the event the riderID does exist however is not value for this stage */
		}
		
		LocalTime riderFinish = riderTimes[riderTimes.length - 1], finTimeUppBound = riderFinish.plusSeconds(1);
		allRiderResults.sort(new ResultComparator()); /* sorts all objects within ArrayList by finishing time */
		
		for (int i = 0; i < allRiderResults.size(); i++) { /* only one loop required as list is sorted due to comparator */	
			if (allRiderResults.get(i).getRiderResults()[riderTimes.length - 1].isAfter(riderFinish) 
					&& allRiderResults.get(i).getRiderResults()[riderTimes.length - 1].isBefore(finTimeUppBound)){ /* if next rider time is after passed in riders time but still within a second of it */
				
				riderFinish = allRiderResults.get(i).getRiderResults()[riderTimes.length - 1];
				finTimeUppBound = riderFinish.plusSeconds(1);
			}
		}
		return riderFinish;
	}

	public static void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
			int index = allResultList.indexOf(getResultObj(stageId, riderId, allResultList));
			if (index == -1) {
				throw new IDNotRecognisedException("Rider cannot be deleted, already does not exist for this stage");
			}
			else {
				allResultList.remove(index); /* removes the rider at the specific index */
			}		
	}
	
	public static int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		ArrayList<Result> listOfRiderResults = getStageResultList(stageId); /*gets list of all stage results */
		int[] riderIDs = new int[listOfRiderResults.size()];
	
		listOfRiderResults.sort(new ResultComparator()); /* uses custom comparator to sort list by finishing result */
	
		for(int i = 0; i < listOfRiderResults.size(); i++) {
			riderIDs[i] = listOfRiderResults.get(i).getRiderID(); /* puts sorted results into correct output format */
		}
		return riderIDs;
	}
	public static LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		ArrayList<Result> relevantStageResults = getAdjustedResultObject(stageId);
		LocalTime[] adjustedResults = new LocalTime[relevantStageResults.size()];
		int length = relevantStageResults.get(0).getRiderResults().length - 1;
		
		for(int i = 0; i < relevantStageResults.size(); i++) {
			adjustedResults[i] = relevantStageResults.get(i).getRiderResults()[length]; /* returns all adjusted final results for the riders in the stage */
		}
		return adjustedResults;
	}
	/** 
	 * Used to find a specified stages finishing times and sort them in ascending order
	 * @param stageId ID of stage to be searched
	 * @return ArrayList of sorted finishing times 
	 * @throws IDNotRecognisedException
	 * @author Nathan
	 */
	public static ArrayList<Result> getAdjustedResultObject (int stageId) throws IDNotRecognisedException{
		ArrayList<Result> relevantStageResults = getStageResultList(stageId);
		int length = relevantStageResults.get(0).getRiderResults().length - 1;
		
		for (int t = 0; t < relevantStageResults.size(); t++) {		/* takes a value and compares it to all other values */
			for (int i = 0; i < relevantStageResults.size(); i++){	
				LocalTime time1 = relevantStageResults.get(t).getRiderResults()[length];
				LocalTime time2 = relevantStageResults.get(i).getRiderResults()[length];
				if(time2.isAfter(time1) && time2.isBefore(time1.plusSeconds(1))) { /* check to ensure that the time is within 1 second after */
					relevantStageResults.get(t).getRiderResults()[length] = relevantStageResults.get(i).getRiderResults()[length]; /* in event above statement is */
				}																												   /*  true adjusts the time */
			}
		}
		relevantStageResults.sort(new ResultComparator()); /* sorts results using result comparator */
		return relevantStageResults;
	}
	/** Rider Checkpoints getter
	 * @return LocalTime[] */	
	public LocalTime[] getRiderResults() { /* rider results getter */
		return this.checkpoints;
	}
	/** Stage ID getter */
	public int getStageID() {	/* stage ID getter */
		return this.stageID;
	}
	/** Rider ID getter */
	public int getRiderID() {	/* rider ID getter */
		return this.riderID;	
	}
	/**
	 * Method to assist with Duplicate Result exception 
	 * @param stageId Relevant stage ID
	 * @param riderID Relevant rider ID
	 * @return True if result already exists / false if not
	 * @author Nathan
	 */
	public boolean resultExists(int stageId, int riderID) {
		boolean exists = false;
		for(int i = 0; i <allResultList.size(); i++) {
			if (allResultList.get(i).getStageID() == stageId) {
				if (allResultList.get(i).getRiderID() == riderID) {
					exists = true;
				}
			}
		}
		return exists;
	}
	/**
	 * Returns the requested object based on their stage and rider ID
	 * @param stageId Id of the stage
	 * @param riderId Id of the rider
	 * @param specifiedList ArrayList to be searched to find the required object
	 * @return The requested result object within the specified list 
	 * @throws IDNotRecognisedException
	 * @author Nathan
	 */
	public static Result getResultObj(int stageId, int riderId, ArrayList<Result> specifiedList) throws IDNotRecognisedException{
		Result obj = null;
		boolean validForStage = true, stageExists = false, riderExists = false;
		for(int i = 0; i <specifiedList.size(); i++) {
			if (specifiedList.get(i).getStageID() == stageId) {
				stageExists = true;
				if (specifiedList.get(i).getRiderID() == riderId) {
					obj = specifiedList.get(i); /* returns the correct result in the event both the stage and rider id contain a related result */
				}
			}
		}
		if (obj == null) {
			if (stageExists == true){
				for(int y = 0; y <specifiedList.size(); y++) {
					if (specifiedList.get(y).getRiderID() == riderId) { /*check to see if the rider id exists just not for this particular stage */
						validForStage = false;
						riderExists = true;
						break;
					}
				}
				if (validForStage == false && riderExists == true) { /* in the event both exist however the rider does not exist for this stage */
					return null;
				}
				else {
					throw new IDNotRecognisedException("ID not recognised"); /* in the event stage exists but rider does not */
				}
			}
			else {
				throw new IDNotRecognisedException("ID not recognised"); /* in the event stage doesnt exist */
			}
		}
		return obj;
	}
	/** Loops through the result ArrayList and puts all the results of the related stage into its own ArrayList.
	 * 
	 * @param stageId The ID of the stage you require the results of
	 * @return ArrayList of the required results
	 * @author Nathan
	 */
	public static ArrayList<Result> getStageResultList(int stageId) throws IDNotRecognisedException {
		ArrayList<Result> relevantResults = new ArrayList<Result>();
		for (int i = 0; i < allResultList.size(); i++) {
			if (allResultList.get(i).getStageID() == stageId) {
				relevantResults.add(allResultList.get(i)); /* stores list of related stage results */
			}
		}
		if (relevantResults.isEmpty()) {
			throw new IDNotRecognisedException("ID not recognised exception"); 
		}
		return relevantResults;
	}
	/**
	 * Comparator used to compare the final index of the checkpoint array 
	 * @return Values sorted based on their finishing times
	 * @author Nathan
	 *
	 */
	static class ResultComparator implements Comparator<Result> {
	     public int compare(Result result1, Result result2) {
	         return result1.getRiderResults()[result1.getRiderResults().length - 1]
	        		 .compareTo(result2.getRiderResults()[result1.getRiderResults().length - 1]);
	     }
	 }
	/** Custom comparator used to compare checkpoint variables 
	 * @return list of checkpoint LocalTimes sorted based on their times 
	 * @author Sam
	 *
	 */
	static class CheckpointComparator implements Comparator <LocalTime> {
		public int compare(LocalTime checkpoint1, LocalTime checkpoint2) {
			return checkpoint1.compareTo(checkpoint2); /* compares two passed in localtime checkpoints */
		}
	}
}
