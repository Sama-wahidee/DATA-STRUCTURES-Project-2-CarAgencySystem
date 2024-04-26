package application;

public class LinkStack {

	LinkedList list = new LinkedList();
	Node top;

	public LinkStack() {
	}

	public Node getTop() {
		return list.getfirst();
	}

	public void push(Object x) {
		list.addFirst(x);
	}

	public Node pop() {
		Node poped = list.getfirst();
		list.removeFirst();
		return poped;
	}

	public boolean isEmpty() {
		if (list.getfirst() == null) {
			return true;
		}
		return false;
	}

	public int size() {
		return list.getCount();
	}

	public StringBuilder PrintStack() {
		StringBuilder s = new StringBuilder();
		if (isEmpty())
			return s;
		LinkStack temp = new LinkStack();
		while (!isEmpty()) {
			Orders x=null;
			if (getTop().element instanceof Object) {
				 x = (Orders) getTop().element;
			}
			temp.push(this.pop());
			s.append(x.getCustomerName() + ", 0" + x.getCustomerMobile() + ", " + ", " + x.getOrderdCar().toString()
					+ ", " + x.getOrderDate() + ", " + x.getOrderStatus() + "\n");
		}
		while (!temp.isEmpty()) {
			this.push(temp.pop());
		}
		return s;
	}
	public StringBuilder PrintStack10() {
		int count=1;
		StringBuilder s = new StringBuilder();
		if (isEmpty())
			return s;
		LinkStack temp = new LinkStack();
		while (!isEmpty()&&count<=10) {
			Orders x=null;
			if (getTop().element instanceof Object) {
				 x = (Orders) getTop().element;
			}
			temp.push(this.pop());
			s.append(x.getCustomerName() + ", 0" + x.getCustomerMobile() + ", " + ", " + x.getOrderdCar().toString()
					+ ", " + x.getOrderDate() +"\n");
			count++;
		}
		while (!temp.isEmpty()) {
			this.push(temp.pop());
		}
		return s;
	}
}
