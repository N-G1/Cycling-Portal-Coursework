package cycling;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
/**
 * Class used to store the stages of a race and their relevant information 
 * @author Nathan
 *
 */
public class Stage {
	int raceID, stageID;
    String stageName, description;
    double length;
    LocalDateTime startTime;
    StageType type;
    String state = "incomplete";
    public boolean concluded = false;
    /**
     * List of all stages currently stored 
     */
    public static ArrayList<Stage> stageList = new ArrayList<Stage>();
    /**
     * List of all segments for this particular stage 
     */
    public ArrayList<Segment> stageSegmentList = new ArrayList<Segment>();
	public Hashtable<Integer, Integer> riderPointsDictionary = new Hashtable<Integer, Integer>();
	public Hashtable<Integer, Integer> riderMountainPointsDictionary = new Hashtable<Integer, Integer>();
    
    
    public Stage(int raceID, String stageName, String description, double length, LocalDateTime startTime, StageType type){
    	/* exception handling handled in race method that calls this constructor */
        int temp;
        this.raceID = raceID;
        this.stageName = stageName;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        temp = stageList.size();
        if (temp > 0){ 
            	this.stageID = stageList.get(temp - 1).stageID + 1;
        } 
        else {
            	this.stageID = 0;
        }
        stageList.add(this);
    }
    /**
     * Returns stage ID
     * @return stageID
     * @author Nathan
     */
    public int getStageID() {
		return this.stageID;
	}
    public static double getStageLength(int stageId) throws IDNotRecognisedException{ 
    	if (stageIdExists(stageId) == false) {
    		throw new IDNotRecognisedException("Stage id does not exists in the system");
    	}
    	else {
    		Stage temp;
            temp = getStageObj(stageId);
            return temp.getLength(); /* returns length of stage */
    	} 
    }
    /**
     * Returns true if stage exists, false if it does not
     * @param stageId ID of stage wished to have its existence verified
     * @return false / true
     * @author Nathan
     */
    public static boolean stageIdExists(int stageId) {
    	boolean exists = false;
    	for(int i=0; i <stageList.size(); i++) {
    		if (stageList.get(i).getStageID() == stageId) { /*check for if stage exists */
    			exists = true;
    			break;
    		}
    	}
    	if (exists == true) {
    		return true;
    	}
    	else {
    		return false;
    	}  
    }
    public static Stage getStageObj(int passedID){
        for (int i = 0; i < stageList.size(); i++) {
            if (stageList.get(i).getStageID() == passedID) {
                return stageList.get(i); /* returns stage object with relevant ID in the event it is found */
            }
        }
        return null;
    }
    
    public static void removeStageById(int stageId) throws IDNotRecognisedException{
    	if (stageIdExists(stageId) == false) {
    		throw new IDNotRecognisedException("Stage id does not exists in the system");
    	}
    	else {
    		 Stage tempStage;
    	        Race tempRace;
    	        tempStage = getStageObj(stageId);
    	        tempRace = Race.getRaceObj(tempStage.getRaceID());
    	        for (int i = 0; i < tempStage.stageSegmentList.size(); i++) {
    	            Segment.removeSegment(tempStage.stageSegmentList.get(i).getSegmentID()); 
    	        }
    	        tempRace.raceStageList.remove(tempRace.raceStageList.indexOf(tempStage));
    	        stageList.remove(stageList.indexOf(tempStage)); /* removes stage and cascades down to remove all segments from this stage */
    	}
       
    }

