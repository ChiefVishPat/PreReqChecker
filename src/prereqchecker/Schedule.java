package prereqchecker;
import java.util.*;

public class Schedule {
    private HashMap<String, HashSet<String>> courseList;   //hashmap for each courseID and a list of its pre-reqs
    private HashMap<String, Boolean> marked;      //hashmap used for DFS when finding preReqs
    
    

    //constructor. Makes a blank schedule
    public Schedule(){
        courseList = new HashMap<>();
        marked = new HashMap<>();
    }

    //getters
    public HashMap<String, HashSet<String>> getCourseHashMap(){
        return courseList;
    }

    public HashMap<String, Boolean> getMarkedHashMap() {
        return marked;
    }

    public HashSet<String> getCourseReqSet(String id) { //returns the course pre-reqs given a course id
        return courseList.get(id);
    }

    public int getCourseListLength(){
        return courseList.size();
    }

    public int getMarkedLength(){
        return marked.size();
    }

    public HashSet<String> getAllCoursesTakenForCourse(String id) {
        HashSet<String> temp = new HashSet<>();
        for (String course : this.getCourseReqSet(id)) {
            temp.add(course);
            DFS(course, temp);
        }

        return temp;
    }

    public HashSet<String> getAllCoursesTakenForCourse(HashSet<String> collectionOfCourses) {
        HashSet<String> temp = new HashSet<>(); // will hold all of the direct and indirect pre-reqs of the courses taken
        for (String course : collectionOfCourses) {
            DFS(course, temp);
        }
        return temp;
    }

    //helper methods I created
    public void resetMarkedList(){      //resets all boolean values to false
        for(String id : marked.keySet()){
            marked.put(id, false);
        }
    }

    public void addCourse(String id){
        courseList.put(id, new HashSet<String>());
        marked.put(id,false);
    }

    public void addCourseAndPreReq(String id, String preReq){
        HashSet<String> preReqs = getCourseReqSet(id);
        preReqs.add(preReq);
        courseList.put(id, preReqs);
        marked.put(id, false);
    }

    public void addPreReq(String id, String req){
        HashSet<String> temp = getCourseReqSet(id);
        temp.add(req);

        courseList.put(id, temp);
        marked.put(id, false);
    }

    //makes an adjacency list
    public void makeAdjList(Schedule sched, String fileIn){
        StdIn.setFile(fileIn);
        int length = StdIn.readInt();

        for (int i = 0; i < length; i++) { // adds all the course keys into hashmap
            String id = StdIn.readString();
            sched.addCourse(id);
        }

        length = StdIn.readInt();

        for (int i = 0; i < length; i++) { // adds pre-reqs into hashmap
            String id = StdIn.readString();
            String req = StdIn.readString();

            sched.addPreReq(id, req);
        }
    }

    //makes an adjacency list and writes it to a file
    public void makeAdjList(Schedule sched, String fileIn, String fileOut) {
        this.makeAdjList(sched, fileIn);
        StdOut.setFile(fileOut);
        String courseAndReqs = null; // build a string of the course + prereqs
        for (String course : sched.getCourseHashMap().keySet()) {
            courseAndReqs = course + " ";
            HashSet<String> preReqs = sched.getCourseReqSet(course);
            for (String req : preReqs) {
                courseAndReqs += req + " ";
            }
            StdOut.println(courseAndReqs + " ");
        }
    }

    // courseID is the pre-req course (of the advanced course) we are checking
    // possibleReqs is the HashSet for the advanced course
    public String isValid(String advancedCourse, String possibleReq) {
        HashSet<String> allPossibleReqs = new HashSet<>(); // HashSet to hold all possible pre-reqs

        DFS(advancedCourse, allPossibleReqs);

        //System.out.println(allPossibleReqs);     //print the HashSet of all possible direct/indirect pre-reqs

        if (allPossibleReqs.contains(possibleReq))
            return "NO";
        return "YES";

    }

    public void DFS(String advancedCourse, HashSet<String> possibleReqs){
        resetMarkedList();
        for(String preReq : getCourseReqSet(advancedCourse)){
            if(marked.get(preReq) == false ){
                possibleReqs.add(preReq);
                DFS(preReq, possibleReqs);
            }
        }
        marked.put(advancedCourse, true);
    }

    public HashSet<String> allEligibleCourses (HashSet<String> coursesTaken){
        HashSet<String> allEligibleCourses = new HashSet<>();   //will hold all eligible courses
        HashSet<String> allCoursesTaken = this.getAllCoursesTakenForCourse(coursesTaken);
        for(String c : coursesTaken) allCoursesTaken.add(c);


        for(String courseID : courseList.keySet()){
            if(!allCoursesTaken.contains(courseID)){
                if(this.canTakeCourse(courseID, allCoursesTaken)){
                    allEligibleCourses.add(courseID);
                }
            }
        }

        return allEligibleCourses;
    }

    public Boolean canTakeCourse(String advancedCourse, HashSet<String> allTakenCourses){
        HashSet<String> advancedPreReqs = this.getAllCoursesTakenForCourse(advancedCourse);

        for(String req : advancedPreReqs){
            if(!allTakenCourses.contains(req)){
                return false;
            }

        }
        return true;
    }

    public HashSet<String> needToTake(String targetID, HashSet<String> coursesTaken){
        HashSet<String> allCoursesTaken = this.getAllCoursesTakenForCourse(coursesTaken);
        for(String c : coursesTaken) allCoursesTaken.add(c);

        HashSet<String> targetIDReqs = this.getAllCoursesTakenForCourse(targetID);

        targetIDReqs.removeAll(allCoursesTaken);

        return targetIDReqs;
    }

    public void schedulePlan(String targetID, HashSet<String> coursesTaken){
        HashMap<Integer, HashSet<String>> plan = new HashMap<>();   //key: semester Value: hashset of courses
        HashSet<String> coursesNeedToTake = this.needToTake(targetID, coursesTaken);

        HashSet<String> preReqsNeeded = new HashSet<>();       //will contain all pre-reqs needed given a courseID
        HashSet<String> semesterCourses = new HashSet<>();      //will contain all the courses yuo can take in a semester
        
        int semester = 1;
        while(!coursesNeedToTake.isEmpty()){
            for(String courseID : coursesNeedToTake){
                preReqsNeeded = this.needToTake(courseID, coursesTaken);
                if(preReqsNeeded.isEmpty()) semesterCourses.add(courseID);
            }
            HashSet<String> temp;
            if(!semesterCourses.isEmpty()){
                temp = new HashSet<>();
                for(String s : semesterCourses){    //creates deep copy of semesterCourses
                    temp.add(s);
                }

                plan.put(semester, temp);
                semester++;

                coursesNeedToTake.removeAll(temp);  //removes the courses I will take in the semester from need to take

                for(String s : temp) coursesTaken.add(s); //adds the courses i will take to the coursesTaken HashSet
             
            }
        }

        StdOut.println(plan.size());
        for(int s : plan.keySet()){
            for(String c : plan.get(s)) StdOut.print(c + " ");
            StdOut.println();
        }

    }
}
