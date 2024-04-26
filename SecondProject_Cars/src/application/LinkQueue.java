package application;

public class LinkQueue {

	private int length;
	private Node front, rear;

	public int getLength() {
		return length;
	}

	public Node getFront() {
		return front;
	}

	public Node getRear() {
		return rear;
	}

	public LinkQueue() {
		length = 0;
		front = rear = null;
	}

	// Adds the specified data to the rear of the queue.
	public void enQueue(Orders front2) {
		Node node = new Node(front2);
		if (isEmpty())
			front = node;
		else
			rear.next = node;
		rear = node;
		length++;
	}

	public Orders deQueue() {
		if (isEmpty())
			return null;
		Orders result = (Orders) front.element;
		front = front.next;
		length--;
		if (isEmpty())
			rear = null;
		return result;
	}

	public boolean isEmpty() {
		return (length == 0);
	}

	public Orders first() {
		if (isEmpty())
			return null;
		return (Orders) front.element;
	}

	public int size() {
		return length;
	}

	public StringBuilder printQueue() {
		Orders front = null;
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < size(); i++) {
			front = first();
			deQueue();
			s.append(front.getCustomerName() + ", 0" + front.getCustomerMobile() + ", " + ", "
					+ front.getOrderdCar().toString() + ", " + front.getOrderDate() + ", " + front.getOrderStatus()
					+ "\n");
			enQueue(front);
		}
		return s;
	}

}