    public static int addCategorizedClimbToStage(int stageId, Double location, SegmentType type, Double averageGradient,
            Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException, InvalidStageTypeException{
    	/* comments for this and method below are almost exactly the same, see below */
    	if (stageIdExists(stageId) == false) {
    		throw new IDNotRecognisedException("Stage id does not exists in the system");
    	}
    	else if (getStageObj(stageId).getConcluded() == true) {
    		throw new InvalidStageStateException("Stage has been concluded and cannot be changed");
    	}
    	else if (location < 0 || location >= getStageObj(stageId).getLength()) {
    		throw new InvalidLocationException("Location is out of range");
    	}
    	else if (getStageObj(stageId).getType() == StageType.TT) {
    		throw new InvalidStageTypeException("Cannot add a sprint or mountain to time trial");
    	}
        Segment segmentObj = new Segment( stageId, location, type, averageGradient, length);
        getStageObj(stageId).stageSegmentList.add(segmentObj);
        return segmentObj.getSegmentID();
    }

    public static int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException, InvalidLocationException, 
    InvalidStageStateException, InvalidStageTypeException{
    	if (stageIdExists(stageId) == false) {
    		throw new IDNotRecognisedException("Stage id does not exists in the system"); /* check for if ID is invalid */
    	}
    	else if (getStageObj(stageId).getConcluded() == true) {
    		throw new InvalidStageStateException("Stage has been concluded and cannot be changed"); /* check for if stage is concluded */
    	}
    	else if (location < 0 || location >= getStageObj(stageId).getLength()) {
    		throw new InvalidLocationException("Location is out of range"); /* check to ensure location is within bounds */
    	}
    	else if (getStageObj(stageId).getType() == StageType.TT) {
    		throw new InvalidStageTypeException("Cannot add a sprint or mountain to time trial"); /* check to ensure stage is not a time trial */
    	}
        Segment segmentObj = new Segment(stageId, location);
        getStageObj(stageId).stageSegmentList.add(segmentObj); /* adds new segment object to list of segments for this stage */
        return segmentObj.getSegmentID(); /* returns new segments ID */
        
    }
    public static void concludeStagePreperation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
    	if (stageIdExists(stageId) == false) {
    		throw new IDNotRecognisedException("Stage id does not exists in the system"); /* check for if ID is invalid */
    	} 
    	if (getStageObj(stageId).getConcluded() == true) {
    		throw new InvalidStageStateException("Stage is already concluded"); /* check for if state is already concluded */
    	}
    	getStageObj(stageId).setConcluded(true); /* sets stages concluded boolean */
    	getStageObj(stageId).setState("waiting for results"); /* sets the stage state */
    	
    }
    
    public static int[] getStageSegments(int stageId)  throws IDNotRecognisedException{
    	if (stageIdExists(stageId) == false) {
    		throw new IDNotRecognisedException("Stage id does not exists in the system"); /* ID not recognised check */
    	}
    	Stage temp = getStageObj(stageId); /* gets object with relevant ID */
        int[] stageSegments = new int[temp.stageSegmentList.size()]; /* declares amount of segments */


		for(int i = 0; i < temp.stageSegmentList.size(); i++) {
			stageSegments[i] = temp.stageSegmentList.get(i).getSegmentID(); /* puts segment IDS in array */
		}
		return stageSegments; /* returns segment IDs */
    }
    public static void calculatePoints(int stageId) throws IDNotRecognisedException {
		ArrayList<Integer> stagePoints = new ArrayList<Integer>();
		Hashtable<Integer, Integer> tempDictionary = new Hashtable<Integer, Integer>();
		Stage temp;
		temp = getStageObj(stageId);
		StageType tempType = temp.getType();
		int riderIdArr[];
		ArrayList<Integer> ridersIds = new ArrayList<Integer>();
		riderIdArr = Result.getRidersRankInStage(stageId);
		for (int i = 0; i < riderIdArr.length; i++) {
			ridersIds.add(riderIdArr[i]);
		}
		if (tempType == StageType.FLAT) {
			stagePoints = distributePoints(ridersIds, Arrays.asList(50,30,20,18,16,14,12,10,8,7,6,5,4,3,2));
		} else if (tempType == StageType.MEDIUM_MOUNTAIN) {
			stagePoints = distributePoints(ridersIds, Arrays.asList(30,25,22,19,17,15,13,11,9,7,6,5,4,3,2));
		} else if (tempType == StageType.HIGH_MOUNTAIN) {
			stagePoints = distributePoints(ridersIds, Arrays.asList(20,17,15,13,11,10,9,8,7,6,5,4,3,2,1));
		} else if (tempType == StageType.TT) {
			stagePoints = distributePoints(ridersIds, Arrays.asList(20,17,15,13,11,10,9,8,7,6,5,4,3,2,1));
		}
		for (int i = 0; i <= temp.stageSegmentList.size() - 1; i++) {
			boolean tempBool = temp.stageSegmentList.get(i).getIsIntermediateSprint();
			if (tempBool == true) {
				ArrayList<Integer> checkpointRiderIds = getRidersRanksForCheckpoint(stageId, i);
				ArrayList<Integer> tempPoints = distributePoints(ridersIds, Arrays.asList(20,17,15,13,11,10,9,8,7,6,5,4,3,2,1));
				for (int k = 0; k <= tempPoints.size() - 1; k++) {
					for (int j = 0; j <= checkpointRiderIds.size() - 1; j++) {
						if (ridersIds.get(k) == checkpointRiderIds.get(j)) {
							int currentPoints = stagePoints.get(k);
							stagePoints.set(k, currentPoints+tempPoints.get(j));
						}
					}
				}
				
			}
		}
		for (int i = 0; i <= stagePoints.size() - 1; i++) {
			tempDictionary.put(ridersIds.get(i), stagePoints.get(i));
		}
		temp.setRiderPointsDictionary(tempDictionary);
	}
    
