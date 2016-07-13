/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */
import java.util.HashMap;

public class NaiveBayesClassifierImpl implements NaiveBayesClassifier 
{
	// for convenience of calculation, we define the types of some instance variables
	// as double
	
	// vocabulary size
	private double v;
	
	// store # of instance label. index 0 for SPAM, index 1 for HAM.
	private double[] instanceCount;
	
	// store # of word label. index 0 for SPAM, index 1 for HAM
	private double[] tokenCount;
	
	// store # of times each word appears in labeled SPAM messages
	private HashMap<String, Integer> mapSpam;
	
	// store # of times each word appears in labeled HAM messages
	private HashMap<String, Integer> mapHam;
	
	/**
	 * Trains the classifier with the provided training data and vocabulary size
	 */
	@Override
	public void train(Instance[] trainingData, int v) 
	{
		this.v = (double) v;
		this.instanceCount = new double[2];
		this.tokenCount = new double[2];
		this.mapSpam = new HashMap<String, Integer>();
		this.mapHam = new HashMap<String, Integer>();
		for(int i = 0; i < trainingData.length; i++)
		{
			Instance instance = trainingData[i];
			if(instance.label == Label.SPAM)
			{
				instanceCount[0] ++;
				
				for(int j = 0; j < instance.words.length; j++)
				{
					String key = instance.words[j];
					if(!mapSpam.containsKey(key))
					{
						mapSpam.put(key, 1);
					}
					else
					{
						mapSpam.put(key, mapSpam.get(key) + 1);
					}
				}
			}
			else if(instance.label == Label.HAM)
			{
				instanceCount[1] ++;
				
				for(int j = 0; j < instance.words.length; j++)
				{
					String key = instance.words[j];
					if(!mapHam.containsKey(key))
					{
						mapHam.put(key, 1);
					}
					else
					{
						mapHam.put(key, mapHam.get(key) + 1);
					}
				}
			}
		}
		for(Integer times : mapSpam.values())
		{
			tokenCount[0] += (double) times;
		}
		for(Integer times : mapHam.values())
		{
			tokenCount[1] += (double) times;
		}
	}

	/**
	 * Returns the prior probability of the label parameter, i.e. P(SPAM) or P(HAM)
	 */
	@Override
	public double p_l(Label label) 
	{
		double sum = instanceCount[0] + instanceCount[1];
		if(label == Label.SPAM)
		{
			return instanceCount[0] / sum;
		}
		else if(label == Label.HAM)
		{
			return instanceCount[1] / sum;
		}
		return 0;
	}

	/**
	 * Returns the smoothed conditional probability of the word given the label,
	 * i.e. P(word|SPAM) or P(word|HAM)
	 */
	@Override
	public double p_w_given_l(String word, Label label) 
	{
		if(label == Label.SPAM)
		{
			if(!mapSpam.containsKey(word))
			{
				return 0.00001 / (v * 0.00001 + tokenCount[0]);	
			}
			else
			{
				return ((double) mapSpam.get(word) + 0.00001) /
						(v * 0.00001 + tokenCount[0]);
			}
		}
		else if(label == Label.HAM)
		{
			if(!mapHam.containsKey(word))
			{
				return 0.00001 / (v * 0.00001 + tokenCount[1]);	
			}
			else
			{
				return ((double) mapHam.get(word) + 0.00001) /
						(v * 0.00001 + tokenCount[1]);
			}
		}
		return 0;
	}
	
	/**
	 * Classifies an array of words as either SPAM or HAM. 
	 */
	@Override
	public ClassifyResult classify(String[] words) 
	{
		ClassifyResult result = new ClassifyResult();
		result.log_prob_spam = Math.log(p_l(Label.SPAM));
		result.log_prob_ham = Math.log(p_l(Label.HAM));
		for(String s : words)
		{
			result.log_prob_spam += Math.log(p_w_given_l(s, Label.SPAM));
			result.log_prob_ham += Math.log(p_w_given_l(s, Label.HAM));
		}
		// ties appears, HAM is preferred
		if(result.log_prob_spam > result.log_prob_ham)
		{
			result.label = Label.SPAM;
		}
		else
		{
			result.label = Label.HAM;
		}
		return result;
	}
}
