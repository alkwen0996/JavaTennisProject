package Tennis;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.github.lalyos.jfiglet.FigletFont;


class GameSetting {
	static Player getter_set;
	static TennisUI tu = new TennisUI();
	public static void main(String agrs[]) throws IOException {
		tu.getContestName();
		inputData();
		getContinue();
	}
	
	static void getContinue() throws IOException {	
		while(true) {
			System.out.println();
			System.out.print("계속 하시겠습니까 ?(Y, N) ");
			Scanner sc = new Scanner(System.in);
			String replay = sc.nextLine();
			String check = "y";

			if(replay.toUpperCase().equals(check.toUpperCase())) {
				
				/*tu.getContestName();*/
				inputData();
			}
			else {
				System.out.println("게임이 종료되었습니다.");
				break;
			}
		}
	}
	
	static void inputData() throws IOException { // 성별 게임타입,이름입력받긔

		String name1= null;
		String name2= null;
		String number = null; // number가 게임타입
		String gender= null;

		number = getNumber(gender); // 게임타입 단/복식입력받긔
		gender = getGender(number); // 성별입력받기

		// GameType 클래스 객체 생성
		GameType game_type= new GameType(number, gender); //오류

		//2. 이름 입력 받기
		name1 =getName(number);
		name2 =getName(number);

		// Player 클래스 객체 생성

		Player p1= new Player(game_type.setNum, name1); 
		Player p2= new Player(game_type.setNum, name2);

		//3. 서브권 결정
		//getter=p1; // 임의로 (do-while과 차이?)
		//getter_game=p1;
		getter_set=p1;
		boolean duceFlag = false;
		
		Score sc = new Score(); 
		sc.getSet(p1,p2,duceFlag,getter_set,game_type); // 점수계산메소드 소환

	}

	static String getGender(String gender) {
		Scanner sc=new Scanner(System.in);
		do {
			System.out.print("성별 선택? [ man / woman / mixdouble ]");
			gender = sc.nextLine();
		} while(!gender.equals("man")&&!gender.equals("woman")&&!gender.equals("mixdouble"));
		return gender;
	}

	static String getNumber(String number) {
		Scanner sc=new Scanner(System.in);
		do {
			System.out.print("인원수 선택? [ single / couple ]");
			number = sc.nextLine();
		} while(!number.equals("single")&&!number.equals("couple"));
		return number;
	}
	static String getName(String number) {
		String name;
		String regex = "^[a-zA-Z]{3}$";
		Scanner sc=new Scanner(System.in);
		do{
			if (number.equals("single"))
			System.out.print("Player Name?(ex:Lee)");
			else
				System.out.print("Team Name?(ex:AAA)");
		name=sc.nextLine();
	}while(!name.matches(regex));
		return name;
	}
}

class TennisUI {
	static StringTokenizer st;

	static void getContestName() throws IOException {
		String msg = FigletFont.convertOneLine("US OPEN TENNIS 2019");
		 st = new StringTokenizer(msg, "\n");
		 char [][] convertMessage = new char[st.countTokens()][];
		 int i=0;
		 while (st.hasMoreTokens()) {
			  convertMessage[i++] = st.nextToken().toCharArray();
		}
		 
		 for (int j = 0; j < convertMessage.length; j++) {
			for (int j2 = 0; j2 < convertMessage[j].length; j2++) {
				System.out.printf("%c", convertMessage[j][j2]);
			}
			System.out.println();
		}
	}
}

class Write{
	String fileName;//=System.getProperty("user.dir");
	String record;
	String arg0;
	FileWriter fw;
	
	
	public Write() throws IOException {
		super();
		this.fileName = System.getProperty("user.dir")+"\\result.txt";
		this.arg0 =
				"%d 번째 Game		%d 번째 Set\n"+
						"----------------------------------------------------"+"\n"
						+"Player		Point		Game		Set"+"\n"
						+"%s			%d			%d			%s\n"
						+"%s			%d			%d			%s\n"+
						"----------------------------------------------------\n";
		this.fw = new FileWriter(fileName);
	}
	
	void Record(Player p1, Player p2, int set) throws IOException {
		record=String.format(arg0, 
				p1.game+p2.game+1, set+1,
				p1.name, p1.points, p1.game,Arrays.toString(p1.sets)
				, p2.name, p2.points, p2.game,Arrays.toString(p2.sets));
		
		fw.write(record);
		
		//fw.close();
		
	}
	
	void Close() throws IOException {
		fw.close();	
	}
}

class Score{
	Print pr = new Print();
	Write wr;
	static Random rnd = new Random();
	static Player getter= new Player();
	static Player getter_game=new Player();
	boolean server_flag = rnd.nextBoolean(); //서브권자 설정
	int set=0;
	
	
	Score() throws IOException{
		wr=new Write();
	}
	
	
	