    public static void calculateMountainPoints(int stageId) throws IDNotRecognisedException {
		Stage temp;
		temp = Stage.getStageObj(stageId);
		boolean tempBool, firstIteration;
		ArrayList<Integer> points = new ArrayList<Integer>();
		ArrayList<Integer> checkpointRiderIds = new ArrayList<Integer>();
		Hashtable<Integer, Integer> mountainPointsDictionary = new Hashtable<Integer, Integer>();
		firstIteration = true;
		for (int i = 0; i <= temp.stageSegmentList.size() - 1; i++) {
			tempBool = temp.stageSegmentList.get(i).getIsIntermediateSprint();
			if (tempBool == false) {
				checkpointRiderIds = getRidersRanksForCheckpoint(stageId, i);
				if (firstIteration == true) {
					for (int j = 0; j <= checkpointRiderIds.size() - 1; j++) {
						mountainPointsDictionary.put(checkpointRiderIds.get(j),0);
					}
					firstIteration = false;
				}
				SegmentType tempType = temp.stageSegmentList.get(i).getType();
				if (tempType == SegmentType.C1) {
					points = distributePoints(checkpointRiderIds, Arrays.asList(10,8,6,4,2,1));
				} else if (tempType == SegmentType.C2) {
					points = distributePoints(checkpointRiderIds, Arrays.asList(5,3,2,1));
				} else if (tempType == SegmentType.C3) {
					points = distributePoints(checkpointRiderIds, Arrays.asList(2,1));
				} else if (tempType == SegmentType.C4) {
					points = distributePoints(checkpointRiderIds, Arrays.asList(1));
				} else if (tempType == SegmentType.HC) {
					points = distributePoints(checkpointRiderIds, Arrays.asList(20,15,12,10,8,6,4,2));
				}
				for (int j = 0; j <= checkpointRiderIds.size() - 1; j++) {
					mountainPointsDictionary.put(checkpointRiderIds.get(j), mountainPointsDictionary.get(checkpointRiderIds.get(j)) + points.get(j));
				}
			}
		}
		temp.setRiderMountainPointsDictionary(mountainPointsDictionary);
	}

