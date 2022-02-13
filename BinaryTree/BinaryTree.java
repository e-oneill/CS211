package BinaryTree;

import java.util.Scanner;

public class BinaryTree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		
		SortedBinaryTree tree = new SortedBinaryTree(new Node(26));
		
		tree.insert(58);
		tree.insert(84);
		tree.insert(81);
		tree.insert(94);
		tree.insert(69);
		tree.insert(15);
		tree.insert(29);
		tree.insert(39);
		tree.insert(52);
		tree.insert(33);
		tree.insert(85);
		

		tree.preOrderTraverse(tree.root);
		System.out.println();
		tree.delete(26);
		tree.preOrderTraverse(tree.root);
	}

}

class SortedBinaryTree {
	Node root;
	
	public SortedBinaryTree()
	{
		root = new Node();
	}
	
	public SortedBinaryTree(Node root) {
		this.root = root;
	}
	
	boolean insertNode(Node newNode) {
		Node current = root;
		while (current.data != newNode.data)
		{
			
		}
		
		return false;
	}
	
	boolean insert(int num) { // returns true if the insert is successful
		Node current = root;
		if (current == null)
		{
			root = new Node(num);
			return true;
		}
		while (current.data != num)
		{
			if (num > current.data)
			{
				if (current.right != null)
				{
					current = current.right;
				}
				else
				{
					current.right = new Node(num);
					return true;
				}
				
			}
			else
			{	
				if (current.left != null)
				{
					current = current.left;
				}
				else
				{
					current.left = new Node(num);
					return true;
				}
			}
			
		}
		
		return false;
	}
	
	Node find(int num) 
	{
		Node current = root;
		while (current != null && current.data != num)
		{
			if (current.data > num)
			{
				current = current.left;
			}
			else
			{
				current = current.right;
			}
		}
		return current;
	}

	
	void inOrderTraverse(Node localNode) //prints out the left tree and then the right tree, root in middle
	{
		if (localNode != null)
		{
			inOrderTraverse(localNode.left);
			System.out.print(localNode.data + " ");
			inOrderTraverse(localNode.right);
			
		}
	}
	
	void preOrderTraverse(Node localNode) //prints the binary tree in the order it would ahve been inserted
	{
		if (localNode != null)
		{
			System.out.print(localNode.data + " ");
			preOrderTraverse(localNode.left);
			preOrderTraverse(localNode.right);
		}
	}
	
	void postOrderTraverse(Node localNode) //returns the tree from bottom up left to right
	{
		if (localNode != null)
		{
			
			postOrderTraverse(localNode.left);
			postOrderTraverse(localNode.right);
			System.out.print(localNode.data + " ");
		}
	}
	
	public boolean delete(int num) {
		Node current = root;
		Node parent = root;
		boolean isLeftChild = true;
		while (current.data != num)
		{
			parent = current;
			if (num < current.data)
			{
				isLeftChild = true;
				current = current.left;
			}
			else
			{
				isLeftChild = false;
				current = current.right;
			}
			if (current == null)
			{
				return false;
			}
		}
		
		if (current.left == null && current.right == null) //deleted node has no children - leaf node
		{
			if (current == root)
			{
				root = null;
			}
			else if (isLeftChild)
			{
				parent.left = null;
				
			}
			else
			{
				parent.right = null;
			}
			return true;
		}
		else if (current.left == null) // deleted node has no left child
		{
			if (current == root)
			{
				root = current.right;
			}
			else if (isLeftChild)
			{
				parent.left = current.right;
				
			}
			else current.right = current.right;	
		}
		else if (current.right == null) // deleted node has no right child
		{
			if (current == root)
			{
				root = current.left;
			}
			else if (isLeftChild)
			{
				parent.left = current.left;
			}
			else parent.right = current.left;
		}
		else //deleted node has two children
		{
			Node successor = getSuccessor(current);
			if ( current == root)
			{
				root = successor;
			}
			else if (isLeftChild)
			{
				parent.left = successor;
			}
			else parent.right = successor;
			
			successor.left = current.left;
		}
		
		return true;
	}
	
	private Node getSuccessor(Node delNode)
	{
		Node successor = delNode.right;
		Node parent = delNode;
		while (successor.left != null)
		{
			parent = successor;
			successor = successor.left;
		}
		if (successor != delNode.right)
		{
			parent.left = successor.right;
			successor.right = delNode.right;
		}
		
		return successor;
	}
	
	public Node minimum() {
		Node current, last = null;
		current = root;
		if (current == null)
		{
			return null;
		}
		while (current != null)
		{
			last = current;
			current = current.left;
		}
		if (last != null)
		{
			return last;
		}
		else
		{
			return null;
		}
		
	}
}

class Node {
	int data;
	Node left;
	Node right;
	
	public Node() {
		
	}
	
	public Node(int data) {
		this.data = data;
		
	}
	
	public Node(int data, Node left, Node right) {
		this.data = data;
		this.left = left;
		this.right = right;
	}
}
