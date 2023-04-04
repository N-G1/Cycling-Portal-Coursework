package cycling;

import java.util.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Race class used to store the details, description, raceID and stage list within race objects that are stored in a larger ArrayList of races 
 * @author Nathan
 *
 */
public class Race {
	String name, description;
    int raceID;
    public static ArrayList<Race> raceList = new ArrayList<Race>();
    public ArrayList<Stage> raceStageList = new ArrayList<Stage>();
    
    public Race(String name, String description) throws IllegalNameException, InvalidNameException{
    	
        int temp;
        this.name = name; 
        this.description = description;
        
        for (int i = 0; i < raceList.size(); i++) { /* check for Illegal name exception */
			if (raceList.get(i).getRaceName() == name){
				throw new IllegalNameException("This name is currently being used by another race");
			}
		}
        if (name.isEmpty() || name.length() >= 256 || name.contains(" ")) { /* check for invalid name exception */
			throw new InvalidNameException("Name cannot be empty, include whitespaces, "
					+ "or be larger than the system limit of characters");
        }

        temp = raceList.size();
        if (temp > 0){ 
            	this.raceID = raceList.get(temp - 1).raceID + 1; /* generates raceID */
        } 
        else {
            	this.raceID = 0;
        }
        raceList.add(this); /* adds new race object to list of race objects */
    }
    public static int[] getRaceIDs() {
    	int[] raceIDs = new int[]{};
    	if (raceList.isEmpty()) {
    		return raceIDs;
    	}
    	else {
    		for(int i=0; i <raceList.size(); i++) {
    			raceIDs[i] = raceList.get(i).getRaceID(); /* loops through and returns race IDs */
    		}
    	}
    	return null;
    }
    public static void removeRace(int raceId) throws IDNotRecognisedException{
    	if (raceIdExists(raceId) == false) {
    		throw new IDNotRecognisedException("Race id does not exists in the system");
    	}
    	else {
    		Race temp;
            temp = getRaceObj(raceId);
            for (int i = 0; i < temp.raceStageList.size(); i++) {
                Stage.removeStageById(temp.raceStageList.get(i).getStageID()); /* removes race based on the corresponding id */
            }
            raceList.remove(raceList.indexOf(temp)); 
    	} 
    }

