import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;

public class Main {

	static State target = null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		int n= sc.nextInt();
		while (n>0) {
			doTestcase(n, sc);
		}
	}
	
	static void doTestcase(int n, Scanner sc) {
		target = new State3(n);
		for (int i=0;i<n;i++) {
			target.push(i,i+1);
		}
		
		State state = new State3(n);
		for (int i=0;i<n;i++) {
			state.push(i,sc.nextInt());
		}
		
		ArrayList<State> current = new ArrayList<>();
		current.add(state);
		ArrayList<State> next = new ArrayList<>();
		HashSet<State> check = new HashSet<>();
		int step=0;
		while (!current.isEmpty()) {
			
			for (State st:current) {
				check.add(state);
				ArrayList<State> neighbours = new ArrayList<>();
				boolean found = getNeighbours(st,neighbours, check);
				if (found) {
					System.out.println(step+1);
					return;
				}
				next.addAll(neighbours);
			}
			step++;
			current = next;
			next = new ArrayList<>();
		}
	
		System.out.println("IMPOSSIBLE");
		
	}

	private static boolean getNeighbours(State st, ArrayList<State> neighbours, HashSet<State> check) {
		// TODO Auto-generated method stub
		
		for (int i=0;i<st.size();i++) {
			for (int j=0;j<st.size();j++) {
				if (i==j) continue;
				int a = st.peek(i);
				if (a<0) continue;
				int b = st.peek(j);
				if (b<0) continue;
				if (a>b) continue;
				
				State neighbour = move(st,i,j);
				if (neighbour.equals(target))
					return true;
				if (check.contains(neighbour)) {
					continue;
				}
				neighbours.add(neighbour);
				
			}
			
		}
		return false;
	}

	private static State move(State st, int i, int j) {
		// TODO Auto-generated method stub
		State stRet =st.clone();
		int a = st.pop(i);
		stRet.push(j,a);
		return stRet;
	}
}

interface State {
	int peek(int pos);
	void push(int pos, int d);
	int pop(int pos);
	State clone();
	int size();
	
}


class State2 implements State  {
	long[] states = null;
	public State2(int n) {
		states=new long[n];
	}
	public int peek(int pos) {
		// TODO Auto-generated method stub
		long st = states[pos];
		int i=findEmptyPos(st);
		if (i==0) return -1;
		long mask = getMask(i-1);
		long rt = st & mask;
		rt >>= (i-1)*8;
		return (int)rt;
	}
	public void push(int pos, int d) {
		// TODO Auto-generated method stub
		long st = states[pos];
		int i=findEmptyPos(st);
		d <<= i*8;
		st |= d;
		states[i] = st;
	}
	
	private int findEmptyPos(long st) {
		// TODO Auto-generated method stub
		for (int i=0;i<8;i++) {
			long mask = getMask(i);
			if ((st & mask) == 0)
				return i;
		}
		return 8;
	}
	
	
	private long getMask(int i) {
		// TODO Auto-generated method stub
		long mask = 127;
		mask <<= i*8;
		return mask;
	}
	public int pop(int pos) {
		long st = states[pos];
		int i=findEmptyPos(st);
		if (i==0) return -1;
		long mask = getMask(i-1);
		long rt = st & mask;
		rt >>= (i-1)*8;
		st &= ~mask;
		states[pos] = st;
		return (int)rt;
	}
	
	
	@Override
	public State clone() {
		// TODO Auto-generated method stub
		State2 obj = new State2(this.states.length);
		for (int i=0;i<this.states.length;i++) {
			obj.states[i] = this.states[i];
		}
		return obj;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(states);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State2 other = (State2) obj;
		if (!Arrays.equals(states, other.states))
			return false;
		return true;
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.states.length;
	}
	
	
}

class State1 {
	Stack<Integer>[] states = null;
	public State1(int n) {
		this.states = new Stack[n];
	}
	public void push(int i, int d) {
		// TODO Auto-generated method stub
		this.states[i].push(d);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(states);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State1 other = (State1) obj;
		if (!Arrays.equals(states, other.states))
			return false;
		return true;
	}
}

class State3 implements State {
	int[][] states = null;
	int[] tops = null;
	
	public State3(int n) {
		states = new int[n][n];
		tops = new int[n];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(states);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State3 other = (State3) obj;
		if (!Arrays.deepEquals(states, other.states))
			return false;
		return true;
	}

	@Override
	public int peek(int pos) {
		// TODO Auto-generated method stub
		if (tops[pos]==0) return -1;
		return this.states[pos][tops[pos]-1];
	}

	@Override
	public void push(int pos, int d) {
		// TODO Auto-generated method stub
		this.states[pos][tops[pos]] = d;
		tops[pos]++;
	}

	@Override
	public int pop(int pos) {
		// TODO Auto-generated method stub
		if (tops[pos]==0) return -1;
		int d = this.states[pos][tops[pos]-1];
		this.states[pos][tops[pos]-1]=0;
		tops[pos] --;
		return d;
	}

	@Override
	public State clone() {
		// TODO Auto-generated method stub
		State3 state = new State3(this.size());
		for (int i=0;i<this.size();i++) {
			for (int j=0;j<this.size();j++) {
				state.states[i][j] = this.states[i][j];
			}
			state.tops[i] = this.tops[i];
		}
		return state;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.states.length;
	}
	
	
}


