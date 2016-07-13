import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Fill in the implementation details of the class DecisionTree using this file.
 * Any methods or secondary classes that you want are fine but we will only
 * interact with those methods in the DecisionTree framework.
 * 
 * You must add code for the 5 methods specified below.
 * 
 * See DecisionTree for a description of default methods.
 */
public class DecisionTreeImpl extends DecisionTree {
	private DecTreeNode root;
	private List<String> labels; // ordered list of class labels
	private List<String> attributes; // ordered list of attributes
	private Map<String, List<String>> attributeValues; // map to ordered
														// discrete values taken
														// by attributes

	/**
	 * Answers static questions about decision trees.
	 */
	DecisionTreeImpl() {
		// no code necessary
		// this is void purposefully
	}

	/**
	 * Build a decision tree given only a training set.
	 * 
	 * @param train: the training set
	 */
	DecisionTreeImpl(DataSet train) 
	{
		this.labels = train.labels;
		this.attributes = train.attributes;
		this.attributeValues = train.attributeValues;
		this.root = buildtree(train.instances, this.attributes, -1, 
				majorityVote(train.instances));
		
	}
	
	/**
	 * Helper method to build a tree and return a root node.
	 * @param instances: training examples
	 * @param attributes: attributes can be used
	 * @param parentAttributeValue: index of position in the parent's children list
	 * @param d: default label, based on parent's majority vote
	 */
	private DecTreeNode buildtree(List<Instance> instances, List<String> attributes, 
			int parentAttributeValue, int d)
	{
		int[] occurrences = new int[labels.size()];
		int majorityVote = 0;
		if (instances.isEmpty())
		{
			return new DecTreeNode(d, null, parentAttributeValue, true); // base case
		}	
		for (Instance instance : instances)
		{
			occurrences[instance.label] ++;
		}
		for (int i = 0; i < occurrences.length; i++)
		{
			if (occurrences[i] == instances.size()) //examples have the same label
			{
				return new DecTreeNode(i, null, parentAttributeValue, true); // base case
			}
			if (occurrences[i] > occurrences[majorityVote])
			{
				majorityVote = i;
			}
		}
		if (attributes.isEmpty())
		{
			return new DecTreeNode(majorityVote, null, parentAttributeValue, true);
		}
		
		String bestAttribute = findBestAttribute(instances, attributes);
		// index of bestAttribute in the list.attributes
		int q = this.attributes.indexOf(bestAttribute);
		// form a subAttributes list without bestAttribute
		List<String> subAttributes = new ArrayList<String>();
		for (String s : attributes)
		{
			if (s != bestAttribute)
			{
				subAttributes.add(s);
			}
		}
		// Create internal node. Store majorityVote in the label parameter, which
		// is convenient when doing the pruning implementation
		DecTreeNode root = new DecTreeNode(majorityVote, q, parentAttributeValue, false);
		for (int i = 0; i < this.attributeValues.get(this.attributes.get(q)).size();
				i++)
		{
			// use majorityVote as argument of default parameter
			root.addChild(buildtree(subInstances(q, i, instances), subAttributes, i, majorityVote));
		}
		return root;
	}
	
	/**
	 * Return a list of instances satisfying the condition that its qth feature equals
	 * ith attribute
	 * @param q: index of qth feature of instance which to be checked
	 * @param i: index of one of the possible answer of qth feature
	 * @param instances: original instances
	 */
	private List<Instance> subInstances (int q, int i, List<Instance> instances)
	{
		List<Instance> subList = new ArrayList<Instance>();
		for (Instance instance : instances)
		{
			if (instance.attributes.get(q) == i)
			{
				subList.add(instance);
			}
		}
		return subList;
	}