	public static int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime, StageType type) 
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException{
		if (raceIdExists(raceId) == false) {
    		throw new IDNotRecognisedException("Race id does not exists in the system"); /* check for id not recognised exception */
    	}
    	else {
    		for (int i = 0; i < Stage.stageList.size(); i++) {
    			if (Stage.stageList.get(i).getStageName() == stageName){
    				throw new IllegalNameException("This name is currently being used by another stage"); /* check for illegal name exception */
    			}
    		}
    		if (stageName.isEmpty() || stageName.length() >= 256 || stageName.contains(" ")) {
    			throw new InvalidNameException("Name cannot be empty, include whitespaces, " /* check for invalid name exception */
    					+ "or be larger than the system limit of characters");
    		}
    		else if (length < 5 || length == 0) {
    			throw new InvalidLengthException("Length cannot be null or less than 5km"); /* check for invalid length exception */
    		}
    		Stage stageObj = new Stage(raceId, stageName, description, length, startTime, type); /* if all checks are passed creates new object and adds to both relevant arraylists */
    		for(int i=0; i < raceList.size(); i++) {
    			if (raceList.get(i).getRaceID() == raceId) {
    				raceList.get(i).setRaceStageList(stageObj);
    			}
    		}
    		return stageObj.getStageID();
    	}
	}
	public static int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		if (raceIdExists(raceId) == false) {
    		throw new IDNotRecognisedException("Race id does not exists in the system");
    	}
    	else {
    		Race race = getRaceObj(raceId);
    		int[] raceStages = new int[race.raceStageList.size()];
    		for (int i = 0; i < race.raceStageList.size(); i++) {
    			if (race.raceStageList.get(i).getRaceID() == raceId) {
    				raceStages[i] = race.raceStageList.get(i).getStageID(); /* returns the races stages in the event the raceId is correct */
    			}
    		}
           return raceStages;
    	}	
    }

    public static int getNumberOfStages(int raceId) throws IDNotRecognisedException{
    	if (raceIdExists(raceId) == false) {
    		throw new IDNotRecognisedException("Race id does not exists in the system");
    	}
    	else {
    		Race temp;
        	temp = getRaceObj(raceId);
            int number = temp.raceStageList.size(); /* returns number of stages if raceId is correct */
            return number;
    	} 	
    }
    public static String viewRaceDetails(int raceId) throws IDNotRecognisedException{
    	if (raceIdExists(raceId) == false) {
    		throw new IDNotRecognisedException("Race id does not exists in the system");
    	}
    	else {
    		Race temp;
        	temp = getRaceObj(raceId);
        	int raceLen = 0;
        	for (int i = 0; i < temp.raceStageList.size(); i++) {
        		raceLen += temp.raceStageList.get(i).getLength();
        	}
        	return (temp.getRaceID() + " , " + temp.getRaceName() + " , " + temp.raceStageList.size() + " , " + raceLen); /* returns list of races details, relatively self explanatory */
    	}
    }
    /**
     * Checks for if a raceID is current in use (exists) or if it isn't.
     * @param raceId ID of race to check
     * @return boolean true / false
     */
    public static boolean raceIdExists(int raceId) {
    	boolean exists = false;
    	for(int i=0; i <raceList.size(); i++) {
    		if (raceList.get(i).getRaceID() == raceId) { /* check performed usually used in checks for invalid name exception to return if the result exists or not */
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
    /**
     * Adds the new stage object to the ArrayList of stage objects currently stored for this race object
     * @param passedObj Passed in stage object
     * @author Nathan
     */
    public void setRaceStageList(Stage passedObj) {
    	raceStageList.add(passedObj);
    }
    /**
     * Race ID getter
     * @return raceID
     * @author Nathan
     */
    public int getRaceID() {
    	return this.raceID;
    }
    /**
     * Race name getter
     * @return Race name
     * @author Nathan
     */
    public String getRaceName() {
    	return this.name;
    }
    /**
     * Race description getter
     * @return Race description
     * @author Nathan
     */
    public String getRaceDescription() {
    	return this.description;
    }
    /**
     * Returns the race object that stores the specific ID
     * @param passedID ID of race object you wish to obtain
     * @return Race object
     * @author Nathan
     */
    public static Race getRaceObj(int passedID){
        for (int i = 0; i < raceList.size(); i++) {
            if (raceList.get(i).getRaceID() == passedID) { /* returns the object with the corresponding stored raceId */
                return raceList.get(i);
            }
        }
        return null;
    }
    public static LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
    	if (raceIdExists(raceId) == false) {
    		throw new IDNotRecognisedException("Race id does not exists in the system");
    	}

    	key.keyList.clear(); /* clears the list of keys to help java garbage collection remove previously used key objects */
    	List<LocalTime> valuesList = new ArrayList<LocalTime>();
    	getAdjustedTimesAndId(raceId);
    	for (int i = 0; i < key.keyList.size(); i++) {
    		valuesList.add(key.keyList.get(i).getTime()); /* returns the time stored within the keys */
    	}
		LocalTime[] p = new LocalTime[valuesList.size()];
		valuesList.sort(new TimeComparator()); /* sorts list of times */
		for (int x = 0; x < valuesList.size(); x++) {
			p[x] = valuesList.get(x); /* stores values in relevant returned datatype */
		}
    	return p;
    }
    public static int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
    	if (raceIdExists(raceId) == false) {
    		throw new IDNotRecognisedException("Race id does not exists in the system");
    	}
    	List<LocalTime> timeList = new ArrayList<LocalTime>();
    	for (int i = 0; i < key.keyList.size(); i++) {
    		timeList.add(key.keyList.get(i).getTime());
    	}
		LocalTime[] riderTimeStore = getGeneralClassificationTimesInRace(raceId);
		int[] riderIdStore = new int[timeList.size()];
		for (int x = 0; x < key.keyList.size(); x++) {
			for (int y = 0; y < key.keyList.size(); y++) {
				if (riderTimeStore[x] == key.keyList.get(y).getTime()){ /* very similar to above function except returns corresponding ids in order of times */
					riderIdStore[x] = key.keyList.get(y).getID();
				}
			}
		}
    	return riderIdStore;
    }
    /**
     * Creates a set of new key objects for each individual rider and aggregate time they have for this current race
     * @param raceId ID of race that requires the keys
     * @throws IDNotRecognisedException
     * @author Nathan
     */
    public static void getAdjustedTimesAndId (int raceId) throws IDNotRecognisedException {
    	Race temp = getRaceObj(raceId);
    	boolean exists = false;
    	LocalTime[] currentRiderTimes; /* current checkpoint array being used within the loop */
    	LocalTime stageDuration; /* time between the first and the last element in the checkpoint array */
    	int includedStages[] = new int[temp.raceStageList.size()]; /* list of stage ids corresponding to the race */
    	int tempId, length;
    	for (int i = 0; i < temp.raceStageList.size(); i++) {
    		includedStages[i] = temp.raceStageList.get(i).getStageID(); 
    	}
    	
    	ArrayList<Result> relevantStageResults = new ArrayList<Result>();
    	for (int i = 0; i < includedStages.length; i++) {
    		relevantStageResults = Result.getAdjustedResultObject(includedStages[i]); /* assigns the arraylist the adjusted times for the current stage */
    		for (int t = 0; t < relevantStageResults.size(); t++) { /* loop for looping through stage arraylist for results */
    			currentRiderTimes = relevantStageResults.get(t).getRiderResults();
    			length = relevantStageResults.get(t).getRiderResults().length - 1;
    			tempId = relevantStageResults.get(t).getRiderID();
    			stageDuration = currentRiderTimes[length].minusNanos(currentRiderTimes[0].toNanoOfDay());
    			
    			for (int x = 0; x < key.keyList.size(); x++) {
    				if (key.keyList.get(x).getID() == tempId) {
    					key.keyList.get(x).setTime(stageDuration); /* checks to see if the key is already present from another stage and aggregates the time in the event it does */
        				exists = true;	
    				}
    			}
    			if (exists == false) { /* if key doesnt already exist creates a new key object */
    				key newKey = new key(stageDuration, tempId);
        			key.keyList.add(newKey);
        			newKey = null;
    			}		
    		}
    	}
    }
    
    
    /**
     * Custom comparator that compares a list of aggregate times and returns them ordered in ascending order
     * @author Nathan
     *
     */
    static class TimeComparator implements Comparator<LocalTime> {
	     public int compare(LocalTime result1, LocalTime result2) {
	         return result1.compareTo(result2); /* compares two passed in localtimes */
	     }
	 }
    /**
     * Class used to store the rider ID and time of that specific rider in an object such that they can be accessed via one another
     * @author Nathan
     *
     */
    static class key {
    	LocalTime time;
    	int riderId;
    	/**
    	 * ArrayList containing the current list of key objects in use
    	 */
    	public static ArrayList<key> keyList = new ArrayList<key>();
    	/**
    	 * Creates a new instance of the key object
    	 * @param time Finishing time of the specific rider
    	 * @param riderId ID of the specified rider
    	 * 
    	 */
    	public key(LocalTime time, int riderId) {
    		this.time = time;
    		this.riderId = riderId; /* adding to the keyList is done externally typically when the constructor is called */
    	}
    	/**
    	 * Aggregate time getter
    	 * @return the aggregate time of this key object
    	 */
    	public LocalTime getTime() {
    		return this.time;
    	}
    	/**
    	 * ID getter
    	 * @return The Rider ID of this key object
    	 */
    	public int getID() {
    		return this.riderId;
    	}
    	/**
    	 * Adds time passed in to the aggregate time stored for this key object
    	 * @param passed passed in LocalTime value
    	 */
    	public void setTime(LocalTime passed) {
    		this.time = this.time.plus(Duration.ofNanos(passed.toNanoOfDay())); /* aggregates the time */
    	}
    }
    /**
     * Removes a race from the list of race objects by name attribute
     * @param name name of race you wish to remove
     */
    public static void removeRaceByName(String name) {
        for (int i=0; i < raceList.size(); i++) {
            if (name == raceList.get(i).getRaceName()) {
                raceList.remove(raceList.get(i).getRaceID());
            }
        }
    }
    public static int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
        Hashtable<Integer, Integer> pointsTable = new Hashtable<Integer, Integer>();
        ArrayList<Integer> ridersPoints = new ArrayList<Integer>();
        int[] riderPointArr;
        int[] tempStageList = Race.getRaceStages(raceId);
        int[] stagePoints = Result.getRidersPointsInStage(tempStageList[0]);
        int[] tempRiders = Result.getRidersRankInStage(tempStageList[0]);
        for (int i = 0; i < tempRiders.length; i++) {
            pointsTable.put(tempRiders[i], stagePoints[i]);
        }
        for (int i = 1; i < tempStageList.length; i++) {
            stagePoints = Result.getRidersPointsInStage(tempStageList[i]);
            tempRiders = Result.getRidersRankInStage(tempStageList[i]);
            for (int j = 1; j < stagePoints.length; j++) {
                pointsTable.put(tempRiders[j], pointsTable.get(tempRiders[j])+stagePoints[i]);
            }
        }
        Set<Integer> keys = pointsTable.keySet();
        for(int key: keys){
            ridersPoints.add(pointsTable.get(key));
        }
        riderPointArr = new int[ridersPoints.size()];
        for(int i = 0; i < ridersPoints.size(); i ++) {
			riderPointArr[i] = ridersPoints.get(i);
		}
        return riderPointArr;
    }
    public static int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
        Hashtable<Integer, Integer> pointsTable = new Hashtable<Integer, Integer>();
        ArrayList<Integer> ridersPoints = new ArrayList<Integer>();
        int[] riderPointArr;
        int[] tempStageList = Race.getRaceStages(raceId);
        int[] stagePoints = Result.getRidersMountainPointsInStage(tempStageList[0]);
        int[] tempRiders = Result.getRidersRankInStage(tempStageList[0]);
        for (int i = 0; i < tempRiders.length; i++) {
            pointsTable.put(tempRiders[i], stagePoints[i]);
        }
        for (int i = 1; i < tempStageList.length; i++) {
            stagePoints = Result.getRidersMountainPointsInStage(tempStageList[i]);
            tempRiders = Result.getRidersRankInStage(tempStageList[i]);
            for (int j = 1; j < stagePoints.length; j++) {
                pointsTable.put(tempRiders[j], pointsTable.get(tempRiders[j])+stagePoints[i]);
            }
        }
        Set<Integer> keys = pointsTable.keySet();
        for(int key: keys){
            ridersPoints.add(pointsTable.get(key));
        }
        riderPointArr = new int[ridersPoints.size()];
        for(int i = 0; i < ridersPoints.size(); i ++) {
			riderPointArr[i] = ridersPoints.get(i);
		}
        return riderPointArr;
    } 
    public static int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException{
        Race tempRace;
        tempRace = Race.getRaceObj(raceId);
        Hashtable<Integer, Integer> tempTable = new Hashtable<Integer, Integer>();
        int[] riderIds = Result.getRidersRankInStage((Race.getRaceStages(raceId))[0]);
        int[] points = getRidersPointsInRace(raceId);
        for (int i = 0; i < points.length; i++) {
            tempTable.put(points[i], riderIds[i]);
        }
        for (int i = 0; i < points.length; i++) {
            for(int j = i+1; j < points.length; j++) {
                int tempI = points[i];
                int tempJ = points[j];
                if(tempI > tempJ){
                    points[i]= tempJ;
                    points[j]= tempI;
                }
            }
        }
        for (int i = 0; i < points.length; i++) {
            riderIds[i] = tempTable.get(points[i]);
        }
        return riderIds;
    }
    public static int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException{
        Race tempRace;
        tempRace = Race.getRaceObj(raceId);
        Hashtable<Integer, Integer> tempTable = new Hashtable<Integer, Integer>();
        int[] riderIds = Result.getRidersRankInStage((Race.getRaceStages(raceId))[0]);
        int[] points = getRidersMountainPointsInRace(raceId);
        for (int i = 0; i < points.length; i++) {
            tempTable.put(points[i], riderIds[i]);
        }
        for (int i = 0; i < points.length; i++) {
            for(int j = i+1; j < points.length; j++) {
                int tempI = points[i];
                int tempJ = points[j];
                if(tempI > tempJ){
                    points[i]= tempJ;
                    points[j]= tempI;
                }
            }
        }
        for (int i = 0; i < points.length; i++) {
            riderIds[i] = tempTable.get(points[i]);
        }
        return riderIds;
    }
}

