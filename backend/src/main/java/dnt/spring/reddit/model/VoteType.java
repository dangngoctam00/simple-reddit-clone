package dnt.spring.reddit.model;


public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1),
    ;
	
	private int direction;
	
    VoteType(int direction) {
    	this.direction = direction;
    }
    
    public int getVoteCount() {
    	return this.direction; 
    }
}