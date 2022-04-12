package lab9;

public class CheckPaper {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int x = ((23&15)>>1)|(34|19);
		System.out.println(x);
		
		int y = ((3|56)<<2)&(54|6<<2);
//		System.out.println(59<<2);
		System.out.println(y);
		
		int z = (18&20)|(4|10)<<2;
		System.out.println(z);
		
		int q = ((13&-1)<<2)|(18|18);
		System.out.println(q);
	}
}
