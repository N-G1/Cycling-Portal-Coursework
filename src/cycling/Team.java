package cycling;

import java.util.*;

/**
 * Class containing all Team related methods and variables
 * @author Nathan
 *
 */
public class Team {
	private String name, description;
	private int teamID;
	/**
	 * Arraylist of all currently created teams
	 * @author Nathan
	 */
	static public ArrayList<Team> teamStore = new ArrayList<Team>();
	
	public Team(String name, String description) throws IllegalNameException, InvalidNameException{
		this.description = description;
		this.name = name;
		
		for (int i = 0; i < teamStore.size(); i++) {
			if (teamStore.get(i).getName() == name){
				throw new IllegalNameException("This name is currently being used by another team"); /* Name exception check */
			}
		}
        if (name.isEmpty() || name.length() >= 256 || name.contains(" ")) {
			throw new InvalidNameException("Name cannot be empty, include whitespaces, "
					+ "or be larger than the system limit of characters"); /* Invalid name exception check */
        }
		int x = teamStore.size();
        if (x > 0){ 
        	this.teamID = teamStore.get(x - 1).teamID + 1;
        } 
        else {
        	this.teamID = 0;
        }
		
		teamStore.add(this);
	}
	/**
	 * Returns the team object with the stored passed in team ID
	 * @param passedID Team ID of object you wish to get 
	 * @return Team object
	 * @author Nathan
	 */
	public static Team getTeamObj(int passedID){
        for (int i = 0; i < teamStore.size(); i++) {
            if (teamStore.get(i).getTeamID() == passedID) {
                return teamStore.get(i);
            }
        }
        return null;
    }
	/**
	 * Check performed to see if passed in team ID is in use (exists)
	 * @param teamId team ID you wish to check against
	 * @return true if exists / false if not 
	 */
	public static boolean teamIdExists(int teamId) {
    	boolean exists = false;
    	for(int i=0; i <teamStore.size(); i++) {
    		if (teamStore.get(i).getTeamID() == teamId) {
    			exists = true; /* teamId is found to exist */
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
	 * Team ID getter
	 * @return teamID
	 * @author Nathan
	 */
	public int getTeamID(){
		return this.teamID;
	}
	/**
	 * Team ID setter
	 * @param passed
	 * @author Nathan
	 */
	public void setTeamID(int passed){
		this.teamID = passed;
	}
	/**
	 * Name getter
	 * @return name
	 * @author Nathan
	 */
	public String getName(){
		return this.name;
	}
	/**
	 * Description getter
	 * @return description
	 * @author Nathan
	 */
	public String getDescription(){
		return this.description;
	}
	
	public static int[] getAllTeamIDs() {
		if (teamStore.isEmpty()) {
			return new int[]{}; /* if no teams exist returns empty array */
		}
		int length;
		length = teamStore.size();	
		int[] teamIDs = new int[length];
		

		for(int i = 0; i <= teamStore.size() - 1; i++) {
			teamIDs[i] = teamStore.get(i).getTeamID(); /* gets all team IDs and stores them in an array */
		}
		return teamIDs;
	}
	public static void removeTeam(int teamId) throws IDNotRecognisedException {
		if (teamIdExists(teamId) == false) {
    		throw new IDNotRecognisedException("Team id does not exists in the system"); /* ID not recognised check */
    	}
        Team tempTeam;
        tempTeam = getTeamObj(teamId);
        for (int i = 0; i < Rider.riderList.size(); i++) {
        	if (Rider.riderList.get(i).getTeamId() == teamId){
        		Rider.removeRider(Rider.riderList.get(i).getRiderID()); /* removes team based on ID and cascades down to remove riders */
        	}
        }
        teamStore.remove(teamStore.indexOf(tempTeam));
	}
	
	public static int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		if (teamIdExists(teamId) == false) {
    		throw new IDNotRecognisedException("Team id does not exists in the system"); /* ID not recognised check */
    	}
		int[] riderIdList;
		int temp = 0;
		for (int i = 0; i < Rider.riderList.size(); i++) {
        	if (Rider.riderList.get(i).getTeamId() == teamId){
        		temp += 1; /* gets how many riders are stored for this particular team */
        	}
        }
		riderIdList = new int[temp];
		for (int i = 0; i < Rider.riderList.size(); i++) {
        	if (Rider.riderList.get(i).getTeamId() == teamId){
        		riderIdList[i] = Rider.riderList.get(i).getRiderID(); /* stores all rider IDs for this team */
        	}
        }
	return riderIdList; /* returns list of rider IDS */
	}
	
}