	/**
	 * Build a decision tree given a training set then prune it using a tuning
	 * set.
	 * 
	 * @param train: the training set
	 * @param tune: the tuning set
	 */
	DecisionTreeImpl(DataSet train, DataSet tune) 
	{
		this.labels = train.labels;
		this.attributes = train.attributes;
		this.attributeValues = train.attributeValues;
		// build the tree using training set
		this.root = buildtree(train.instances, this.attributes, -1, 
				majorityVote(train.instances));
		// loop continue when there is more than one node in the tree &&
		// the accuracy of the best pruned node is no worse than the original
		while(!root.terminal)
		{
			DecTreeNode newRoot = new DecTreeNode(null, null, null, false);
			// copy the original tree, new root node is newRoot
			copyTree(this.root, newRoot);
			// use the new tree to find the best pruned tree
			DecTreeNode bestRoot = prune(newRoot, tune);
			if (calculateAccuracy(this.root, tune) > calculateAccuracy(bestRoot, tune))
			{
				break;
			}
			this.root = bestRoot;
		}
	}
	
	/**
	 * Return a pruned tree root node with the highest accuracy
	 * 
	 */
	private DecTreeNode prune(DecTreeNode root, DataSet tune)
	{
		Queue<DecTreeNode> queue = new LinkedList<DecTreeNode>();
		double maxAccuracy = -1;
		DecTreeNode bestPruneNode = root;
		
		// use a queue to do the BFS traverse the tree
		queue.add(root);
		while(!queue.isEmpty())
		{
			DecTreeNode currNode = queue.remove(); 
			// if currNode is internal node, prune it and calculate the accuracy
			if (!currNode.terminal)
			{
				currNode.terminal = true;
				double temp = calculateAccuracy(root, tune);
				// to avoid different trees by different choice between several 
				// tied best pruned nodes, please always choose the first node in 
				// BFS traverse order, or namely the node with least depth and the 
				// leftmost position
				if (maxAccuracy < temp)
				{
					maxAccuracy = temp;
					bestPruneNode = currNode;
				}
				currNode.terminal = false;
			}
			for(DecTreeNode child : currNode.children)
			{
				queue.add(child);
			}
		}
		bestPruneNode.terminal = true;
		return root;
	}
	
	/**
	 * copy tree
	 */
	private void copyTree(DecTreeNode oldNode, DecTreeNode newNode)
	{
		if (!oldNode.terminal)
		{
			newNode.label = oldNode.label;
			newNode.attribute = oldNode.attribute;
			newNode.parentAttributeValue = oldNode.parentAttributeValue;
			newNode.terminal = oldNode.terminal;
			for (DecTreeNode oldChild : oldNode.children)
			{
				DecTreeNode newChild = new DecTreeNode(null, null, null, false);
				newNode.addChild(newChild);
				copyTree(oldChild, newChild);
			}
		}
		else 
		{
			newNode.label = oldNode.label;
			newNode.attribute = oldNode.attribute;
			newNode.parentAttributeValue = oldNode.parentAttributeValue;
			newNode.terminal = oldNode.terminal;
		}
	}
	
	/**
	 * Calculate the accuracy of a tree using tune set
	 * 
	 */
	private double calculateAccuracy(DecTreeNode root, DataSet tune)
	{
		double correct = 0;
		double total = 0;
		double accuracy = 0; 
		for (Instance instance : tune.instances)
		{
			DecTreeNode node = root;
			while (!node.terminal)
			{
				int answer = instance.attributes.get(node.attribute);
				node = node.children.get(answer);
			}
			if (instance.label.equals(node.label))
			{
				correct++;
			}
			total++;
		}
		accuracy = correct / total;
		return accuracy;
	}
	
	@Override
	public String classify(Instance instance) 
	{
		DecTreeNode node = this.root;
		while (!node.terminal)
		{
			int answer = instance.attributes.get(node.attribute);
			node = node.children.get(answer);
		}
		return this.labels.get(node.label);
	}

	@Override
	/**
	 * Print the decision tree in the specified format
	 */
	public void print() {

		printTreeNode(root, null, 0);
	}
	
