package cycling;

import java.util.ArrayList;
/**
 * Class containing all related methods and variables of segments of a stage  
 * @author Nathan
 *
 */
public class Segment {
	int stageID, segmentID;
    double location, averageGradient, length;
    SegmentType type;
    boolean isIntermediateSprint = false;
    /**
     * List of all segments currently stores for all stages 
     */
    public static ArrayList<Segment> segmentList = new ArrayList<Segment>();
    
	public Segment(int stageId, double location, SegmentType type, Double averageGradient, /*  Constructor for Climb */
			Double length) {
		int temp;
		this.location = location;
		this.type = type;
		this.averageGradient = averageGradient;
		this.length = length;
		this.stageID = stageId;
		temp = segmentList.size();
        if (temp > 0){ 													/* check for if this is the first id */
            	this.segmentID = segmentList.get(temp).segmentID + 1;	/* in the system or if it needs to be calculated */
        } 																/* based of previous ID, this is performed in every constructor */
        else {															/* and is only commented on here */
            	this.segmentID = 0;
        }
        segmentList.add(this);
	}
	
	public Segment(int stageId, Double location) { /* Constructor for Sprint */
		int temp;
		this.location = location;
		this.stageID = stageId;
		this.isIntermediateSprint = true;
		temp = segmentList.size();
        if (temp > 0){ 
            	this.segmentID = segmentList.get(temp - 1).segmentID + 1;
        } 
        else {
            	this.segmentID = 0;
        }
        segmentList.add(this);
	}
	/**
	 * Returns segment ID
	 * @return segmentID
	 * @author Nathan
	 */
	public int getSegmentID() {
		return this.segmentID;
	}
	/**
	 * Returns the segment object with the stored ID passed in 
	 * @param passedID Segment ID to be checked against
	 * @return relevant Segment object
	 * @author Nathan
	 */
	public static Segment getSegmentObj(int passedID){
        for (int i = 0; i < segmentList.size(); i++) {
            if (segmentList.get(i).getSegmentID() == passedID) {
                return segmentList.get(i); /* returns relevant segment ID */
            }
        }
        return null;
    }
	/**
	 * Verifies if segment ID is currently in use
	 * @param segmentId ID to be checked against
	 * @return true if exists / false if not
	 * @author Nathan
	 */
	public static boolean segmentIdExists(int segmentId) {
    	boolean exists = false;
    	for(int i=0; i <segmentList.size(); i++) {
    		if (segmentList.get(i).getSegmentID() == segmentId) {
    			exists = true; /* segment ID is currently stored */
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
	public static void removeSegment(int segmentID) throws IDNotRecognisedException{
		if (segmentIdExists(segmentID) == false) {
    		throw new IDNotRecognisedException("Segment id does not exists in the system"); /* Invalid ID check */
    	}
        Segment tempSegment;
        Stage tempStage;
        tempSegment = getSegmentObj(segmentID);
        tempStage = Stage.getStageObj(tempSegment.getStageID());
        tempStage.stageSegmentList.remove(tempStage.stageSegmentList.indexOf(tempSegment));
        segmentList.remove(segmentList.indexOf(tempSegment)); /* removes segment based on ID */
    }
	/**
	 * Location getter
	 * @return location
	 * @author Nathan
	 */
	public double getLocation() {
		return this.location;
	}
	/**
	 * Type getter
	 * @return type
	 * @author Nathan
	 */
	public SegmentType getType() {
		return this.type;
	}
	/**
	 * Avg Gradient getter
	 * @return averageGradient
	 * @author Nathan
	 */
	public double avgGradient() {
		return this.averageGradient;
	}
	/**
	 * Length getter
	 * @return length
	 * @author Nathan
	 */
	public double getLength() {
		return this.length;
	}
	/**
	 * Stage ID getter
	 * @return stageID
	 * @author Nathan
	 */
	public int getStageID() {
		return this.stageID;
	}
	/**
	 * Intermediate Sprint boolean getter
	 * @return isIntermediateSprint 
	 * @author Nathan
	 */
	public boolean getIsIntermediateSprint() {
		return this.isIntermediateSprint;
	}
}