    public static ArrayList<Integer> getRidersRanksForCheckpoint(int stageId, int location) throws IDNotRecognisedException {
		int[] tempRiders = Result.getRidersRankInStage(stageId);
		Hashtable<LocalTime, Integer> checkpointsDictionary = new Hashtable<LocalTime, Integer>();
		LocalTime tempCheckpoint;
		ArrayList<LocalTime> checkpoints = new ArrayList<LocalTime>();
		for (int i = 0; i <= tempRiders.length - 1; i++) {
			tempCheckpoint = Result.getRiderResultsInStage(stageId, tempRiders[i])[location];
			checkpoints.add(tempCheckpoint);
			checkpointsDictionary.put(tempCheckpoint, tempRiders[i]);
		}
		checkpoints.sort(new Result.CheckpointComparator());
		ArrayList<Integer> riderIds = new ArrayList<Integer>();
		for (int x = 0; x <= checkpoints.size() - 1; x++) {
			riderIds.add(checkpointsDictionary.get(checkpoints.get(x)));
		}
		return riderIds;
	}
    
    public static ArrayList<Integer> distributePoints(ArrayList<Integer> riderIds, List<Integer> pointsSet) {
		ArrayList<Integer> points = new ArrayList<Integer>();
		int tempPoints = 0;
		for (int i = 0; i < riderIds.size(); i++) {
			tempPoints = 0;
			if (i < pointsSet.size()) {
				tempPoints = pointsSet.get(i);
			}
			points.add(tempPoints);
		}
		return points;
	}
    /**
     * Returns Race ID
     * @return raceID
     * @author Nathan
     */
    public int getRaceID() {
    	return this.raceID;
    }
    /**
     * Returns stage concluded state
     * @return concluded
     * @author Nathan
     */
    public boolean getConcluded() {
    	return this.concluded;
    }
    /**
     * Gets stage segment list
     * @return ArrayList stageSegmentList
     * @author Nathan
     */
    public ArrayList<Segment> getStageSegmentList() {
    	return this.stageSegmentList;
    }
    /**
     * Sets concluded boolean
     * @param value true / false
     * @author Nathan
     */
    public void setConcluded(boolean value) {
    	this.concluded = value;
    }
    /**
     * Sets stage state
     * @param value
     * @author Nathan
     */
    public void setState(String value) {
    	this.state = value;	
    }
    /**
     * Returns stage state
     * @return state
     * @author Nathan
     */
    public String getState() {
    	return this.state;
    }
    /**
     * Returns stage name
     * @return stage name
     * @author Nathan
     */
    public String getStageName() {
    	return this.stageName;
    }
    /**
     * Returns description 
     * @return description
     * @author Nathan
     */
    public String getDescription() {
    	return this.description;
    }
    /**
     * Returns length type
     * @return length
     * @author Nathan
     */
    public double getLength() {
    	return this.length;
    }
    /**
     * Returns start time 
     * @return start time
     * @author Nathan
     */
    public LocalDateTime getStartTime() {
    	return this.startTime;
    }
    /**
     * Returns stage type
     * @return type
     * @author Nathan
     */
    public StageType getType() {
    	return this.type;
    }
    /**
     * Hashtable that stores the riders points
     * @return riderPointsDictionary
     * @author Sam
     */
	public Hashtable<Integer, Integer> getRiderPointsDictionary() {
    	return this.riderPointsDictionary;
    }
	/**
     * Hashtable that stores the riders mountain points
     * @return riderPointsDictionary
     * @author Sam
     */
	public Hashtable<Integer, Integer> getRiderMountainPointsDictionary() {
    	return this.riderMountainPointsDictionary;
    }
	/**
     * Rider mountain points Hashtable setter
     * @author Sam
     */
	public void setRiderMountainPointsDictionary(Hashtable<Integer, Integer> newTable) {
    	this.riderMountainPointsDictionary = newTable;
    }
	/**
     * Rider points Hashtable setter
     * @author Sam
     */
	public void setRiderPointsDictionary(Hashtable<Integer, Integer> newtable) {
		this.riderPointsDictionary = newtable;
	}
}    
