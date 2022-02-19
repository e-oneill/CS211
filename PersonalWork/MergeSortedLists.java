package PersonalWork;

public class MergeSortedLists {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] list1num = {-10, -9, -7, -7, -1, 2, 3, 3, 4};
		int[] list2num = {-10, -7, -7, -1, 2, 3, 6};
		ListNode[] list1 = new ListNode[list1num.length];
		ListNode[] list2 = new ListNode[list2num.length];
		for (int i=0; i<list1.length; i++)
		{
			list1[i] = new ListNode(list1num[i]);
			if (i > 0)
			{
				list1[i-1].next = list1[i];
			}
		}
		
		for (int i=0; i<list2.length; i++)
		{
			list2[i] = new ListNode(list2num[i]);
			if (i > 0)
			{
				list2[i-1].next = list2[i];
			}
		} 
		

		

		
		ListNode newHead = mergeTwoLists(list1[0], list2[0]);
		while (newHead != null)
		{
			if (newHead.next != null)
				System.out.print(newHead.val + ", ");
			else
				System.out.print(newHead.val);
			newHead = newHead.next;
		}
		
	}
	
	public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
		ListNode dummy = new ListNode(0); //dummy node = will return next node after this
		ListNode tail = dummy; //tail is used to track the current end of the merged list
		while (true)
		{
			if (list1 == null) //if list1 is null return the other
			{
				tail.next = list2;
				break;
			}
			if (list2 == null) //vice versa
			{
				tail.next = list1;
				break;
			}
			
			if (list1.val <= list2.val) //if list1 is less than or equal to list2, the tail's next is set to list1;
			{
				tail.next = list1;
				list1 = list1.next;
			}
			else //otherwise the tail is set to list2 and list2 is incremented
			{
				tail.next = list2;
				list2 = list2.next;
			}
			tail=tail.next;
		}
		return dummy.next;
    }

}


  class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }
 