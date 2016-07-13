import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.LinkedList;

public class AStarSearchImpl implements AStarSearch 
{	
	@Override	
	public SearchResult search(String initConfig, int modeFlag) 
	{
		// store the config and real cost of the states which are in open
		Map<String, Integer> open = new HashMap<String, Integer>();
		
		// store the config and real cost of the states which are in closed
		Map<String, Integer> closed = new HashMap<String, Integer>();
		
		// number of popped states
		int numPoppedStates = 0;
		
		// OPEN
		PriorityQueue<State> openQueue = new PriorityQueue<State>(10, State.comparator);
		
		// generate the state state and put it into openQueue and open
		State s = new State(initConfig, 0, getHeuristicCost(initConfig, modeFlag), "");
		openQueue.add(s);
		open.put(initConfig, 0);
		
		while(!openQueue.isEmpty())
		{
			// remove from openQueue & open and place in closed, a state n for which
			// f(n) is the minimum
			State n = openQueue.remove();
			open.remove(n.config);
			closed.put(n.config, n.realCost);
			numPoppedStates ++;
			
			if(checkGoal(n.config))
			{
				return new SearchResult(n.config, n.opSequence, numPoppedStates);
			}
			
			// expand n, generating all its successors
			LinkedList<State> successors = new LinkedList<State>();
			for(char i = 'A'; i <= 'H'; i++)
			{
				String newConfig = move(n.config, i);
				successors.add(new State(newConfig, n.realCost + 1,
						getHeuristicCost(newConfig, modeFlag), n.opSequence + i));
			}
			
			for(State successor : successors)
			{
				Integer gOpen = open.get(successor.config);
				Integer gClosed = closed.get(successor.config);
				
				// if this successor is not in open or closed 
				if(gOpen == null && gClosed == null)
				{
					openQueue.add(successor);
					open.put(successor.config, successor.realCost);
				}
				else
				{
					// if this successor is in open
					if(gClosed == null)
					{
						// if g(successor) is strictly less than its old g value
						// update g(successor) in openQueue and open
						if(successor.realCost < gOpen.intValue())
						{
							openQueue.remove(successor);
							openQueue.add(successor);
							open.remove(successor.config);
							open.put(successor.config, successor.realCost);
						}
					}
					// if this successor is in closed
					else
					{
						// if g(successor) is strictly less than its old g value
						// remove from closed and place it on openQueue and open
						// with new g
						if(successor.realCost < gClosed.intValue())
						{
							openQueue.add(successor);
							open.put(successor.config, successor.realCost);
							closed.remove(successor.config);
						}
					}
				}
						
			}
		}
		return null;
	}

	@Override
	public boolean checkGoal(String config) 
	{
		// index of the blocks in the center square in char array
		int[] center = new int[] {6, 7, 8, 11, 12, 15, 16, 17};
		char c = config.charAt(center[0]);
		for(int i = 1; i < center.length; i++)
		{
			if(c != config.charAt(center[i]))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public String move(String config, char op) 
	{
		char[] blocks = config.toCharArray();
		
		// store the index of blocks that are needed to move
		int[] moves;
		
		switch(op) {
		
		case 'A' : {
			moves = new int[] {0, 2, 6, 11, 15, 20, 22};
			break;
		}
		
		case 'B' : {
			moves = new int[] {1, 3, 8, 12, 17, 21, 23};
			break;
		}
		
		case 'C' : {
			moves = new int[] {10, 9, 8, 7, 6, 5, 4};
			break;
		}
		
		case 'D' : {
			moves = new int[] {19, 18, 17, 16, 15, 14, 13};
			break;
		}
		
		case 'E' : {
			moves = new int[] {23, 21, 17, 12, 8, 3, 1};
			break;
		}
		
		case 'F' : {
			moves = new int[] {22, 20, 15, 11, 6, 2, 0};
			break;
		}
		
		case 'G' : {
			moves = new int[] {13, 14, 15, 16, 17, 18, 19};
			break;
		}
		
		case 'H' : {
			moves = new int[] {4, 5, 6, 7, 8, 9, 10};
			break;
		}
		
		default : {
			moves = new int[7];
			break;
		}
		
		}
		
		// make the move for the corresponding operation
		char temp = blocks[moves[0]];
		for(int i = 1; i < moves.length; i++)
		{
			blocks[moves[i - 1]] = blocks[moves[i]];
		}
		
		blocks[moves[moves.length - 1]] = temp;
		
		return new String(blocks);
	}

	@Override
	public int getHeuristicCost(String config, int modeFlag) 
	{		
		if(modeFlag == 1)
		{
			// index of the blocks in the center square in char array
			int[] center = new int[] {6, 7, 8, 11, 12, 15, 16, 17};
			// store the number of blocks with each symbols in the center square
			int[] nCenter = new int[3];
			for(int i = 0; i < center.length; i++)
			{
				char c = config.charAt(center[i]);
				if(c == '1')
				{
					nCenter[0]++;
				}
				else if(c == '2')
				{
					nCenter[1]++;
				}
				else
				{
					nCenter[2]++;
				}
			}
			return 8 - Math.max(nCenter[0], Math.max(nCenter[1], nCenter[2]));
		}
		else if(modeFlag == 2)
		{
			return 0;
		}
		else
		{
			// index of the blocks in the outskirts in char array
			int[] outskirts = new int[] {0, 1, 2, 3, 4, 5, 9, 10,
										 13, 14, 18, 19, 20, 21, 22, 23};
			// store the number of blocks with each symbols in the outskirts
			int[] nOutskirts = new int[3];
			for(int i = 0; i < outskirts.length; i++)
			{
				char c = config.charAt(outskirts[i]);
				if(c == '1')
				{
					nOutskirts[0]++;
				}
				else if(c == '2')
				{
					nOutskirts[1]++;
				}
				else
				{
					nOutskirts[2]++;
				}
			}
			return Math.min(nOutskirts[0], Math.min(nOutskirts[1], nOutskirts[2]));
		}
	}
}
