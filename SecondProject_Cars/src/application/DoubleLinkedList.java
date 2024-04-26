//Name:sama wahidee
//Id:1211503
//Section:1
package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoubleLinkedList {
	private DoubleNode first;
	private DoubleNode last;
	int count;

	public DoubleLinkedList() {
	}

	// function to return the first node of the linked list
	public DoubleNode getfirst() {
		if (count != 0) {
			return first;
		}
		return null;

	}

	// function to return the last node of the linked list
	public DoubleNode getLast() {
		if (count != 0) {
			return last;
		}
		return null;

	}

	// function that add double node into the head of the double linked list
	public void addFirst(String s, LinkedList linkedList) {
		if (count == 0) {
			first = last = new DoubleNode(s, linkedList);
		} else {
			DoubleNode temp = new DoubleNode(s, linkedList);
			first.previous = temp;
			temp.next = first;
			first = temp;
		}
		count++;
	}

	// function that add double node into the tail of the double linked list
	public void addLast(String s, LinkedList linkedList) {
		if (count == 0) {
			first = last = new DoubleNode(s, linkedList);
		} else {
			DoubleNode temp = new DoubleNode(s, linkedList);

			last.next = temp;
			last = temp;
			last.previous = temp;
		}
		count++;
	}

	// function that add doublenode after a specific index in the double linked list
	public void add(String s, int index, LinkedList linkedList) {
		if (index == 0) {
			addFirst(s, linkedList);
		} else {
			if (index >= count) {
				addLast(s, linkedList);
			} else {
				DoubleNode temp = new DoubleNode(s, linkedList);
				DoubleNode current = first;
				for (int i = 0; i < index - 1; i++) {
					current = current.next;
				}
				temp.next = current.next;
				temp.previous = current;
				current.next = temp;
				temp.next.previous = temp;
				count++;
			}
		}
	}

	// function that removes the first doublenode of the linked list
	public boolean removeFirst() {
		if (count == 0) {
			return false;
		} else {
			if (count == 1) {
				first = last = null;
			} else {
				first = first.next;
				first.previous = null;
			}
			count--;
			return true;

		}

	}

	// function that removes the last doublenode of the linked list
	public boolean removeLast() {
		if (count == 0) {
			return false;
		} else {
			if (count == 1) {
				first = last = null;
			} else {
				DoubleNode current = first;
				for (int i = 1; i <= count - 2; i++) {
					current = current.next;

				}
				last = current;
				current.next = null;
			}
			count--;
			return true;

		}

	}

	// function that removes a specific doublenode in the linked list due to it's
	// object
	public boolean remove(String o) {
		if (count == 0) {
			return false;
		} else {
			if (first.Brand.compareTo(o)==0) {
				return removeFirst();
			} else {
				DoubleNode current = first;
				for (int i = 1; i < count; i++) {
					current = current.next;
					if (current.Brand.equals(o)) {
						current.previous.next = current.next;
						current.next.previous = current.previous;
						count--;
						return true;
					}
				}
				return false;
			}
		}
	}

	// function that searches for a specific doublenode due to it's location
	public boolean search(String l) {
		if (count == 0) {
			return false;
		} else {
			DoubleNode current = first;
			while ((current != null) && (!(current.getBrand().equals(l)))) {
				current = current.next;
			}
			if (current != null) {
				return true;
			}

		}
		return false;

	}

	// function that returns a specific doublenode due to it's martyr object
	public DoubleNode getNode(Object o) {
		if (count == 0) {
			return null;
		} else {
			DoubleNode current = first;
			while ((current != null) && (!(current.Brand.equals(o)))) {
				current = current.next;
			}
			if (current != null) {
				return current;
			}

		}
		return null;

	}

	// function to update the location
	public void update(DoubleNode old, String neww) {
		old.setBrand(neww);
	}

	// function to print the double linkedlist
	public void PrintList() {
		if (count > 0) {
			DoubleNode current = first;
			while (current != null) {
				System.out.println(current.Brand + " " + ((Cars) current.linkedList.getfirst().element).getModel());
				current = current.next;
			}

		} else {
			System.out.println("The list is empty!!");
		}
	}

	// function that sort the double linhedlist
	public void sort() {
		List<String> tempList = new ArrayList<>();

		DoubleNode current = first;
		while (current != null) {
			tempList.add(current.Brand);
			current = current.next;
		}

		Collections.sort(tempList);

		current = first;
		for (int i = 0; i < tempList.size(); i++) {
			current.Brand = tempList.get(i);
			current = current.next;
		}
	}

	public void singleSort() {
		DoubleNode current = first;
		while (current != null) {
			current.linkedList.sort();
			current = current.next;
		}
	}
	public StringBuilder print() {
		StringBuilder s= new StringBuilder();
		DoubleNode current = first;
		while (current != null) {
			Node n= current.linkedList.getfirst();
			while (n!=null) {
				s.append(((Cars)n.element).toString()+"\n");
				n=n.next;
			}
			
			current=current.next;
		}
		return s;
	}
}
