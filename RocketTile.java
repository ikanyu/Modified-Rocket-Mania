
public class RocketTile extends Tile{
	
	public RocketTile(){
		this.rocket = true;
		this.fuel=false;
		this.left = true;
	}
	
	public void rotate() {
		// does nothing/can't be rotated
		
	}

	public Tile[] getAdjacent(Tile[][] gameBoard) {
		
		Tile[] t = new Tile[1];
		
		// condition make sure that we don't go out of the 
		// bounds of the game board.
		if(x>0){
			t[0] = gameBoard[y][x-1];
		}
		return t;
	}

	public void setX(int x) {
		this.x = x;
		
	}

	public void setY(int y) {
		this.y = y;
		
	}

}