	void getSet(Player p1, Player p2, boolean duceFlag,Player getter_set,GameType game_type) throws IOException {
		getter = p1;
		getter_game=p1;

		while (Math.max(p1.win_set,p2.win_set)<= Math.rint(game_type.setNum/2)) {  // SetContinueCheck () : 과반수 "이상"의 게임 승리시  3중 2, (남자 단식의 경우 5중 3 ) break(종료)
			p1.game=0;
			p2.game=0;
			getGame(getter,p1,p2,duceFlag,getter_set);///// while 2 // 여섯 게임 끝

			//승자???
			//1)
			//getter_set=getter;
			//2)
			getter_set=p1.game>p2.game?p1:p2;

			// 승자 세트 ++;
			getter_set.win_set++;
			p1.sets[set]=p1.game;
			p2.sets[set]=p2.game;

			// 세트수 까지 포함한 전광판 출력 
			pr.printTotal(p1,p2);
			wr.Record(p1, p2, set);
			set++;
		}

		System.out.println("<<<<<<게임종료>>>>>>");

		// 승자 받기 
		Player final_winner;
		final_winner= p1.win_set>p2.win_set? p1:p2;
		// 승자 출력 
		System.out.printf("●US Open Tennis 2019● WINNER IS [%s]", final_winner.name);
		wr.fw.write("=====END OF WRITE=====");
	}

	void getGame(Player getter,Player p1, Player p2, boolean duceFlag,Player getter_set) throws IOException {
		System.out.printf("●US Open Tennis 2019●  [%s] VS [%s]\n",p1.name,p2.name );

		duceFlag=false;
		while(duceFlag?Math.max(p1.game,p2.game)<7:Math.max(p1.game, p2.game)!=6) {
			p1.points=0;
			p2.points=0; // 초기화 
			wr.Record(p1, p2, set);
			

			getter.server=server_flag?p1.name:p2.name;
			pr.serveChange(getter.server);
			getPoint(p1,p2,getter); 
			// 한쪽의 points 가 4가 될때까지 반복 ( 즉, 한쪽의 points < 4 인 동안 반복 ) 

			// 반복문 빠져나오면 << 한게임 결과 출력 >>

			// 승자?
			getter_game=p1.points>p2.points?p1:p2;

			getter_game.game++;// 반복문 빠져나왔을때 getter는 승자 
			pr.courtChange(p1, p2);
			server_flag=!server_flag;
			pr.printGame(getter_game,p1,p2);
			wr.Record(p1, p2, set);
			
			if (p1.game==5&&p2.game==5) { // 게임 듀스 상황 
				System.out.println("= 게임 듀스 상황 =");
				duceFlag=true;
				continue;
			}

			if (p1.game==6&&p2.game==6) { // 타이브레이크 상황
				System.out.println("= 타이브레이크 상황 =");
				pr.serveChange(getter.server);
				getter_set=tieBreak(p1,  p2); //Cannot make a static reference to the non-static method tieBreak(Player, Player) from the type Exercise00
				pr.printTieBreak(p1, p2);
				wr.Record(p1, p2, set);
			}
		}
	}

	void getPoint(Player p1, Player p2, Player getter) throws IOException {

		while (getter.points<4) {
			getter = rnd.nextBoolean() ? p1:p2;
			getter.points++;

			int []score = {0,15,30,40,p1.game+p2.game+1}; // 1에서득점
			int get_p1score = score[p1.points];
			int get_p2score = score[p2.points]; //0 1 2 랜덤 생성

			pr.printPoint(getter,p1,p2,get_p1score,get_p2score,score);
			wr.Record(p1, p2, set);
			if(check_PointDuce(p1,p2,getter)) { break;}
		}
	}

	private Player tieBreak(Player p1, Player p2) {
		//게임이 시작될 때마다 각 플레이어 포인트 초기화
		p1.points = 0; 
		p2.points = 0;

		int points = 0;//랜덤하려고
		String disduce = "Point deuce";//듀스표시용

		//둘중 하나의 포인트가 7포인트가 될 때까지 반복
		//일단 타이브레이크에서 듀스 없는 상황
		int turn = 1;//반복문 돌리고자. 타이브레이크는 12포인트중 7포인트니까	
		boolean flag_deuce=false;
		while (flag_deuce?Math.abs(p1.points-p2.points)<2:turn<=12) {
			if(turn%2==0)System.out.println("[Serve Change]");
			if((turn)%7==0) System.out.println("@@@Court Change@@@");
			System.out.println("turn="+turn);
			points = (int) ((Math.random()*10)+1);
			if(points%2!=0) p1.points++;
			else p2.points++;
			turn++;
			System.out.println("point1 ="+p1.points);
			System.out.println("point2 ="+p2.points);
			if(p1.points==6&&p2.points==6) {
				System.out.println(disduce);
				flag_deuce=true;
				continue;
			}
			//반복문 빠져나오는 상황
			if(p1.points==7) {
				p1.game++;
				break;
			}
			else if (p2.points==7) {
				p2.game++;
				break;
			}	
		}
		//return point1==7 ? game1 : game2;//7 or 6

		return Math.max(p1.points, p2.points)==p1.points? p1:p2;

		/*
		if(point1==7)  return game1;
		else if (point2==7)  return game2;
		else return 6;//듀스
		 */
	}

