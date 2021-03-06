import java.util.ArrayList;
import java.util.List;

public abstract class User {
	protected String username;
	protected String password;
	protected String type;
	protected List<Course> courses;
	
	public User(String username, String password, String type){
		this.username = username;
		this.password = password;
		this.type = type;
		courses = new ArrayList<>(); 
		
		//Commented out to prevent NullPointerException
		loadCourse();
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getType() {
		return type;
	}
	
	public void resetPassword(String password) {
		this.password = password;
	}
	
	public boolean isCourseExist(String name) {
		for (Course course : courses) {
			if (course.getCourseName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void loadCourse() {
		List<Course> list = Database.loadCourses(getUsername());
		for (Course c : list) {
			courses.add(c);
		}
	}
	
	public List<Course> getCourse() {
		return courses;
	}
	
	public static boolean isValidCode(String s) {
		boolean isValid = true;
		if (s.length() == 4) {
			for(char c : s.toCharArray()) {
				if (!Character.isLetterOrDigit(c)) {
					isValid = false;
				}
			}
		} else {
			isValid = false;
		}
		return isValid;
	}
	
	//should be in student class
	public void submitResult(String username, Quiz quiz) {
		String quizName = quiz.getName();
		int quizID = Database.getQuizID(quizName);
		int userID = Database.getUserID(username);
		int answerID, questionID;
		List<Question> questions = quiz.getQuestions();
		for (Question q : questions) {
			for(Answer a : q.getAnswers()) {
				if (a.isSelected()){
					answerID = a.getAnswerID();
					questionID = Database.getQuestionID(answerID);
					Database.addQuestionResult(userID, quizID, questionID, answerID);
				}
			}
		}
		
		calculateQuizResult(userID, quizID);
	}
	
	//should be in student class
	public void calculateQuizResult(int userID, int quizID) {
		Database.calculateQuizResult(userID, quizID);
	}
	
	public void printUserInfo(){
		System.out.println("Username: " + username + ", Password: " + password + ", Type: " + type);
	}
	
	public abstract void addCourse(String courseName, String accessCode);
	
}
