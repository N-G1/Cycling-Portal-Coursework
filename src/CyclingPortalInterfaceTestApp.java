import cycling.CyclingPortal;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

import javax.naming.LinkLoopException;

import org.junit.jupiter.api.io.TempDir;
import org.junit.platform.commons.util.ToStringBuilder;

import cycling.BadMiniCyclingPortal;
import cycling.CyclingPortalInterface;
import cycling.DuplicatedResultException;
import cycling.IDNotRecognisedException;
import cycling.IllegalNameException;
import cycling.InvalidCheckpointsException;
import cycling.InvalidLengthException;
import cycling.InvalidLocationException;
import cycling.InvalidNameException;
import cycling.InvalidStageStateException;
import cycling.InvalidStageTypeException;
import cycling.MiniCyclingPortalInterface;
import cycling.Result;
import cycling.SegmentType;
import cycling.StageType;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the CyclingPortalInterface interface -- note you
 * will want to increase these checks, and run it on your CyclingPortal class
 * (not the BadCyclingPortal class).
 *
 * 
 * @author Diogo Pacheco
 * @version 1.0
 */
public class CyclingPortalInterfaceTestApp {
//	static int var, var2, var3, var4;
//	static int[] x;
	static String temp = "", temp2 = "", temp3 = "", adjusted ="", generalClassificationString ="", teamIds = "", raceStages = "", segmentIdString = "", teamIdString = "", teamRiderString = "riders ", points = "points ", mountainpointString = "m points ";
	static int[] t;
	static int testID = 0, testID2 = 1, testID3 = 12, testID4 = 2;
    static int testRiderID = 0, testRiderID2 = 1, testRiderID3 = 2, testRiderID4 = 3;
	static LocalTime checkpoints[] = new LocalTime[3], checkpoints2[] = new LocalTime[3], checkpoints3[] = new LocalTime[3], checkpoints4[] = new LocalTime[3];
	static LocalTime x[], y[], adjustedResults[], generalClassification[];
	/**
	 * Test method.
	 * 
	 * @param args not used
	 * @throws InvalidNameException 
	 * @throws IllegalNameException 
	 * @throws IDNotRecognisedException 
	 * @throws InvalidStageStateException 
	 * @throws InvalidCheckpointsException 
	 * @throws DuplicatedResultException 
	 * @throws InvalidLengthException 
	 * @throws InvalidStageTypeException 
	 * @throws InvalidLocationException 
	 */
	public static void main(String[] args) throws IllegalNameException, InvalidNameException, IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException, InvalidStageStateException, InvalidLengthException, InvalidLocationException, InvalidStageTypeException {
		System.out.println("The system compiled and started the execution...");

		CyclingPortalInterface portal = new CyclingPortal();
//		CyclingPortalInterface portal = new BadCyclingPortal();

		assert (portal.getRaceIds().length == 0)
				: "Innitial SocialMediaPlatform not empty as required or not returning an empty array.";
//		var = portal.createTeam("name", "desc");
//		var2 = portal.createTeam("name2", "desc2");
//		var3 = portal.createTeam("name3", "desc3");
//		var4 = portal.createTeam("name4", "desc4"); 
//		x = portal.getTeams();
//		System.out.println(var + " , " + var2 + " , " + var3 + " , " + var4);	
		/* testing for get teams method */
//		for (int i = 0; i <= x.length - 1; i++) {
//			temp += x[i] + " , ";
//		}
//		System.out.println("team ids: " + temp );
		portal.createRace("name", "desc");
		portal.createTeam("name", "description");
		portal.addStageToRace(testID, "sname", "desc", 6.0, LocalDateTime.now(), StageType.FLAT);
		portal.addStageToRace(testID, "name", "desc", 8.0, LocalDateTime.now(), StageType.FLAT);
		portal.createRider(testID, "NAME", 2001);
		portal.createRider(testID, "NAME2", 2001);
		portal.createRider(testID, "NAME3", 2001);
		portal.createRider(testID, "NAME4", 2001);
		checkpoints[0] = (LocalTime.parse("03:14:53"));
		checkpoints[1] = (LocalTime.parse("03:00:53"));
		checkpoints[2] = (LocalTime.parse("05:03:13")); /* 0 */
		checkpoints2[0] = (LocalTime.parse("07:14:53"));
		checkpoints2[1] = (LocalTime.parse("17:03:12.874"));
		checkpoints2[2] = (LocalTime.parse("20:03:12.874")); /* 1 */
		checkpoints3[0] = (LocalTime.parse("12:14:53"));
		checkpoints3[1] = (LocalTime.parse("20:00:14"));
		checkpoints3[2] = (LocalTime.parse("20:05:14")); /* 2 */
		checkpoints4[0] = (LocalTime.parse("05:14:53"));
		checkpoints4[1] = (LocalTime.parse("20:14:53"));
		checkpoints4[2] = (LocalTime.parse("20:03:13.312")); /* 3 */
		System.out.println(portal.addCategorizedClimbToStage(testID, Double.valueOf(1), SegmentType.C1, Double.valueOf(10), Double.valueOf(5)));
		System.out.println(portal.addIntermediateSprintToStage(testID, Double.valueOf(1)));
		portal.concludeStagePreparation(testID);
		portal.concludeStagePreparation(testID2);
		portal.registerRiderResultsInStage(testID, testRiderID, checkpoints);
		portal.registerRiderResultsInStage(testID, testRiderID2, checkpoints2);
		portal.registerRiderResultsInStage(testID, testRiderID3, checkpoints3);
		portal.registerRiderResultsInStage(testID, testRiderID4, checkpoints4);
		portal.registerRiderResultsInStage(testID2, testRiderID4, checkpoints4);
		portal.registerRiderResultsInStage(testID2, testRiderID3, checkpoints4);
		
		
		x = portal.getRiderResultsInStage(testID, testRiderID4);
		y = portal.getRiderResultsInStage(testID, testRiderID2);
		for (int i = 0; i <= x.length - 1; i++) {
			temp += x[i] + " , ";
		}
		System.out.println("rider times: " + temp );
		for (int i = 0; i <= y.length - 1; i++) {
			temp2 += y[i] + " , ";
		}
		System.out.println("rider times: " + temp2);
		System.out.println(portal.getRiderAdjustedElapsedTimeInStage(testID, testRiderID2));
//		portal.deleteRiderResultsInStage(testID, testRiderID2);
		System.out.println(Result.allResultList);
		t = portal.getRidersRankInStage(testID);
		for (int i = 0; i <= t.length - 1; i++) {
			temp3 += t[i] + " , ";
		}
		System.out.println(temp3);
		adjustedResults = portal.getRankedAdjustedElapsedTimesInStage(testID);
		for (int i = 0; i <= adjustedResults.length - 1; i++) {
			adjusted += adjustedResults[i] + " adjusted ";			
		}
		System.out.println(adjusted);
		generalClassification = portal.getGeneralClassificationTimesInRace(testID);
		for (int i = 0; i <= generalClassification.length - 1; i++) {
			generalClassificationString += generalClassification[i] + " , ";
		}
		System.out.println(generalClassificationString);
		int[] generalRank = portal.getRidersGeneralClassificationRank(testID);
		String generalRankstring = " ";
		for (int i = 0; i <= generalRank.length - 1; i++) {
			generalRankstring += generalRank[i] + " , ";
		}
		System.out.println(generalRankstring);
		
		//portal.removeRaceById(0);
		//portal.removeStageById(testID);
		for (int i = 0; i <= portal.getTeams().length - 1; i++) {
			teamIds+= portal.getTeams()[i] + " , ";
		}
		System.out.println(teamIds);
		System.out.println(portal.viewRaceDetails(0));
		System.out.println(portal.getNumberOfStages(0));
		portal.getRaceStages(testID);
		for (int i = 0; i < portal.getRaceStages(testID).length; i++) {
			raceStages += portal.getRaceStages(testID)[i] + " , ";
		}
		System.out.println(raceStages);
		System.out.println(portal.getStageLength(testID));
		//portal.concludeStagePreparation(testID);
		//portal.removeSegment(testID);
		for (int i = 0; i < portal.getStageSegments(testID).length; i++) {
			segmentIdString += portal.getStageSegments(testID)[i] + " , ";
		}
		System.out.println(segmentIdString);
		portal.createTeam("nam", "description");
		for (int i = 0; i < portal.getTeams().length; i++) {
			teamIdString += portal.getTeams()[i] + " , ";
		}
		System.out.println(teamIdString);
//		portal.removeRider(testID);
		for (int i = 0; i < portal.getTeamRiders(testID).length; i++) {
			teamRiderString += portal.getTeamRiders(testID)[i] + " , ";
		}
		System.out.println(teamRiderString);
		for (int i = 0; i < portal.getRidersPointsInStage(testID).length; i++) {
			points += portal.getRidersPointsInStage(testID)[i] + " , ";
		}
		System.out.println(points);
		for (int i = 0; i < portal.getRidersMountainPointsInStage(testID).length; i++) {
			mountainpointString += portal.getRidersMountainPointsInStage(testID)[i] + " , ";
		}
		System.out.println(mountainpointString);
		System.out.println(points);
		String nan = "point classification ";
		for (int i = 0; i < portal.getRidersPointClassificationRank(testID).length; i++) {
			nan += portal.getRidersPointClassificationRank(testID)[i] + " , ";
		}
		System.out.println(nan);
		String nan2 = "mountain classification ";
		for (int i = 0; i < portal.getRidersMountainPointClassificationRank(testID).length; i++) {
			nan2 += portal.getRidersMountainPointClassificationRank(testID)[i] + " , ";
		}
		System.out.println(nan2);
		String t = "rider rank ";
		for (int i = 0; i < portal.getRidersRankInStage(testID).length; i++) {
			t += portal.getRidersRankInStage(testID)[i] + " , ";
		}
		System.out.println(t);
	}
}