	/**
	 * Prints the subtree of the node
	 * with each line prefixed by 4 * k spaces.
	 */
	public void printTreeNode(DecTreeNode p, DecTreeNode parent, int k) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < k; i++) {
			sb.append("    ");
		}
		String value;
		if (parent == null) {
			value = "ROOT";
		} else{
			String parentAttribute = attributes.get(parent.attribute);
			value = attributeValues.get(parentAttribute).get(p.parentAttributeValue);
		}
		sb.append(value);
		if (p.terminal) {
			sb.append(" (" + labels.get(p.label) + ")");
			System.out.println(sb.toString());
		} else {
			sb.append(" {" + attributes.get(p.attribute) + "?}");
			System.out.println(sb.toString());
			for(DecTreeNode child: p.children) {
				printTreeNode(child, p, k+1);
			}
		}
	}

	@Override
	public void rootInfoGain(DataSet train) 
	{	
		this.labels = train.labels;
		this.attributes = train.attributes;
		this.attributeValues = train.attributeValues;
		for(int i = 0; i < this.attributes.size(); i++)
		{
			double rootEntropy;
			String attribute = attributes.get(i);
			int[] occurrences = new int[labels.size()];
			for (Instance instance : train.instances)
			{
				occurrences[instance.label] ++;
			}
			rootEntropy = calculateEntropy(occurrences);
			
			System.out.print(attribute);
			System.out.format(" %.5f\n", calculateGain(attribute, train.instances, rootEntropy));
		}
	}
	
	/**
	 * Return the index of label, which most instances have
	 * 
	 */
	private int majorityVote(List<Instance> instances)
	{
		int[] occurrences = new int[labels.size()];
		int majorityVote = 0;
		for (Instance instance : instances)
		{
			occurrences[instance.label] ++;
		}
		for (int i = 0; i < occurrences.length; i++)
		{
			// always choose small index when ties
			if (occurrences[i] > occurrences[majorityVote])
			{
				majorityVote = i;
			}
		}
		return majorityVote;
	}
	
	/**
	 * Find the best attribute in the set of attributes
	 * 
	 */
	private String findBestAttribute(List<Instance> instances, List<String> attributes)
	{
		double bestGain = -10;
		String bestAttribute = null;
		double rootEntropy;
		int[] occurrences = new int[labels.size()];
		for (Instance instance : instances)
		{
			occurrences[instance.label] ++;
		}
		rootEntropy = calculateEntropy(occurrences);
		for (String s : attributes)
		{
			double gain = calculateGain(s, instances, rootEntropy);
			// always choose the attribute with smaller index when ties
			if (gain > bestGain)
			{
				bestGain = gain;
				bestAttribute = s;
			}
		}
		return bestAttribute;
	}
	
	/**
	 * Calculate gain
	 */
	private double calculateGain(String s, List<Instance> instances, 
			double rootEntropy)
	{	
		double condEntropy = 0;
		for (int i = 0; i < this.attributeValues.get(s).size();
				i++)
		{
			int[] occurrences = new int[labels.size()];
			double count = 0;
			for (Instance instance : instances)
			{
				if (instance.attributes.get(this.attributes.indexOf(s)) == i)
				{
					occurrences[instance.label] ++;
					count ++;
				}
			}
			condEntropy += count / instances.size() * calculateEntropy(occurrences);
		}
		return rootEntropy - condEntropy;
	}
	
	/**
	 * Calculate entropy
	 * 
	 */
	private double calculateEntropy(int[] occurrences)
	{
		double total = 0;
		for (int i = 0; i < occurrences.length; i++)
		{
			total += (double) occurrences[i];
		}
		if (total == 0)
		{
			return 0;
		}
		double entropy = 0;
		for (int i = 0; i < occurrences.length; i++)
		{
			if (occurrences[i] > 0)
			{
				entropy += (-1) * (occurrences[i] / total) * 
						(Math.log(occurrences[i] / total) / Math.log(2));
			}
		}
		return entropy;
	}
}

