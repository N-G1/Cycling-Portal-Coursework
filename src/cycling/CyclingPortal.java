package cycling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CyclingPortal implements CyclingPortalInterface{

	@Override
	public int[] getRaceIds() {
		return Race.getRaceIDs();
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		Race raceObj = new Race(name, description);
		return raceObj.getRaceID();
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		return Race.viewRaceDetails(raceId);
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		Race.removeRace(raceId);
		
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		return Race.getNumberOfStages(raceId);
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
			StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		return Race.addStageToRace(raceId, stageName, description, length, startTime, type);
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		return Race.getRaceStages(raceId);
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		return Stage.getStageLength(stageId);
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		Stage.removeStageById(stageId);
		
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, SegmentType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		return Stage.addCategorizedClimbToStage(stageId, location, type, averageGradient, length);
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		return Stage.addIntermediateSprintToStage(stageId, location);
	}

	@Override
	public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {
		Segment.removeSegment(segmentId);
		
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		Stage.concludeStagePreperation(stageId);
		
	}

	@Override
	public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
		return Stage.getStageSegments(stageId);
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		Team obj = new Team(name, description);
		return obj.getTeamID(); 
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		Team.removeTeam(teamId);
	}

	@Override
	public int[] getTeams() {
		return Team.getAllTeamIDs();   
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		return Team.getTeamRiders(teamId);
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		Rider riderObj = new Rider(name, yearOfBirth, teamID);
		return riderObj.getRiderID();
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		Rider.removeRider(riderId);
		
	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
			InvalidStageStateException {
		@SuppressWarnings("unused")
		Result resultObj = new Result(stageId, riderId, checkpoints);
		
	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		return Result.getRiderResultsInStage(stageId, riderId); 
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		return Result.getRiderAdjustedElapsedTimeInStage(stageId, riderId); 
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Result.deleteRiderResultsInStage(stageId, riderId);
		
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		return Result.getRidersRankInStage(stageId);
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		return Result.getRankedAdjustedElapsedTimesInStage(stageId);
	} 

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		return Result.getRidersPointsInStage(stageId);
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		return Result.getRidersMountainPointsInStage(stageId);
	}

	@Override
	public void eraseCyclingPortal() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		Race.removeRaceByName(name);
		
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		return Race.getRidersGeneralClassificationRank(raceId);
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		return Race.getGeneralClassificationTimesInRace(raceId);
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		return Race.getRidersPointsInRace(raceId);
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		return Race.getRidersMountainPointsInRace(raceId);
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		return Race.getRidersPointClassificationRank(raceId);
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		return Race.getRidersMountainPointClassificationRank(raceId);
	}

}
