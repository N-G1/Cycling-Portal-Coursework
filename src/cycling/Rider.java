package cycling;

import java.util.ArrayList;
/**
 * Class containing all rider related methods and variables 
 * @author Nathan
 *
 */
public class Rider {
	  String name;
	  private int yearOfBirth, teamID, riderID;
	  /**
	   * Stores all current rider object
	   * @author Nathan
	   */
	  public static ArrayList<Rider> riderList = new ArrayList<Rider>();
	    public Rider(String name, int yearOfBirth, int teamID) throws IDNotRecognisedException, IllegalArgumentException{
	        try {
	        	this.name = name;
		        this.yearOfBirth = yearOfBirth;
		        this.teamID = teamID;
			}
	        catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Illegal argument passed via one of the variables"); /* Illegal argument check */
			}
	       
	        if (Team.teamIdExists(teamID) == false) {
	    		throw new IDNotRecognisedException("Team id does not exists in the system"); /* ID check */
	    	}
	        
	        int x = riderList.size();
	        if (x > 0){ 
	        	this.riderID = riderList.get(x - 1).riderID + 1;
	        } 
	        else {
	        	this.riderID = 0;
	        }
	        riderList.add(this);
	        
	    }
	    public static void removeRider(int riderID) throws IDNotRecognisedException {
	    	if (riderIdExists(riderID) == false) {
	    		throw new IDNotRecognisedException("Team id does not exists in the system");
	    	}
	    	Rider temp;
	    	temp = getRiderObj(riderID);
	        riderList.remove(riderList.indexOf(temp)); /* removes rider from ArrayList based on the index of the object with the related passed ID */
	    }
	    /**
	     * Rider ID getter
	     * @return Rider ID
	     * @author Nathan
	     */
	    public int getRiderID(){
			return this.riderID;
		}
	    /**
	     * Gets rider object with the related passed ID and returns it 
	     * @param passedID ID of rider you wish to get 
	     * @return Rider object
	     * @author Nathan
	     */
	    public static Rider getRiderObj(int passedID){
	        for (int i = 0; i < riderList.size(); i++) {
	            if (riderList.get(i).getRiderID() == passedID) {
	                return riderList.get(i);
	            }
	        }
	        return null;
	    }
	    /**
	     * Check for if this rider ID is already in use (exists)
	     * @param riderId ID you wish to check against
	     * @return true if exists / false if not
	     * @author Nathan
	     */
	    public static boolean riderIdExists(int riderId) {
	    	boolean exists = false;
	    	for(int i=0; i <riderList.size(); i++) {
	    		if (riderList.get(i).getRiderID() == riderId) {
	    			exists = true; /* rider ID is found to exist */
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
	     * Name getter
	     * @return name
	     * @author Nathan
	     */
	    public String getName() {
	    	return this.name;
	    }
	    /**
	     * Year of birth getter
	     * @return yearOfBirth
	     * @author Nathan
	     */
	    public int getYearOfBirth() {
	    	return this.yearOfBirth;
	    }
	    /**
	     * Team ID getter
	     * @return teamID
	     * @author Nathan
	     */
	    public int getTeamId() {
	    	return this.teamID;
	    }
	}