	boolean check_PointDuce(Player p1, Player p2, Player getter) throws IOException {

		if (p1.points==3&&p2.points==3) {

			System.out.println("듀스상황");

			while (Math.abs(p1.points-p2.points)!=2) {
				getter=rnd.nextBoolean() ?p1:p2;
				getter.points++;

				String duce_score[] = {"40","AD","  "} ;
				String duce_p1score;  
				String duce_p2score;

				if( p1.points==p2.points) {
					duce_p1score= duce_score[0];
					duce_p2score= duce_score[0];
				}else if (getter==p1){
					duce_p1score=duce_score[1];
					duce_p2score=duce_score[2];
				}else {
					duce_p1score=duce_score[2];
					duce_p2score=duce_score[1];
				}
				if(Math.abs(p1.points-p2.points)==2)
					System.out.printf("%2s 득점 : [%d번째 게임 승리] \n ", getter.name, p1.game+p2.game+1);
				else System.out.printf("%2s 득점 : [%s]-[%s] \n ", getter.name, duce_p1score, duce_p2score);
				System.out.printf("p1 point : %d , p2 point : %d \n", p1.points, p2.points);
				wr.Record(p1, p2, set);
			}
			System.out.println("듀스상황 끝");
			return true;
		}else {
			return false;
		}


	}
}

class Print{
	void printPoint(Player getter,Player p1,Player p2,int get_p1score,int get_p2score,int []score){
		if(get_p1score==score[4]||get_p2score==score[4])
			System.out.printf("%2s 득점 : [%d번째 게임 승리]\n", getter.name, p1.game+p2.game+1);
		else System.out.printf("%2s 득점 : [%d]-[%d] \n ", getter.name, get_p1score, get_p2score);
		System.out.printf("p1 point : %d , p2 point : %d \n", p1.points, p2.points);
	}
	void printGame(Player getter_game,Player p1,Player p2){
		drawLine('-', 42);
		System.out.printf("Point 결과 => %s Point : %d , %s Point : %d\nGame 결과 => %s Game : %d, %s Game : %d\n"
				, p1.name, p1.points, p2.name, p2.points, p1.name, p1.game, p2.name, p2.game );
		drawLine('-', 42);
	}
	void printTotal(Player p1, Player p2) {
		drawLine('*', 60);
		System.out.println("Player	Point	Game	Set");
		System.out.printf("%s	%d	%d	%s\n",p1.name, p1.points, p1.game,Arrays.toString(p1.sets));
		System.out.printf("%s	%d	%d	%s\n",p2.name, p2.points, p2.game,Arrays.toString(p2.sets));
		drawLine('*', 60);
	}
	void printTieBreak(Player p1, Player p2) {
		System.out.printf("Point 결과 => %s Point : %d , %s Point : %d\nGame 결과 => %s Game : %d, %s Game : %d\n"
				, p1.name, p1.points, p2.name, p2.points, p1.name, p1.game, p2.name, p2.game );
	}
	void courtChange(Player p1,Player p2){
		if((p1.game+p2.game)%2==1) System.out.println("@@@Court Change@@@");
	}
	void serveChange(String server) {
		System.out.printf(">%s Service Game<\n",server);
	}
	void drawLine(char style, int n) {
		for (int i = 1; i <= n ; i++) {
			System.out.printf("%c", style);
		}		
		System.out.println();
	}
}

class GameType{  //Player 클래스와 포함관계 
	//fields
	int setNum;
	Player p1, p2;
	String gameType;
	String 인원수, 성별; // 필요할지 모르지만 일단 무조건 필드로 받아두기 

	// constructors
	GameType(String 인원수선택, String 성별선택){
		this.인원수=인원수선택;
		this.성별=성별선택;
		this.setNum= (인원수선택.equals("single")&&성별선택.equals("man"))? 5 : 3 ;
		this.gameType=this.성별+this.인원수;
		//	p1=new Player(setNum);
		//	p2=new Player(setNum);

	}


	// methods

}

class Player{
	//fields
	int points;
	int[] sets ;
	int win_set;// 이긴세트수 
	//ArrayList<Integer> sets;
	String name;
	int game;
	String server;
	//constructors
	Player(){}
	Player(int setNum, String name){ // 3 or 5
		this.sets = new int[setNum];
		//ArrayList<>();
		this.name=name;
	}
	// methods
	void serve(){
		// 서브 성공 하면 +1 아니면 +0
		//points+=     ? 1 : 0 ; 
		Random rnd=new Random();
		points+=1*rnd.nextInt(2);

	}
}
