import java.util.*;

public class PlayerImpl implements Player {
	// Identifies the player
	private int name = 0;
	int n = 0;

	// Constructor
	public PlayerImpl(int name, int n) {
		this.name = 0;
		this.n = n;
	}

	// Function to find possible successors
	@Override
	public ArrayList<Integer> generateSuccessors(int lastMove, int[] crossedList) 
	{
		// arrayList to store the successors
		ArrayList<Integer> successors = new ArrayList<Integer>();
		
		// if it is the first move
		if(lastMove == -1)
		{
			for(int i = 1; i < (this.n + 1) / 2; i++)
			{
				if(i % 2 == 0)
				{
					successors.add(0, i);
				}
			}
			return successors;
		}
		
		// if it is not the first move
		for(int i = 1; i <= this.n; i++)
		{
			// the number i hasn't been crossed out
			if(crossedList[i] == 0)
			{
				// if it is a multiple or factor of the last move
				if(i % lastMove == 0 || lastMove % i == 0)
				{
					successors.add(0, i);
				}
			}
		}
		return successors;
	}

	// The max value function
	@Override
	public int max_value(GameState s) 
	{
		ArrayList<Integer> successors = generateSuccessors(s.lastMove, s.crossedList);
		
		// if s is a leaf
		if(successors.isEmpty())
		{
			s.leaf = true;
			s.bestMove = -1;
			return -1;
		}
		else
		{
			int alpha = Integer.MIN_VALUE;
			for(Integer i : successors)
			{
				GameState successor = new GameState(s.crossedList, i);
				
				// update the crossedList of this state of successor
				successor.crossedList[i] = 1;
				int temp = min_value(successor);
				if(alpha < temp)
				{
					alpha = temp;
					s.bestMove = i;
					
					// There are only two possible values for this game, -1 or 1.
					// Thus, the maximum value alpha can get is 1. If alpha gets
					// 1, it cannot be bigger. Therefore, return.
					
					// Besides, as we traverse the list of successors from the biggest
					// number to smallest number, if there are multiple numbers 
					// yielding the best value, maximum of those numbers is returned.
					if(alpha == 1)
					{
						return alpha;
					}
				}
			}
			return alpha;
		}
	}

	// The min value function
	@Override
	public int min_value(GameState s) 
	{
		ArrayList<Integer> successors = generateSuccessors(s.lastMove, s.crossedList);
		if(successors.isEmpty())
		{
			s.leaf = true;
			s.bestMove = -1;
			return 1;
		}
		else
		{
			int beta = Integer.MAX_VALUE;
			for(Integer i : successors)
			{
				GameState successor = new GameState(s.crossedList, i);
				successor.crossedList[i] = 1;
				int temp = max_value(successor);
				if(beta > temp)
				{
					beta = temp;
					s.bestMove = i;
					
					// The same case as the max_value function since the minimum 
					// value beta can get is -1.
					if(beta == -1)
					{
						return beta;
					}
				}
			}
			return beta;
		}
	}

	// Function to find the next best move
	@Override
	public int move(int lastMove, int[] crossedList) 
	{
		GameState s = new GameState(crossedList, lastMove);
		max_value(s);
		return s.bestMove;
	}
}