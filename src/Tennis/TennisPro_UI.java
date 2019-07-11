package Tennis;

import java.io.IOException;
import java.util.StringTokenizer;

import com.github.lalyos.jfiglet.FigletFont;


class TennisPro_UI {
	static StringTokenizer st;
	static int i = 0;
	public static void main(String[] args) throws IOException {
		getContestName();
		getScoreSort();
		
		 
		String name1 = FigletFont.convertOneLine("Lee");
		String name2 = FigletFont.convertOneLine("Kim");
		
		st = new StringTokenizer(name1,"\n");
		char [][] name1s = new char[st.countTokens()][];
		i=0;
		while (st.hasMoreTokens()) {
			name1s[i++] = st.nextToken().toCharArray();
		}
		
		for (int i = 0; i < name1s.length; i++) {
			for (int j = 0; j < name1s[i].length; j++) {
				System.out.printf("%c",name1s[i][j]);
			}
			System.out.println();
		}
		
		st = new StringTokenizer(name2,"\n");
		char [][] name2s = new char[st.countTokens()][];
		i=0;
		while (st.hasMoreTokens()) {
			name2s[i++] = st.nextToken().toCharArray();
		}
		
		for (int i = 0; i < name2s.length; i++) {
			for (int j = 0; j < name2s[i].length; j++) {
				System.out.printf("%c",name2s[i][j]);
			}
			System.out.println();
		}
		
	}

	private static void getScoreSort() throws IOException {
		String previous_sets = FigletFont.convertOneLine("name     sets     game     point"); 
		 st = new StringTokenizer(previous_sets,"\n");
		 char [][] convertSets = new char[st.countTokens()][];
		 i=0;
		 while (st.hasMoreTokens()) {
			convertSets[i++] = st.nextToken().toCharArray();
		}
		 
		 for (int j = 0; j < convertSets.length; j++) {
			for (int j2 = 0; j2 < convertSets[j].length; j2++) {
				System.out.printf("%c",convertSets[j][j2]);
			}
			System.out.println();
		}
		 System.out.println();
	}

	private static void getContestName() throws IOException {
		String msg = FigletFont.convertOneLine("US OPEN TENNIS 2019");
		 st = new StringTokenizer(msg, "\n");
		 char [][] convertMessage = new char[st.countTokens()][];
		 while (st.hasMoreTokens()) {
			  convertMessage[i++] = st.nextToken().toCharArray();
		}
		 
		 
		 for (int j = 0; j < convertMessage.length; j++) {
//			 System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
			for (int j2 = 0; j2 < convertMessage[j].length; j2++) {
				System.out.printf("%c", convertMessage[j][j2]);
			}
			System.out.println();
		}
	}
	
}
